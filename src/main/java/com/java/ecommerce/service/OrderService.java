package com.java.ecommerce.service;

import com.java.ecommerce.dto.CartDto;
import com.java.ecommerce.dto.CartItemDto;
import com.java.ecommerce.exceptions.OrderNotFoundException;
import com.java.ecommerce.model.Order;
import com.java.ecommerce.model.OrderItem;
import com.java.ecommerce.model.User;
import com.java.ecommerce.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    CartService cartService;

    @Autowired
    OrderItemsService orderItemsService;

    @Autowired
    OrderRepository orderRepository;

    public Order getOrderById(Integer orderId) {

        Optional<Order> order = orderRepository.findById(orderId);

        if (!order.isPresent()) {
            return null;
        }
        return order.get();
    }

    public List<Order> getOrderByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public void placeOrder(User user) {

        // first let get cart items for the user
        CartDto cartDto = cartService.listCartItems(user);

        List<CartItemDto> cartItemDtoList = cartDto.getCartItems();

        // create the order and save it
        Order newOrder = new Order();
        newOrder.setCreatedDate(new Date());
        newOrder.setUser(user);
        newOrder.setTotalPrice(cartDto.getTotalCost());
        Order savedOrder = orderRepository.save(newOrder);

        for (CartItemDto cartItemDto : cartItemDtoList) {
            // create orderItem and save each one
            OrderItem orderItem = new OrderItem();
            orderItem.setCreatedDate(new Date());
            orderItem.setPrice(cartItemDto.getProduct().getPrice());
            orderItem.setProduct(cartItemDto.getProduct());
            orderItem.setQuantity(cartItemDto.getQuantity());
            orderItem.setOrder(newOrder);
            // add to order item list
            orderItemsService.save(orderItem);
        }
        cartService.deleteUserCartItems(user);

    }

    public List<Order> listOrders() {
        return orderRepository.findAll();
    }
}
