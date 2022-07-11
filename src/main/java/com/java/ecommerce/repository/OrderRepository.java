package com.java.ecommerce.repository;

import com.java.ecommerce.model.Order;
import com.java.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    
    //Named Query
    @Query("From Order as ord where  ord.user.id=:userId")
    List<Order> findByUserId(Long userId);

//    List<Order> findAllByUserOrderByCreatedDateDesc(User user);
}
