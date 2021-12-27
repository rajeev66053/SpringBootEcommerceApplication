package com.java.ecommerce.repository;

import com.java.ecommerce.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("SELECT R FROM Role R WHERE R.name = :name")
    public Role findByName(@Param("name") String name);
}
