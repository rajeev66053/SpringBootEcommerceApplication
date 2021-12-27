package com.java.ecommerce.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Getter
@Setter
public class AddToCartDto {

    private Integer id;
    @NotNull
    private String productCode;
    @NotNull
    private Integer quantity;
}
