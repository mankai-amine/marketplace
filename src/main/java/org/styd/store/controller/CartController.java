package org.styd.store.controller;

import jakarta.transaction.Transactional;
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
            Optional<User> optUser = userRepository.findById(user.getId());
            if (optUser.isPresent()) {
                User validUser = optUser.get();
                return validUser.getCartItems();
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    // TODO incorporate insufficientstockexception?
    @PostMapping("/add")
    public Set<CartItem> addToCart(@AuthenticationPrincipal CustomUserDetails user,
                                   @RequestParam Long productId, @RequestParam int amount) {
        if (amount <= 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount must be at least 1.");
        }
        if (!checkUser(user)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        if (!checkProduct(productId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        Product stockCheck = productRepository.findById(productId).get();
        if ((stockCheck.getStockAmount() - amount) < 0) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Not enough product in stock");
        }
        User validUser = userRepository.findById(user.getId()).get();
        validUser.addToCart(stockCheck, amount);
        userRepository.save(validUser);

        return validUser.getCartItems();
    }

    @PostMapping("/add/button")
    public Set<CartItem> addToCartButton(@AuthenticationPrincipal CustomUserDetails user,
                                         @RequestParam Long productId) {
        if (!checkUser(user)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        if (!checkProduct(productId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        Product stockCheck = productRepository.findById(productId).get();
        if ((stockCheck.getStockAmount() - 1) < 0) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Not enough product in stock");
        }
        User validUser = userRepository.findById(user.getId()).get();
        validUser.addToCart(stockCheck, 1);
        userRepository.save(validUser);

        return validUser.getCartItems();
    }

    // transactional to make sure the cart item deletion and user save are atomic
    @DeleteMapping("/remove")
    @Transactional
    public Set<CartItem> removeFromCart(@AuthenticationPrincipal CustomUserDetails user,
                                        @RequestParam Long productId) {
        if (!checkUser(user)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        if (!checkProduct(productId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        Product toRemove = productRepository.findById(productId).get();
        User validUser = userRepository.findById(user.getId()).get();

        validUser.removeFromCart(toRemove);
        validUser = userRepository.saveAndFlush(validUser);

        return validUser.getCartItems();
    }

    @DeleteMapping("/remove/button")
    @Transactional
    public Set<CartItem> removeFromCartButton(@AuthenticationPrincipal CustomUserDetails user,
                                              @RequestParam Long productId) {
        if (!checkUser(user)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        if (!checkProduct(productId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        Product toRemove = productRepository.findById(productId).get();
        User validUser = userRepository.findById(user.getId()).get();

        validUser.removeOneFromCart(toRemove);
        validUser = userRepository.saveAndFlush(validUser);

        return validUser.getCartItems();
    }

    @DeleteMapping("/clear")
    public Set<CartItem> clearCart(@AuthenticationPrincipal CustomUserDetails user) {
        if (!checkUser(user)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        User validUser = userRepository.findById(user.getId()).get();
        validUser.clearCart();
        userRepository.save(validUser);
        return validUser.getCartItems();
    }

    private boolean checkUser(@AuthenticationPrincipal CustomUserDetails user) {
        Optional<User> checkUser = userRepository.findById(user.getId());
        return checkUser.isPresent();
    }

    private boolean checkProduct(Long productId) {
        Optional<Product> checkProduct = productRepository.findById(productId);
        return checkProduct.isPresent();
    }

}
