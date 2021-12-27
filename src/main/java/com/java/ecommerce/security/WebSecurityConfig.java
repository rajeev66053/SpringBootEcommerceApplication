package com.java.ecommerce.security;

import com.java.ecommerce.controller.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.dao.*;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin/**").hasAnyAuthority("CREATOR", "EDITOR", "ADMIN")
                .antMatchers("/admin/products").hasAnyAuthority("CREATOR", "EDITOR", "ADMIN")
                .antMatchers("/admin/products/create").hasAnyAuthority("ADMIN", "CREATOR")
                .antMatchers("/admin/products/edit/**").hasAnyAuthority("ADMIN", "EDITOR")
                .antMatchers("/admin/products/delete/**").hasAnyAuthority("ADMIN")
                .antMatchers("/customer_details/**", "/orders/**").hasAuthority("USER")
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/register/**", "/forgot_password/**", "/reset/**").permitAll()
                .antMatchers("/", "/productlist/**", "/product/**", "/cart/**").permitAll()
                .antMatchers("/", "/resources/**", "/static/**", "/css/**", "/product-image/**").permitAll()
                .antMatchers("/cart/mycart").hasAnyAuthority("USER")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(loginSuccessHandler)
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/403");

        http.csrf().disable();
        http.headers().frameOptions().disable();
    }
}
