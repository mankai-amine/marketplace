package org.styd.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.styd.store.entity.CartItem;
import org.styd.store.entity.Product;
import org.styd.store.entity.User;
import org.styd.store.repository.ProductRepository;
import org.styd.store.repository.UserRepository;
import org.styd.store.securingweb.CustomUserDetails;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/get")
    public Set<CartItem> getCart(@AuthenticationPrincipal CustomUserDetails user) {
        if (checkUser(user)) {
            return user.getUser().getCartItems();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @PostMapping("/add")
    public Set<CartItem> addToCart(@AuthenticationPrincipal CustomUserDetails user,
                                   @RequestParam Long productId, @RequestParam int amount) {
        if (checkUser(user)) {

        }
    }

    private boolean checkUser(@AuthenticationPrincipal CustomUserDetails user) {
        Optional<User> checkUser = userRepository.findById(user.getId());
        return checkUser.isPresent();
    }

    private boolean checkProduct(Product product) {
        Optional<Product> checkProduct = productRepository.findById(product.getId());
        return checkProduct.isPresent();
    }

}
