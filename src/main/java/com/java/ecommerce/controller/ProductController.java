package com.java.ecommerce.controller;

import com.java.ecommerce.dto.AddToCartDto;
import com.java.ecommerce.model.Product;
import com.java.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping({"/productlist"})
    public String listProduct(Model model) {

        List<Product> products = productService.findAllProducts();

        model.addAttribute("products", products);
        return "product/productlist";
    }

    @GetMapping({"/product/{productcode}"})
    public String viewProduct(@PathVariable("productcode") String productcode, Model model, RedirectAttributes redirectAttributes) {
        Product product = productService.findProductByCode(productcode);

        if (product != null) {
            AddToCartDto addToCartDto = new AddToCartDto();
            model.addAttribute("product", product);
            model.addAttribute("addToCartDto", addToCartDto);
            return "product/detail";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Product doesnot exist.");
            return "redirect:/productlist";
        }


    }

}
