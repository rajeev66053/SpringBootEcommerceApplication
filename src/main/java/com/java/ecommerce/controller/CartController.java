package com.java.ecommerce.controller;

import com.java.ecommerce.dto.AddToCartDto;
import com.java.ecommerce.dto.CartDto;
import com.java.ecommerce.model.Product;
import com.java.ecommerce.model.User;
import com.java.ecommerce.security.MyUserDetails;
import com.java.ecommerce.service.CartService;
import com.java.ecommerce.service.ProductService;
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

import java.io.IOException;

@Controller
public class CartController {

    @Autowired
    private UserService userService;
    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @PostMapping("/cart/add/{productcode}")
    public String add(@PathVariable("productcode") String productcode, AddToCartDto addToCartDto, BindingResult result, Model model, RedirectAttributes redirectAttributes, @AuthenticationPrincipal MyUserDetails userDetails) throws IOException {
        if (userDetails != null) {
            Product product = productService.findProductByCode(productcode);

            if (product != null) {
                String username = userDetails.getUsername();
                User user = userService.getUserByUsername(username);

                if (addToCartDto.getQuantity() != null) {

                    int quantity = addToCartDto.getQuantity();

                    if (quantity < 1) {
                        model.addAttribute("addToCartDto", addToCartDto);
                        redirectAttributes.addFlashAttribute("errorMessage", "A product quantity cannot be less than 1.");
                        return "redirect:/product/" + productcode;
                    }
                    cartService.addToCart(product, addToCartDto.getQuantity(), user);
                    redirectAttributes.addFlashAttribute("successMessage", "Product has been added to cart.");
                    return "redirect:/productlist";

                } else {
                    redirectAttributes.addFlashAttribute("errorMessage", "A product quantity cannot be null.");
                    return "redirect:/product/" + productcode;
                }

            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Product doesnot exist.");
                return "redirect:/productlist";
            }

        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Please login to add the product in cart.");
            return "redirect:/login";

        }
    }

    @GetMapping("/cart/mycart")
    public String getCartItems(Model model, RedirectAttributes redirectAttributes, @AuthenticationPrincipal MyUserDetails userDetails) {

        if (userDetails != null) {

            String username = userDetails.getUsername();
            User user = userService.getUserByUsername(username);
            CartDto cartdto = cartService.listCartItems(user);

            AddToCartDto addToCartDto = new AddToCartDto();

            model.addAttribute("cartdto", cartdto);
            model.addAttribute("addToCartDto", addToCartDto);
            return "cart/cartlist";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Please login to view your cart.");
            return "redirect:/login";

        }
    }

    @PostMapping("/cart/update/{cartItemId}")
    public String updateCartItem(@PathVariable("cartItemId") int itemId, AddToCartDto addToCartDto, Model model, RedirectAttributes redirectAttributes, @AuthenticationPrincipal MyUserDetails userDetails) {

        if (userDetails != null) {

            String productcode = addToCartDto.getProductCode();

            Product product = productService.findProductByCode(productcode);

            if (product != null) {
                String username = userDetails.getUsername();
                User user = userService.getUserByUsername(username);


                if (addToCartDto.getQuantity() != null) {

                    int quantity = addToCartDto.getQuantity();

                    if (quantity < 1) {
                        model.addAttribute("addToCartDto", addToCartDto);
                        redirectAttributes.addFlashAttribute("errorMessage", "A product quantity cannot be less than 1.");
                        return "redirect:/cart/mycart";

                    }

                    cartService.updateCartItem(addToCartDto);
                    redirectAttributes.addFlashAttribute("successMessage", "Product has been updated in the cart.");
                    return "redirect:/cart/mycart";

                } else {
                    redirectAttributes.addFlashAttribute("errorMessage", "A product quantity cannot be null.");
                    return "redirect:/product/" + productcode;
                }
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Product doesnot exist.");
                return "redirect:/cart/mycart";
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Please login to view your cart.");
            return "redirect:/login";

        }
    }

    @GetMapping("/cart/delete/{cartItemId}")
    public String deleteCartItem(@PathVariable("cartItemId") int itemId, RedirectAttributes redirectAttributes, @AuthenticationPrincipal MyUserDetails userDetails) {
        if (userDetails != null) {
            String username = userDetails.getUsername();
            User user = userService.getUserByUsername(username);

            cartService.deleteCartItem(itemId, user.getId());
            redirectAttributes.addFlashAttribute("successMessage", "Product has been deleted from the cart.");
            return "redirect:/cart/mycart";

        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Please login to view your cart.");
            return "redirect:/login";
        }
    }
}
