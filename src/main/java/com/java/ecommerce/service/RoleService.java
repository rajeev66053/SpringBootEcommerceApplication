package com.java.ecommerce.service;

import com.java.ecommerce.model.Role;
import com.java.ecommerce.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    public List<Role> listRoles() {
        return roleRepository.findAll();
    }
}
