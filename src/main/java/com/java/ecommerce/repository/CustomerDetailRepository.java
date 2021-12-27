package com.java.ecommerce.repository;

import com.java.ecommerce.model.CustomerDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerDetailRepository extends JpaRepository<CustomerDetail, Long> {

    @Query("FROM CustomerDetail as cd where cd.user.id=?1")
    public CustomerDetail findCustomerDetailByUserId(Long Id);
}
