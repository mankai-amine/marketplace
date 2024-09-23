package org.styd.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.styd.store.entity.User;
import org.styd.store.exception.InsufficientStockException;
import org.styd.store.exception.PaymentFailureException;
import org.styd.store.securingweb.CustomUserDetails;
import org.styd.store.service.CheckoutService;

@Controller
public class CheckoutController {

    @Autowired
    private CheckoutService checkoutService;

    @PostMapping("/buyer/checkout")
    public String checkout(@AuthenticationPrincipal CustomUserDetails currentUser, RedirectAttributes redirectAttributes) {

        User buyer = currentUser.getUser();

        try {
            checkoutService.processCheckout(buyer);
            redirectAttributes.addFlashAttribute("flashMessageSuccess", "Checkout completed successfully!");
            return "redirect:/";
        } catch (InsufficientStockException | PaymentFailureException e) {
            redirectAttributes.addFlashAttribute("flashMessageError", e.getMessage());
            return "redirect:/buyer/cart";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("flashMessageError", "Checkout failed.");
            return "redirect:/buyer/cart";
        }
    }

}

