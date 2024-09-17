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
import org.styd.store.repository.ProductRepository;
import org.styd.store.repository.UserRepository;
import org.styd.store.securingweb.CustomUserDetailsService;
import org.styd.store.service.ProductService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
//import org.styd.store.service.ProductService;

// TODO review which data members and auto wired fields are necessary
@Slf4j
@Controller
public class ProductController {

//    private final ProductService productService;

//    @Autowired
//    public ProductController(ProductService productService) {
//        this.productService = productService;
//    }


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    // proof of concept. Once we have a db to call products from we won't need this to test
//    Long idOne = 1L;
//    Long idTwo = 1L;
//    Long idThree = 1L;
//    Long idFour = 2L;
//    Long idFive = 1L;
//    Long idSix = 1L;
//    Long idSeven = 3L;
//    Long idEight = 1L;
//    Long idNine = 1L;
//    Product testProduct1 = new Product(idOne, idTwo, idThree, "Trombone", "Trombones are the best",
//            100.15, 1, null, false);
//    Product testProduct2 = new Product(idFour, idFive, idSix, "Bananas", "Bananas are delicious and full of potassium",
//            3.50, 5, null, false);
//    Product testProduct3 = new Product(idSeven, idEight, idNine, "Cat litter", "Because you love Sir Reginald Napsalot III so much",
//            50.0, 2, null, false);
//    List<Product> listOfProducts = new ArrayList<>(){{
//        add(testProduct1);
//        add(testProduct2);
//        add(testProduct3);
//    }};

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
        return "add-product";
    }

    @GetMapping({"/seller/edit/{prodId}"})
    public String editProduct(Model model, @PathVariable Long prodId){
        Optional<Product> product = productRepository.findById(prodId);
        if (product.isEmpty()) {
            return "redirect:/seller/products";
        }
        model.addAttribute("product", product);
        return "add-product";
    }

    @GetMapping("/seller/delete/{prodId}")
    public String delete(@PathVariable Long prodId, RedirectAttributes redirAttrs){
        productRepository.deleteById(prodId);
        redirAttrs.addFlashAttribute("flashMessageSuccess", "Product deleted successfully");
        return "redirect:/seller/products";
    }

    @PostMapping("/seller/saveProduct")
    public String saveProduct(@Valid Product product, BindingResult result, @RequestParam("file") MultipartFile file,
                              Principal principal, RedirectAttributes redirAttrs){

        if (result.hasErrors()) {
            log.debug(String.valueOf(result));
            return "add-product";
        }

        String username = principal.getName();
        User seller = userRepository.findByUsername(username);
        product.setSeller(seller);

//        // upload the product image
//        productService.uploadProductImage(file, product.getId());
//        // get the image URL from the database
//        product.setImageUrl(product.getImageUrl());

        // dummy data to test the controller
        product.setImageUrl("test-url");

        productRepository.save(product);

        redirAttrs.addFlashAttribute("flashMessageSuccess", "Product saved successfully");

        return "redirect:/seller/products";
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
