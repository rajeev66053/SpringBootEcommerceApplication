package com.java.ecommerce.repository;

import com.java.ecommerce.model.Cart;
import com.java.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    List<Cart> findAllByUserOrderByCreatedDateDesc(User user);

    //Whenever you are trying to modify a record in db, you have to mark it @Transactional as well as @Modifying, which instruct Spring that it can modify existing records.
    @Transactional
    @Modifying
    @Query("Delete FROM Cart c where c.user.id=:#{#user.id}")
    void deleteByUser(@Param("user") User user);


}
