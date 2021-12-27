package com.java.ecommerce.repository;

import com.java.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {


    @Query("SELECT p FROM Product p WHERE p.productCode = :productcode")
    Product findProductByCode(@Param("productcode") String productcode);
}
