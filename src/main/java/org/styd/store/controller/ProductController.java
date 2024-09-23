package org.styd.store.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.styd.store.entity.Product;
import org.styd.store.entity.User;
import org.styd.store.repository.CategoryRepository;
import org.styd.store.repository.ProductRepository;
import org.styd.store.repository.UserRepository;
import org.styd.store.securingweb.CustomUserDetailsService;
import org.styd.store.service.ProductService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class ProductController {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @GetMapping("/")
    public String viewIndex(Model model) {
        model.addAttribute("products", productRepository.findByIsDeletedFalse());

        Long currentUserId = customUserDetailsService.getCurrentUserId();
        model.addAttribute("currentUserId", currentUserId);

        if (currentUserId != null) {
            Optional<User> toCheck = userRepository.findById(currentUserId);
            if (toCheck.isPresent()) {
                User toShow = toCheck.get();
                model.addAttribute("user", toShow);
            }
        }

        return "index";
    }

    // FIXME Null Pointer error if product doesn't exist
    @GetMapping("/product/{prodId}")
    public String viewProduct(@PathVariable Long prodId, Model model, Principal principal) {
        Product product = productRepository.findProductById(prodId);

        model.addAttribute("product", product);

        Long sellerId = product.getSeller().getId();
        model.addAttribute("sellerId", sellerId);

        boolean isSellerEqualUser = checkSellerEqualUser(principal, sellerId);
        model.addAttribute("isSellerEqualUser", isSellerEqualUser);

        return "single-product";
    }

    @GetMapping({"/seller/{sellerId}/products"})
    public String viewOwnProducts(Model model, Principal principal, @PathVariable Long sellerId) {

        boolean isSellerEqualUser = checkSellerEqualUser(principal, sellerId);

        List<Product> products = productRepository.findBySellerId(sellerId);
        model.addAttribute("products", products);
        model.addAttribute("isSellerEqualUser", isSellerEqualUser);

        Long currentUserId = customUserDetailsService.getCurrentUserId();
        model.addAttribute("currentUserId", currentUserId);

        return "index";
    }

    @GetMapping({"/seller/add"})
    public String addProduct(Model model){
        model.addAttribute("product", new Product());

        model.addAttribute("categories", categoryRepository.findByIsDeletedFalse());

        return "add-product";
    }

    @GetMapping({"/seller/edit/{prodId}"})
    public String editProduct(Model model, @PathVariable Long prodId, Principal principal, RedirectAttributes redirectAttributes) {
        Optional<Product> product = productRepository.findById(prodId);
        if (product.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Product not found");
            return "redirect:/";
        }
        model.addAttribute("product", product.get());

        model.addAttribute("categories", categoryRepository.findAll());

        // if a seller tries to edit another seller's product
        Long sellerId = product.get().getSeller().getId();
        boolean isSellerEqualUser = checkSellerEqualUser(principal, sellerId);
        if(!isSellerEqualUser){
            redirectAttributes.addFlashAttribute("flashMessageError", "Seller ID mismatch.");
            return "redirect:/";
        }

        return "add-product";
    }

    @GetMapping("/seller/delete/{prodId}")
    public String delete(@PathVariable Long prodId, RedirectAttributes redirAttrs, Principal principal){

        // if a seller tries to delete another seller's product
        Optional<Product> product = productRepository.findById(prodId);
        if (product.isEmpty()) {
            redirAttrs.addFlashAttribute("flashMessageError", "Product not found.");
            return "redirect:/";
        }
        Long sellerId = product.get().getSeller().getId();
        boolean isSellerEqualUser = checkSellerEqualUser(principal, sellerId);
        if(!isSellerEqualUser){
            redirAttrs.addFlashAttribute("flashMessageError", "Seller ID mismatch.");
            return "redirect:/";
        }
        String urlInsert = getUserIdFromPrincipal(principal);

        Product toDelete = product.get();
        toDelete.setIsDeleted(true);
        productRepository.save(toDelete);
        redirAttrs.addFlashAttribute("flashMessageSuccess", "Product deleted successfully");

        return "redirect:/seller/" + urlInsert + "/products";
    }

    @GetMapping("/seller/restore/{prodId}")
    public String restoreProduct(@PathVariable Long prodId, RedirectAttributes redirAttrs, Principal principal){
        Optional<Product> prodToFind = productRepository.findById(prodId);
        if (prodToFind.isEmpty()){
            redirAttrs.addFlashAttribute("flashMessageError", "Product not found.");
            return "redirect:/";
        }
        Long sellerId = prodToFind.get().getSeller().getId();
        if (!checkSellerEqualUser(principal, sellerId)){
            redirAttrs.addFlashAttribute("flashMessageError", "Seller ID mismatch.");
            return "redirect:/";
        }
        String urlInsert = getUserIdFromPrincipal(principal);

        Product prodToRestore = prodToFind.get();
        prodToRestore.setIsDeleted(false);
        productRepository.save(prodToRestore);
        redirAttrs.addFlashAttribute("flashMessageSuccess", "Product restored successfully");
        return "redirect:/seller/" + urlInsert + "/products";
    }

    @PostMapping("/seller/saveProduct")
    public String saveProduct(@Valid Product product, BindingResult result, @RequestParam("file") MultipartFile file,
                              Principal principal, Model model, RedirectAttributes redirAttrs){

        if (result.hasErrors()) {
            log.debug(String.valueOf(result));
            model.addAttribute("categories", categoryRepository.findAll());
            return "add-product";
        }

        // Custom validation to prevent overwriting other user's products when malicious user makes html edits on hidden id field
        if (product.getId() != null) {
            Product toCheck = productRepository.findProductById(product.getId());
            Long toConfirm = toCheck.getSeller().getId();
            Long currentUserId = customUserDetailsService.getCurrentUserId();
            if (!toConfirm.equals(currentUserId)) {
                redirAttrs.addFlashAttribute("flashMessageError", "User Mismatch.");
                return "redirect:/";
            }
        }

        String username = principal.getName();
        User seller = userRepository.findByUsername(username);
        product.setSeller(seller);

        if (!file.isEmpty()){
            String fileUrl = productService.uploadProductImage(file, product.getId());
            product.setImageUrl(fileUrl);
        }

        productRepository.save(product);

        String urlInsert = getUserIdFromPrincipal(principal);

        redirAttrs.addFlashAttribute("flashMessageSuccess", "Product saved successfully");

        return "redirect:/seller/" + urlInsert + "/products";
    }



    // check if the authenticated user has the same id as sellerId in the URL
    // this will grant him the possibility to modify and delete that specific list of products
    private boolean checkSellerEqualUser(Principal principal, Long sellerId) {
        if (principal != null) {
            String username = principal.getName();
            User currentUser = userRepository.findByUsername(username);
            Long currentUserId = currentUser.getId();
            return sellerId.equals(currentUserId);
        } else {
            return false;
        }
    }


    /**
     * Helper function to get the currently authenticated user's ID for URL redirect
     * @param principal Authenticated user
     * @return principal user's Long ID converted to a String for url insertion
     */
    private String getUserIdFromPrincipal(Principal principal){
        User userRedirect = userRepository.findByUsername(principal.getName());
        Long userLongRedirect = userRedirect.getId();
        return userLongRedirect.toString();
    }

}
