package com.java.ecommerce.service;

import com.java.ecommerce.model.ConfirmationToken;
import com.java.ecommerce.model.Role;
import com.java.ecommerce.model.User;
import com.java.ecommerce.repository.RoleRepository;
import com.java.ecommerce.repository.UserRepository;
import com.java.ecommerce.utils.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @Autowired
    private EmailService emailService;

    @Autowired
    RoleRepository roleRepository;

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User getUserByUsername(String username) {

        return userRepository.getUserByUsername(username);

    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public Optional<User> findUserByResetToken(String resetToken) {
        return userRepository.findUserByResetToken(resetToken);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void signUpUser(User user, HttpServletRequest request) {

        final String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());

        user.setPassword(encryptedPassword);

        User createdUser = userRepository.save(user);

        Set<Role> roleSet = new HashSet<>();

        Role role = roleRepository.findByName("USER");
        roleSet.add(role);
        createdUser.setRoles(roleSet);

        final ConfirmationToken confirmationToken = new ConfirmationToken(user);

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        sendConfirmationMail(user.getEmail(), confirmationToken.getConfirmationToken(), request);

    }

    public void sendConfirmationMail(String userMail, String token, HttpServletRequest request) {

        String appUrl = Utility.getSiteURL(request);
        String link = appUrl + "/register/confirm?token=" + token;
        String subject = "Mail Confirmation Link!";
        String message = "Thank you for registering. Please click on the below link to activate your account." + link;

        emailService.sendEmail(userMail, subject, message);
    }

    public void confirmUser(ConfirmationToken confirmationToken) {

        final User user = confirmationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);

        confirmationTokenService.deleteConfirmationToken(confirmationToken.getId());

    }
}