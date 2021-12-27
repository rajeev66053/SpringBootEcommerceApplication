package com.java.ecommerce.controller;

import com.java.ecommerce.dto.CartDto;
import com.java.ecommerce.dto.checkout.CheckoutFormDto;
import com.java.ecommerce.model.CustomerDetail;
import com.java.ecommerce.model.User;
import com.java.ecommerce.security.MyUserDetails;
import com.java.ecommerce.service.*;
import com.stripe.model.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CheckOutController {

    @Value("${STRIPE_PUBLIC_KEY}")
    private String stripePublicKey;
    @Autowired
    UserService userService;
    @Autowired
    CartService cartService;
    @Autowired
    CustomerDetailService customerDetailService;
    @Autowired
    StripeService stripeService;

    @GetMapping("/checkout")
    public String checkout(Model model, RedirectAttributes redirectAttributes, @AuthenticationPrincipal MyUserDetails userDetails) {
        if (userDetails != null) {
            String username = userDetails.getUsername();
            User user = userService.getUserByUsername(username);

            CartDto cartdto = cartService.listCartItems(user);

            CustomerDetail customerDetail = customerDetailService.getCustomerDetailByUserId(user.getId());
            if (customerDetail == null) {
                customerDetail = new CustomerDetail();
            }

            CheckoutFormDto checkoutFormDto = new CheckoutFormDto();
            model.addAttribute("cartdto", cartdto);
            model.addAttribute("customerDetail", customerDetail);
            model.addAttribute("checkoutFormDto", checkoutFormDto);
            return "payment/checkout";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Please login to add the product in cart.");
            return "redirect:/login";

        }
    }

    @PostMapping("/proceed_checkout")
    public String proceedCheckout(CheckoutFormDto checkoutFormDto, RedirectAttributes redirectAttributes, @AuthenticationPrincipal MyUserDetails userDetails) throws Exception {
        if (userDetails != null) {


            String username = userDetails.getUsername();
            User user = userService.getUserByUsername(username);

            CustomerDetail customerDetail = customerDetailService.getCustomerDetailByUserId(user.getId());

            if (customerDetail != null) {
                customerDetail.setName(checkoutFormDto.getName());
                customerDetail.setAddress(checkoutFormDto.getAddress());
                customerDetail.setCountry(checkoutFormDto.getCountry());
                customerDetail.setCity(checkoutFormDto.getCity());
                customerDetail.setState(checkoutFormDto.getState());
                customerDetail.setZip(checkoutFormDto.getZip());
                customerDetail.setPhone(checkoutFormDto.getPhone());
                customerDetailService.save(customerDetail);

            } else {
                CustomerDetail customer_details = new CustomerDetail();
                customer_details.setUser(user);
                customer_details.setName(checkoutFormDto.getName());
                customer_details.setAddress(checkoutFormDto.getAddress());
                customer_details.setCountry(checkoutFormDto.getCountry());
                customer_details.setCity(checkoutFormDto.getCity());
                customer_details.setState(checkoutFormDto.getState());
                customer_details.setZip(checkoutFormDto.getZip());
                customer_details.setPhone(checkoutFormDto.getPhone());
                customerDetailService.save(customer_details);

            }


            Token token = stripeService.getCardToken(checkoutFormDto);

            CartDto cartdto = cartService.listCartItems(user);

            double total = cartdto.getTotalCost();

            stripeService.chargeNewCard(token, total);

            return "redirect:/createOrder";

        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Please login to add the product in cart.");
            return "redirect:/login";

        }

    }

}