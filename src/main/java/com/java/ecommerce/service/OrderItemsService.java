package com.java.ecommerce.service;

import com.java.ecommerce.model.OrderItem;
import com.java.ecommerce.repository.OrderItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderItemsService {

    @Autowired
    OrderItemsRepository orderItemsRepository;

    public void save(OrderItem orderItem) {
        orderItemsRepository.save(orderItem);
    }
}
