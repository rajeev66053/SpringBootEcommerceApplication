package com.java.ecommerce.service;

import com.java.ecommerce.model.Product;
import com.java.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public Product findProductByCode(String productcode) {
        Product product = productRepository.findProductByCode(productcode);
        return product;
    }

    public Product add(Product product) {

        return productRepository.save(product);
    }

    public Product update(Product product) {

        return productRepository.save(product);
    }

    public void deleteById(String productcode) {

        productRepository.deleteById(productcode);
    }
}
