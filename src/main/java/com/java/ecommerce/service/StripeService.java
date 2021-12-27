package com.java.ecommerce.service;

import com.java.ecommerce.dto.checkout.CheckoutFormDto;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService {
    @Value("${STRIPE_SECRET_KEY}")
    private String API_SECRET_KEY;

    public Token getCardToken(CheckoutFormDto checkoutFormDto) throws StripeException {
        Stripe.apiKey = API_SECRET_KEY;

        Map<String, Object> card = new HashMap<>();
        card.put("number", checkoutFormDto.getCardNumber());
        card.put("exp_month", Integer.parseInt(checkoutFormDto.getExpirationMonth()));
        card.put("exp_year", Integer.parseInt(checkoutFormDto.getExpirationYear()));
        card.put("cvc", checkoutFormDto.getCvv());
        Map<String, Object> params = new HashMap<>();
        params.put("card", card);

        return Token.create(params);

    }

    public Charge chargeNewCard(Token token, double amount) throws Exception {

        Stripe.apiKey = API_SECRET_KEY;

        Map<String, Object> chargeParams = new HashMap<String, Object>();
        chargeParams.put("amount", (int) (amount * 100));
        chargeParams.put("currency", "USD");
        chargeParams.put("description", "Payment has been done.");
        chargeParams.put("source", token.getId());
        return Charge.create(chargeParams);

    }

}
