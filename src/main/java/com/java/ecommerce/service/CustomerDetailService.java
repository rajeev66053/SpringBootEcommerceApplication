package com.java.ecommerce.service;

import com.java.ecommerce.model.CustomerDetail;
import com.java.ecommerce.repository.CustomerDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerDetailService {
    @Autowired
    CustomerDetailRepository customerDetailRepository;

    public CustomerDetail getCustomerDetailByUserId(Long id) {
        return customerDetailRepository.findCustomerDetailByUserId(id);
    }

    public void save(CustomerDetail customer_detail) {

        customerDetailRepository.save(customer_detail);

    }
}
