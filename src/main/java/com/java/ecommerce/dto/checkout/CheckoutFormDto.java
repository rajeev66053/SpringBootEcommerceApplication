package com.java.ecommerce.dto.checkout;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Future;

@Data
@Getter
@Setter
@ToString
public class CheckoutFormDto {

    private String name;
    private String phone;
    private String address;
    private String country;
    private String city;
    private String state;
    private String zip;


    private String cardName;
    private String cardNumber;
    private String expirationMonth;
    private String expirationYear;
    private String cvv;

}
