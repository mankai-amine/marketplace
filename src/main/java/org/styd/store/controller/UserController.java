package org.styd.store.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.styd.store.entity.Category;
import org.styd.store.entity.User;
import org.styd.store.repository.CartItemRepository;
import org.styd.store.repository.CategoryRepository;
import org.styd.store.repository.UserRepository;
import org.styd.store.securingweb.CustomUserDetails;
import org.styd.store.service.UserService;

import java.security.Principal;
import java.util.Optional;


@Slf4j
@Controller
public class UserController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private UserService userService;
    @Autowired
    private CartItemRepository cartItemRepository;


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

    @GetMapping("/users/settings")
    public String settings(Principal principal, Model model) {
        String username = principal.getName();
        User currentUser = userRepo.findByUsername(username);
        // hide the credit card number
        currentUser.setCreditCard(null);
        model.addAttribute("user", currentUser);
        return "user-settings";
    }

    @PostMapping("users/settings/edit" )
    public String saveSettings(@Valid User user, BindingResult result, @RequestParam("file") MultipartFile file,
                               @AuthenticationPrincipal CustomUserDetails currentUser, RedirectAttributes redirAttrs){


        if (!user.getId().equals(currentUser.getUser().getId())){
            redirAttrs.addFlashAttribute("flashMessageError", "User mismatch, access denied.");
            return "redirect:/users/settings";
        }

        // if the form input email exist in DB
        if (userRepo.findByEmail(user.getEmail()) != null) {
            // if the current user's (spring's) id DOES NOT match the id associated with the email in the db, reject
            if (!currentUser.getId().equals(userRepo.findByEmail(user.getEmail()).getId())) {
                result.rejectValue("email", "emailExists", "Email already exists");
                return "user-settings";
            }
        }

        // check that the entered passwords match
        if (!user.getPassword().equals(user.getPassword2())) {
            result.rejectValue("password2", "passwordsDoNotMatch", "Passwords must match");
            return "user-settings";
        }

        // Hash the password and store it in the database
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
        } else{
            // keep the existing password card if no new one is provided
            user.setPassword(currentUser.getUser().getPassword());
        }

        // store the file URL in the database
        if (!file.isEmpty()){
            String fileUrl = userService.uploadUserProfilePicture(file, user.getId());
            user.setPicture(fileUrl);
        }

        // Hash the credit card and store it in the database
        if (user.getCreditCard() != null && !user.getCreditCard().isEmpty()) {
            if (checkCreditCard(user.getCreditCard())) {
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String encodedCreditCard = passwordEncoder.encode(user.getCreditCard());
                user.setCreditCard(encodedCreditCard);
            } else {
                result.rejectValue("creditCard", "creditCardNotValid", "Credit Card must be only numbers and have 12-16 characters.");
                return "user-settings";
            }
        } else {
            // keep the existing credit card if no new one is provided
            user.setCreditCard(currentUser.getUser().getCreditCard());
        }

        user.setUsername(currentUser.getUser().getUsername());
        user.setRole(currentUser.getUser().getRole());
        user.setCartItems(currentUser.getUser().getCartItems());

        userRepo.save(user);
        redirAttrs.addFlashAttribute("flashMessageSuccess", "Settings updated successfully.");

        return "redirect:/";
    }

    @GetMapping("buyer/cart")
    public String viewCart(@AuthenticationPrincipal CustomUserDetails currentUser, Model model){
        model.addAttribute("cartItems", cartItemRepository.findByBuyer(currentUser.getUser()));
        return "buyer-cart";
    }

    // Start of admin mapping

    @GetMapping("/admin")
    public String adminMain(){
        return "admin-main";
    }

    @GetMapping("/admin/categories")
    public String adminCategories(Model model){
        model.addAttribute("categories", categoryRepo.findAll());
        return "admin-categories";
    }

    @GetMapping("/admin/categories/add")
    public String adminCategoriesAdd(Model model){
        model.addAttribute("category", new Category());
        return "admin-categories-add";
    }

    @PostMapping("/admin/categories/add")
    public String processCategoriesAdd(@Valid Category category, BindingResult result, RedirectAttributes redirectAttributes){
        if (result.hasErrors()) {
            log.debug(String.valueOf(result));
            redirectAttributes.addFlashAttribute("flashMessageError", "Error occurred when creating category.");
            return "admin-categories-add";
        }
        if (categoryRepo.findByName(category.getName()).isPresent()){
            redirectAttributes.addFlashAttribute("flashMessageError", "Category already exists.");
            return "admin-categories-add";
        }
        categoryRepo.save(category);
        redirectAttributes.addFlashAttribute("flashMessageSuccess", "Category added successfully.");
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/edit/{id}")
    public String adminCategoriesEdit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes){
        Optional<Category> toEdit = categoryRepo.findById(id);
        if (toEdit.isPresent()){
            model.addAttribute("category", toEdit.get());
        } else {
            redirectAttributes.addFlashAttribute("flashMessageError", "Category not found.");
            return "redirect:/admin/categories";
        }
        return "admin-categories-edit";
    }

    @PostMapping("/admin/categories/edit/{id}")
    public String processCategoriesEdit(@Valid Category category, BindingResult result,
                                        RedirectAttributes redirectAttributes){
        if (result.hasErrors()) {
            log.debug(String.valueOf(result));
            redirectAttributes.addFlashAttribute("flashMessageError", "Error occurred when updating category.");
            return "admin-categories-edit";
        }
        Optional<Category> checkId = categoryRepo.findById(category.getId());
        if (checkId.isPresent()){
            Optional<Category> checkName = categoryRepo.findByName(category.getName());
            if (checkName.isPresent() && !checkId.get().getId().equals(checkName.get().getId())){
                redirectAttributes.addFlashAttribute("flashMessageError", "Category name already exists.");
                return "admin-categories-edit";
            }
            Category toEdit = checkId.get();
            toEdit.setName(category.getName());
            categoryRepo.save(toEdit);
            redirectAttributes.addFlashAttribute("flashMessageSuccess", "Category edited successfully.");
            return "redirect:/admin/categories";
        } else {
            redirectAttributes.addFlashAttribute("flashMessageError", "Category does not exist.");
            return "admin-categories-edit";
        }
    }

    @PostMapping("/admin/categories/delete/{id}")
    public String categoryDelete(@PathVariable Long id, RedirectAttributes redirectAttributes){
        Optional<Category> toFind = categoryRepo.findById(id);
        if (toFind.isPresent()){
            // new Category object, DON'T DELETE
            Category toDelete = toFind.get();
            toDelete.setIsDeleted(true);
            categoryRepo.save(toDelete);
            redirectAttributes.addFlashAttribute("flashMessageSuccess", "Category deleted successfully.");
            return "redirect:/admin/categories";
        } else {
            redirectAttributes.addFlashAttribute("flashMessageError", "Category not found.");
            return "redirect:/admin/categories";
        }
    }

    @PostMapping("/admin/categories/restore/{id}")
    public String categoryRestore(@PathVariable Long id, RedirectAttributes redirectAttributes){
        Optional<Category> toFind = categoryRepo.findById(id);
        if (toFind.isPresent()){
            Category toRestore = toFind.get();
            toRestore.setIsDeleted(false);
            categoryRepo.save(toRestore);
            redirectAttributes.addFlashAttribute("flashMessageSuccess", "Category restored successfully.");
        } else {
            redirectAttributes.addFlashAttribute("flashMessageError", "Category not found.");
        }
        return "redirect:/admin/categories";
    }

    // End of Admin Categories

    @GetMapping("/admin/users")
    public String adminUsers(Model model){
        model.addAttribute("users", userRepo.findAll());
        return "admin-users";
    }

    @GetMapping("/admin/users/add")
    public String adminUsersAdd(Model model){
        model.addAttribute("user", new User());
        return "admin-users-add";
    }

    @PostMapping("/admin/users/add")
    public String processUsersAdd(@Valid User user, BindingResult result,
                                  @RequestParam MultipartFile file, RedirectAttributes redirectAttributes){
        if (result.hasErrors()) {
            log.debug(String.valueOf(result));
            redirectAttributes.addFlashAttribute("flashMessageError", "Error occurred when adding user.");
            return "admin-users-add";
        }
        if (!user.getPassword().equals(user.getPassword2())) {
            result.rejectValue("password2", "passwordsDoNotMatch", "Passwords must match");
            return "admin-users-add";
        }

        if (userRepo.findByUsername(user.getUsername()) != null) {
            result.rejectValue("username", "usernameExists", "Username already exists");
            return "admin-users-add";
        }

        if (userRepo.findByEmail(user.getEmail()) != null) {
            result.rejectValue("email", "emailExists", "Email already exists");
            return "admin-users-add";
        }
        if (!file.isEmpty()){
            String fileUrl = userService.uploadUserProfilePicture(file, user.getId());
            user.setPicture(fileUrl);
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        if (!checkCreditCard(user.getCreditCard())){
            result.rejectValue("creditCard", "creditCardNotValid", "Credit Card must be only numbers and have 12-16 characters.");
            return "admin-users-add";
        }
        String encodedCreditCard = passwordEncoder.encode(user.getCreditCard());
        user.setPassword(encodedPassword);
        user.setCreditCard(encodedCreditCard);

        userRepo.save(user);
        redirectAttributes.addFlashAttribute("flashMessageSuccess", "User added successfully.");
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users/edit/{id}")
    public String adminUsersEdit(@PathVariable Long id, Model model,
                                 RedirectAttributes redirectAttributes){
        Optional<User> toEdit = userRepo.findById(id);
        if (toEdit.isPresent()){
            User nullPassAndCredit = toEdit.get();
            nullPassAndCredit.setPassword(null);
            nullPassAndCredit.setCreditCard(null);
            model.addAttribute("user", nullPassAndCredit);
        } else {
            redirectAttributes.addFlashAttribute("flashMessageError", "User not found.");
            return "redirect:/admin/users";
        }
        return "admin-users-edit";
    }

    @PostMapping("/admin/users/edit/{id}")
    public String processUsersEdit(@Valid User user, BindingResult result,
                                   MultipartFile file, RedirectAttributes redirectAttributes){
        User dbComparison = userRepo.findById(user.getId()).get();
        // if form input and db value don't match
        if (!user.getUsername().equals(dbComparison.getUsername())){
            // make sure the username doesn't already exist
            if (userRepo.findByUsername(user.getUsername()) != null) {
                result.rejectValue("username", "usernameExists", "Username already exists");
                return "redirect:/admin/users/edit/" + user.getId();
            }
            dbComparison.setUsername(user.getUsername());
        }
        // check that form input for user and user's db email are not matching
        if (!user.getEmail().equals(dbComparison.getEmail())){
            // since they don't match, db cannot already have an email like the one input on form
            if (userRepo.findByEmail(user.getEmail()) != null){
                result.rejectValue("email", "emailExists", "Email already exists");
                return "redirect:/admin/users/edit/" + user.getId();
            }
            dbComparison.setEmail(user.getEmail());
        }
        if (!user.getRole().equals(dbComparison.getRole())){
            dbComparison.setRole(user.getRole());
        }
        if (!user.getAddress().equals(dbComparison.getAddress())){
            dbComparison.setAddress(user.getAddress());
        }

        // check if file uploaded
        if (!file.isEmpty()){
            String fileUrl = userService.uploadUserProfilePicture(file, user.getId());
            dbComparison.setPicture(fileUrl);
        }

        // check if password fields are both filled and match before updating the user's password, otherwise don't
        if ((user.getPassword() != null && !user.getPassword().isEmpty()) && (user.getPassword2() != null && !user.getPassword2().isEmpty())){
            if (user.getPassword().equals(user.getPassword2())) {
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String encodedPassword = passwordEncoder.encode(user.getPassword());
                user.setPassword(encodedPassword);
            } else {
                result.rejectValue("password2", "passwordsDoNotMatch", "Passwords must match");
                return "redirect:/admin/users/edit/" + user.getId();
            }
        }

        // check if credit card edited
        if (user.getCreditCard() != null && !user.getCreditCard().isEmpty()){
            if (!checkCreditCard(user.getCreditCard())){
                result.rejectValue("creditCard", "creditCardNotValid", "Credit Card must be only numbers and have 12-16 characters.");
                return "redirect:/admin/users/edit/" + user.getId();
            }
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedCreditCard = passwordEncoder.encode(user.getCreditCard());
            dbComparison.setCreditCard(encodedCreditCard);
        }

        userRepo.save(dbComparison);
        redirectAttributes.addFlashAttribute("flashMessageSuccess", "User edited successfully.");
        return "redirect:/admin/users";
    }

    private boolean checkCreditCard(String creditCardNumber) {
        String regexCheck = "^\\d{12,16}$";
        return creditCardNumber.matches(regexCheck);
    }

}
