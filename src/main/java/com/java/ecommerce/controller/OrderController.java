package com.java.ecommerce.controller;

import com.java.ecommerce.model.Order;
import com.java.ecommerce.model.User;
import com.java.ecommerce.security.MyUserDetails;
import com.java.ecommerce.service.CartService;
import com.java.ecommerce.service.OrderItemsService;
import com.java.ecommerce.service.OrderService;
import com.java.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class OrderController {
    @Autowired
    UserService userService;

    @Autowired
    CartService cartService;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderItemsService orderItemsService;

    @GetMapping("/createOrder")
    public String placeOrder(RedirectAttributes redirectAttributes, @AuthenticationPrincipal MyUserDetails userDetails) {

        if (userDetails != null) {

            String username = userDetails.getUsername();
            User user = userService.getUserByUsername(username);

            orderService.placeOrder(user);

            redirectAttributes.addFlashAttribute("successMessage", "Order has been placed Successfully.");
            return "redirect:/productlist";


        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Please login to view your cart.");
            return "redirect:/login";

        }
    }

    @GetMapping("/admin/orders")
    public String getAllOrders(Model model) {
        List<Order> orders = orderService.listOrders();
        model.addAttribute("orders", orders);
        return "order/orders";
    }


    @GetMapping("/admin/orders/{orderId}")
    public String getOrderById(@PathVariable("orderId") Integer orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        model.addAttribute("order", order);
        return "order/view";
    }

    @GetMapping("/orders")
    public String getOrderByUser(Model model, RedirectAttributes redirectAttributes, @AuthenticationPrincipal MyUserDetails userDetails) {

        if (userDetails != null) {
            String username = userDetails.getUsername();
            User user = userService.getUserByUsername(username);

            List<Order> orders = orderService.getOrderByUserId(user.getId());
            model.addAttribute("orders", orders);
            return "order/detail";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Please login to view your cart.");
            return "redirect:/login";

        }
    }
}
