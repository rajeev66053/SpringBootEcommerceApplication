package com.java.ecommerce.dto;

import com.java.ecommerce.model.Cart;
import com.java.ecommerce.model.Product;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Getter
@Setter
public class CartItemDto {
    private Integer id;
    private Integer quantity;
    private Product product;
    private double totalCost;

    public CartItemDto(Cart cart, double totalCost) {
        this.setId(cart.getId());
        this.setQuantity(cart.getQuantity());
        this.setProduct(cart.getProduct());
        this.setTotalCost(totalCost);
    }
}
