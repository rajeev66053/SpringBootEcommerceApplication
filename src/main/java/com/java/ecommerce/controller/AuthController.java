package com.java.ecommerce.controller;

import com.java.ecommerce.model.ConfirmationToken;
import com.java.ecommerce.model.User;
import com.java.ecommerce.service.ConfirmationTokenService;
import com.java.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @GetMapping("/login")
    public String showLoginPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "/login";
        }
        return "redirect:/";
    }

    @GetMapping("/register")
    public String signUp(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register")
    public String signUp(@Valid User user, BindingResult result, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        User userexist = userService.getUserByUsername(user.getUsername());

        if (userexist != null) {
            result.rejectValue("username", "error.user", "An account already exists for this username.");
        }


        Optional<User> userwithemail = userService.findUserByEmail(user.getEmail());
        if (userwithemail.isPresent()) {
            result.rejectValue("email", "error.user", "An account already exists for this email.");
        }

        if (result.hasErrors()) {
            return "/register";
        }

        userService.signUpUser(user, request);

        redirectAttributes.addFlashAttribute("successMessage", "Confirmation Email has been sent to the email.");

        return "redirect:/login";
    }

    @GetMapping("/register/confirm")
    String confirmMail(@RequestParam("token") String token, RedirectAttributes redirectAttributes) {

        Optional<ConfirmationToken> optionalConfirmationToken = confirmationTokenService.findConfirmationTokenByToken(token);

        optionalConfirmationToken.ifPresent(userService::confirmUser);

        redirectAttributes.addFlashAttribute("successMessage", "You have been successfully registered.");

        return "redirect:/login";
    }


}
