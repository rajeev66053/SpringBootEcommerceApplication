package com.java.ecommerce.controller;

import com.java.ecommerce.model.CustomerDetail;
import com.java.ecommerce.model.User;
import com.java.ecommerce.security.MyUserDetails;
import com.java.ecommerce.service.CustomerDetailService;
import com.java.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CustomerDetailController {

    @Autowired
    UserService userService;

    @Autowired
    CustomerDetailService customerDetailService;

    @GetMapping("/customer_details/create")
    public String createCustomerDetail(Model model, RedirectAttributes redirectAttributes, @AuthenticationPrincipal MyUserDetails userDetails) {

        if (userDetails != null) {
            CustomerDetail customer_details = new CustomerDetail();
            model.addAttribute("customer_details", customer_details);

            return "customer_details/create";

        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Please login to add the product in cart.");
            return "redirect:/login";

        }
    }

    @PostMapping("/customer_details/add")
    public String addCustomerDetail(CustomerDetail customer_details, BindingResult result, RedirectAttributes redirectAttributes, @AuthenticationPrincipal MyUserDetails userDetails) {
        if (result.hasErrors()) {
            return "customer_details/create";
        }
        if (userDetails != null) {
            String username = userDetails.getUsername();
            User user = userService.getUserByUsername(username);
            customer_details.setUser(user);
            customerDetailService.save(customer_details);

            redirectAttributes.addFlashAttribute("successMessage", "Customer Detail has been added.");
            return "redirect:/profile";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Please login to add the product in cart.");
            return "redirect:/login";

        }

    }

    @GetMapping("/customer_details/edit/{customerDetailId}")
    public String editCustomerDetail(@PathVariable("customerDetailId") Long id, Model model, RedirectAttributes redirectAttributes, @AuthenticationPrincipal MyUserDetails userDetails) {

        if (userDetails != null) {
            String username = userDetails.getUsername();
            User user = userService.getUserByUsername(username);
            CustomerDetail customerDetail = customerDetailService.getCustomerDetailByUserId(user.getId());

            model.addAttribute("customer_details", customerDetail);
            return "customer_details/update";

        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Please login to add the product in cart.");
            return "redirect:/login";

        }
    }

    @PostMapping("/customer_details/update/{customerDetailId}")
    public String updateCustomerDetail(@PathVariable("customerDetailId") Long id, CustomerDetail customer_details, BindingResult result, RedirectAttributes redirectAttributes, @AuthenticationPrincipal MyUserDetails userDetails) {

        if (result.hasErrors()) {
            customer_details.setId(id);
            return "customer_details/update";
        }
        if (userDetails != null) {
            String username = userDetails.getUsername();
            User user = userService.getUserByUsername(username);
            CustomerDetail customerDetail = customerDetailService.getCustomerDetailByUserId(user.getId());

            customer_details.setId(customerDetail.getId());
            customer_details.setUser(user);
            customerDetailService.save(customer_details);

            redirectAttributes.addFlashAttribute("successMessage", "Customer Detail has been updated.");
            return "redirect:/profile";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Please login to add the product in cart.");
            return "redirect:/login";

        }

    }

    @GetMapping("/profile")
    public String profile(Model model, RedirectAttributes redirectAttributes, @AuthenticationPrincipal MyUserDetails userDetails) {
        if (userDetails != null) {
            String username = userDetails.getUsername();
            User user = userService.getUserByUsername(username);
            CustomerDetail customer_details = customerDetailService.getCustomerDetailByUserId(user.getId());
            model.addAttribute("user", user);
            model.addAttribute("customer_details", customer_details);
            return "user/profile";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Please login to add the product in cart.");
            return "redirect:/login";

        }

    }
}
