package com.java.ecommerce.runner;

import com.java.ecommerce.model.Product;
import com.java.ecommerce.model.Role;
import com.java.ecommerce.model.User;
import com.java.ecommerce.repository.ProductRepository;
import com.java.ecommerce.repository.RoleRepository;
import com.java.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class StartUpRunner implements CommandLineRunner {


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {

        List<Role> roles = new ArrayList<>();
        Role role1 = new Role();
        role1.setName("USER");
        roles.add(role1);
        Role role2 = new Role();
        role2.setName("CREATOR");
        roles.add(role2);
        Role role3 = new Role();
        role3.setName("EDITOR");
        roles.add(role3);
        Role role4 = new Role();
        role4.setName("ADMIN");
        roles.add(role4);

        roleRepository.saveAll(roles);

        User admin = new User();
        admin.setId(1L);
        admin.setFirstname("admin");
        admin.setLastname("admin");
        admin.setEmail("admin@gmail.com");
        admin.setUsername("admin");
        final String encryptedPassword = bCryptPasswordEncoder.encode("admin");
        admin.setPassword(encryptedPassword);
        admin.setEnabled(true);

        Set<Role> roleSet = new HashSet<>();
        Role admin_role = roleRepository.findByName("ADMIN");
        roleSet.add(admin_role);
        admin.setRoles(roleSet);

        userRepository.save(admin);


        User user = new User();
        user.setId(2L);
        user.setFirstname("Rajeev");
        user.setLastname("Thapaliya");
        user.setEmail("rthapaliya@griddynamics.com");
        user.setUsername("rajeev");
        final String encryptedPassword1 = bCryptPasswordEncoder.encode("rajeev");
        user.setPassword(encryptedPassword1);
        user.setEnabled(true);

        Set<Role> roleSet1 = new HashSet<>();
        Role user_role = roleRepository.findByName("USER");
        roleSet1.add(user_role);
        user.setRoles(roleSet1);

        userRepository.save(user);


        List<Product> products = new ArrayList<>();
        products.add(new Product("P001", "product1", null, 10.20, "product1Description", new Date()));
        products.add(new Product("P002", "product2", null, 20.20, "product2Description", new Date()));
        products.add(new Product("P003", "product3", null, 50.20, "product3Description", new Date()));
        products.add(new Product("P004", "product4", null, 60.20, "product4Description", new Date()));
        productRepository.saveAll(products);

    }
}
