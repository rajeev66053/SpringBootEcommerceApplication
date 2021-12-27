package com.java.ecommerce.controller;

import com.java.ecommerce.model.Product;
import com.java.ecommerce.model.Role;
import com.java.ecommerce.model.User;
import com.java.ecommerce.repository.RoleRepository;
import com.java.ecommerce.service.RoleService;
import com.java.ecommerce.service.UserService;
import com.java.ecommerce.utils.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    RoleRepository roleRepository;

    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "user/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model) {
        Optional<User> optionalUser = userService.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Role> listRoles = roleService.listRoles();
            model.addAttribute("user", user);
            model.addAttribute("listRoles", listRoles);
        }
        return "user/update";
    }

    @PostMapping("/users/update/{id}")
    public String updateUser(@PathVariable("id") Long id, @Valid User user, BindingResult result, Model model) throws IOException {
        if (result.hasErrors()) {
            user.setId(id);
            return "user/update";
        }

        Optional<User> optionalUser = userService.findById(id);

        if (optionalUser.isPresent()) {
            userService.save(user);

        }

        return "redirect:/admin/users";
    }

}
