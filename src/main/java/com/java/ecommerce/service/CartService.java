package com.java.ecommerce.service;

import com.java.ecommerce.dto.AddToCartDto;
import com.java.ecommerce.dto.CartDto;
import com.java.ecommerce.dto.CartItemDto;
import com.java.ecommerce.exceptions.CartItemNotExistException;
import com.java.ecommerce.model.Cart;
import com.java.ecommerce.model.Product;
import com.java.ecommerce.model.User;
import com.java.ecommerce.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    CartRepository cartRepository;

    public void addToCart(Product product, int quantity, User user) {
        Cart cart = new Cart(product, quantity, user);
        cartRepository.save(cart);
    }

    public CartDto listCartItems(User user) {
        List<Cart> cartList = cartRepository.findAllByUserOrderByCreatedDateDesc(user);
        List<CartItemDto> cartItems = new ArrayList<>();

        for (Cart cart : cartList) {
            CartItemDto cartItemDto = getDtoFromCart(cart);
            cartItems.add(cartItemDto);
        }

        double totalCost = 0;
        for (CartItemDto cartItemDto : cartItems) {
            totalCost += (cartItemDto.getProduct().getPrice() * cartItemDto.getQuantity());
        }
        return new CartDto(cartItems, totalCost);
    }

    public static CartItemDto getDtoFromCart(Cart cart) {

        double totalCost = (cart.getProduct().getPrice() * cart.getQuantity());

        return new CartItemDto(cart, totalCost);
    }

    public void updateCartItem(AddToCartDto addToCartDto) {
        Optional<Cart> optionalCart = cartRepository.findById(addToCartDto.getId());

        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            cart.setQuantity(addToCartDto.getQuantity());
            cart.setCreatedDate(new Date());
            cartRepository.save(cart);
        }
    }

    public void deleteCartItem(int cartId, Long userId) throws CartItemNotExistException {
        if (!cartRepository.existsById(cartId)) {
            throw new CartItemNotExistException("Cart id is invalid : " + cartId);
        }
        cartRepository.deleteById(cartId);

    }

    public void deleteCartItems(int userId) {
        cartRepository.deleteAll();
    }


    public void deleteUserCartItems(User user) {
        cartRepository.deleteByUser(user);
    }
}
