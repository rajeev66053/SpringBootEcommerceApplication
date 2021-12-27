package com.java.ecommerce.model;

import com.java.ecommerce.utils.FileUploadUtil;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.util.Date;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Product {
    @Id
    @Column(name = "product_code", unique = true, length = 20, nullable = false)
    @NotBlank(message = "Product code  is mandatory")
    private String productCode;

    @NotBlank(message = "Name is mandatory")
    @Column(nullable = false)
    @Size(min = 5, message = "{Size.Product.Name}")
    private String name;
    @Column(length = 64)
    private String image;
    @NotNull(message = "Price cannot be null")
    @Min(value = 1, message = "Price must be atleast 1")
    private double price;
    @Lob
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Created_date", nullable = false)
    private Date createdDate = new Date();


    @Transient
    public String getImagePath() throws IOException {

        String defaultImagePath="/product-image/static/product.png";

        if (image == null || productCode == null) {
            return defaultImagePath;
        };

        String imagePath="product-image/" + productCode + "/" + image;
        boolean imageExist = FileUploadUtil.checkFile(imagePath);

        if(imageExist){
            return "/"+imagePath;
        }else{
            return defaultImagePath;
        }

    }
}