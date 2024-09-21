package org.styd.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.styd.store.entity.User;
import org.styd.store.exception.InsufficientStockException;
import org.styd.store.exception.PaymentFailureException;
import org.styd.store.securingweb.CustomUserDetails;
import org.styd.store.service.CheckoutService;

@RestController
@RequestMapping("/buyer/checkout")
public class CheckoutController {

    @Autowired
    private CheckoutService checkoutService;

    @PostMapping
    public String checkout(@AuthenticationPrincipal CustomUserDetails currentUser, RedirectAttributes redirectAttributes) {

        User buyer = currentUser.getUser();

        try {
            checkoutService.processCheckout(buyer);
            redirectAttributes.addFlashAttribute("message", "Checkout completed successfully!");
            return "redirect:/";
        } catch (InsufficientStockException | PaymentFailureException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/checkout";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Checkout failed.");
            return "redirect:/checkout";
        }
    }

}

