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

// TODO review which data members and auto wired fields are necessary
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
        model.addAttribute("products", productRepository.findAll());

        Long currentUserId = customUserDetailsService.getCurrentUserId();
        model.addAttribute("currentUserId", currentUserId);

        return "index";
    }

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

        model.addAttribute("categories", categoryRepository.findAll());

        return "add-product";
    }

    @GetMapping({"/seller/edit/{prodId}"})
    public String editProduct(Model model, @PathVariable Long prodId, Principal principal){
        Optional<Product> product = productRepository.findById(prodId);
        if (product.isEmpty()) {
            return "redirect:/";
        }
        model.addAttribute("product", product.get());

        model.addAttribute("categories", categoryRepository.findAll());

        // if a seller tries to edit another seller's product
        Long sellerId = product.get().getSeller().getId();
        boolean isSellerEqualUser = checkSellerEqualUser(principal, sellerId);
        if(!isSellerEqualUser){
            return "redirect:/";
        }

        return "add-product";
    }

    @GetMapping("/seller/delete/{prodId}")
    public String delete(@PathVariable Long prodId, RedirectAttributes redirAttrs, Principal principal){

        // if a seller tries to delete another seller's product
        Optional<Product> product = productRepository.findById(prodId);
        if (product.isEmpty()) {
            return "redirect:/";
        }
        Long sellerId = product.get().getSeller().getId();
        boolean isSellerEqualUser = checkSellerEqualUser(principal, sellerId);
        if(!isSellerEqualUser){
            return "redirect:/";
        }

        productRepository.deleteById(prodId);
        redirAttrs.addFlashAttribute("flashMessageSuccess", "Product deleted successfully");

        return "redirect:/";
    }

    @PostMapping("/seller/saveProduct")
    public String saveProduct(@Valid Product product, BindingResult result, @RequestParam("file") MultipartFile file,
                              Principal principal, Model model, RedirectAttributes redirAttrs){

        if (result.hasErrors()) {
            log.debug(String.valueOf(result));
            model.addAttribute("categories", categoryRepository.findAll());
            return "add-product";
        }

        // Custom validation to avoid malicious user doing html edits on hidden id field
        String username = principal.getName();
        if (product.getId() != null) {
            Product toCheck = productRepository.findProductById(product.getId());
            String toConfirm = toCheck.getSeller().getUsername();
            if (!toConfirm.equals(username)) {
                redirAttrs.addFlashAttribute("flashMessageError", "User Mismatch.");
                return "redirect:/";
            }
        }

        User seller = userRepository.findByUsername(username);
        product.setSeller(seller);

        if (!file.isEmpty()){
            String fileUrl = productService.uploadProductImage(file, product.getId());
            product.setImageUrl(fileUrl);
        }

        productRepository.save(product);

        redirAttrs.addFlashAttribute("flashMessageSuccess", "Product saved successfully");

        return "redirect:/";
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

}
