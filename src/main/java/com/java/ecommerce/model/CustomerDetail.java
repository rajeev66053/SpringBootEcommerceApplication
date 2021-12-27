package com.java.ecommerce.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "customer_details")
@Data
@NoArgsConstructor
@Setter
@Getter
@ToString
public class CustomerDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "name")
    @NotEmpty(message = "Please provide your name")
    private String name;

    @Column(name = "phone")
    @NotEmpty(message = "Please provide your phone number")
    private String phone;

    @Column(name = "address", nullable = false)
    @NotEmpty(message = "Please provide your address")
    private String address;

    @Column(name = "country", nullable = false)
    @NotEmpty(message = "Please provide the name of your country")
    private String country;

    @Column(name = "city", nullable = false)
    @NotEmpty(message = "Please provide the name of your city")
    private String city;

    @Column(name = "state", nullable = false)
    @NotEmpty(message = "Please provide the name of your state")
    private String state;

    @Column(name = "zip", nullable = false)
    @NotEmpty(message = "Please provide  your zip")
    private String zip;


}
