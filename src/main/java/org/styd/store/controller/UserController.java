package org.styd.store.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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
    public String uploadProfilePicture(@PathVariable Long userId, @RequestParam("file") MultipartFile file,
                                       RedirectAttributes redirectAttributes) {
        if (file.isEmpty()){
            redirectAttributes.addFlashAttribute("message", "Please upload a file and try again.");
            return "redirect:/";
        }
        userService.uploadUserProfilePicture(file, userId);
        redirectAttributes.addFlashAttribute("message", "Profile picture uploaded successfully.");
        return "redirect:/";
    }

    @GetMapping("/users/uploadimgform")
    public String uploadImgForm(@AuthenticationPrincipal CustomUserDetails currentUser, Model model){
//        if (result.hasErrors()){
//            redirectAttributes.addFlashAttribute("message", "An error occurred.");
//            return "redirect:/";
//        }
//        user = currentUser;
        System.out.println("Banana" + currentUser.getUser());
        model.addAttribute("user", currentUser.getUser());
        return "user-image-upload";
    }

}
