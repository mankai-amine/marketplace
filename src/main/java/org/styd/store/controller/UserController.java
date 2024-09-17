package org.styd.store.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.styd.store.entity.User;
import org.styd.store.repository.UserRepository;
import org.styd.store.securingweb.CustomUserDetails;
import org.styd.store.service.UserService;


@Slf4j
@Controller
public class UserController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String viewRegisterPage(Model model){
        model.addAttribute("user", new User());
        return "register-form";
    }

    @PostMapping("/register")
    public String processRegister(@Valid User user, BindingResult result){

        if (result.hasErrors()) {
            log.debug(String.valueOf(result));
            return "register-form";
        }

        if (!user.getPassword().equals(user.getPassword2())) {
            result.rejectValue("password2", "passwordsDoNotMatch", "Passwords must match");
            return "register-form";
        }

        if (userRepo.findByUsername(user.getUsername()) != null) {
            result.rejectValue("username", "usernameExists", "Username already exists");
            return "register-form";
        }

        if (userRepo.findByEmail(user.getEmail()) != null) {
            result.rejectValue("email", "emailExists", "Email already exists");
            return "register-form";
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepo.save(user);

        return "register-success";
    }

    @PostMapping("/users/{userId}/uploadProfilePicture")
    public String uploadProfilePicture(@PathVariable Long userId, @RequestParam("file") MultipartFile file, @AuthenticationPrincipal CustomUserDetails currentUser,
                                       RedirectAttributes redirectAttributes) {
        if (!currentUser.getUser().getId().equals(userId)) {
            redirectAttributes.addFlashAttribute("message", "ID mismatch error.");
            return "redirect:/";
        }
        if (file.isEmpty()){
            redirectAttributes.addFlashAttribute("message", "Please upload a file and try again.");
            return "redirect:/";
        }
        try {
            userService.uploadUserProfilePicture(file, userId);
            redirectAttributes.addFlashAttribute("message", "Profile picture uploaded successfully.");
//             FIXME more precise exception handling?
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "An error occurred while uploading profile picture.");
        }

        return "redirect:/";
    }

    // FIXME Turn this into a settings page later
    @GetMapping("/users/uploadimgform")
    public String uploadImgForm(@AuthenticationPrincipal CustomUserDetails currentUser, Model model,
                                RedirectAttributes redirectAttributes){
        if (currentUser.getUser() == null){
            redirectAttributes.addFlashAttribute("message", "An error occurred.");
            return "redirect:/";
        }

        // Temporary solution around product LazyRetrieval error, keeping in case it's needed again
//        User user = userRepo.findById(currentUser.getUser().getId())
//                .orElseThrow(() -> new RuntimeException("User not found"));

        // Below code works again with the User class declaring a new empty HashSet<> for products when initialized
        model.addAttribute("user", currentUser.getUser());
        return "user-image-upload";
    }

    @GetMapping("/admin")
    public String adminMain(){
        return "admin-main";
    }

    @GetMapping("/admin/categories")
    public String adminCategories(){
        return "admin-categories";
    }

    @GetMapping("/admin/categories/add")
    public String adminCategoriesAdd(){
        return "admin-categories-add";
    }

    @GetMapping("/admin/users")
    public String adminUsers(){
        return "admin-users";
    }

    @GetMapping("/admin/orders")
    public String adminOrders(){
        return "admin-orders";
    }
}
