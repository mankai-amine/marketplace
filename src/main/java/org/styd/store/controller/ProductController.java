package org.styd.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.styd.store.entity.Product;
import org.styd.store.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
//import org.styd.store.service.ProductService;

// TODO review which data members and auto wired fields are necessary
@Controller
public class ProductController {

//    private final ProductService productService;

//    @Autowired
//    public ProductController(ProductService productService) {
//        this.productService = productService;
//    }

    @Autowired
    private ProductRepository productRepository;

    // proof of concept. Once we have a db to call products from we won't need this to test
    Long idOne = 1L;
    Long idTwo = 1L;
    Long idThree = 1L;
    Long idFour = 2L;
    Long idFive = 1L;
    Long idSix = 1L;
    Long idSeven = 3L;
    Long idEight = 1L;
    Long idNine = 1L;
    Product testProduct1 = new Product(idOne, idTwo, idThree, "Trombone", "Trombones are the best",
            100.15, 1, null, false);
    Product testProduct2 = new Product(idFour, idFive, idSix, "Bananas", "Bananas are delicious and full of potassium",
            3.50, 5, null, false);
    Product testProduct3 = new Product(idSeven, idEight, idNine, "Cat litter", "Because you love Sir Reginald Napsalot III so much",
            50.0, 2, null, false);
    List<Product> listOfProducts = new ArrayList<>(){{
        add(testProduct1);
        add(testProduct2);
        add(testProduct3);
    }};

    @GetMapping("/")
    public String viewIndex(Model model) {
        model.addAttribute("products", listOfProducts);
//        model.addAttribute("products", productRepository.findAll());
        return "index";
    }

    @GetMapping("/product/{prodId}")
    public String viewProduct(@PathVariable Long prodId, Model model) {
        int longToInt = prodId.intValue();
        Product toShow = listOfProducts.get((longToInt) -1);
        model.addAttribute("product", toShow);
        return "single-product";
    }
}
