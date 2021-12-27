package com.java.ecommerce.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    // Inject via application.properties
    @Value("${welcome.message}")
    private String message;


    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("message", message);
        return "index";
    }
}
