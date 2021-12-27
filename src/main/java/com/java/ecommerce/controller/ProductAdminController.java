package com.java.ecommerce.controller;

import com.java.ecommerce.model.Product;
import com.java.ecommerce.service.ProductService;
import com.java.ecommerce.utils.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;

@Controller
@RequestMapping("/admin")
public class ProductAdminController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public String getProducts(Model model) {
        model.addAttribute("products", productService.findAllProducts());
        return "product/products";
    }

    @GetMapping("/products/create")
    public String createProduct(Product product) {
        return "product/create";
    }

    @PostMapping("/products/add")
    public String saveProduct(@RequestParam("file") MultipartFile file, @Valid Product product, BindingResult result, Model model) throws IOException {

        Product productexist = productService.findProductByCode(product.getProductCode());

        if (productexist != null) {
            result.rejectValue("productCode", "error.product", "A product already exists for this productcode.");
        }

        if (result.hasErrors()) {
            return "product/create";
        }


        String fileName = null;
        if (!file.isEmpty()) {

            fileName = StringUtils.cleanPath(file.getOriginalFilename());
            product.setImage(fileName);
        }

        product.setCreatedDate(new Date());
        Product savedProduct = productService.add(product);

        if (savedProduct != null && fileName != null) {
            String uploadDir = "product-image/" + savedProduct.getProductCode();
            FileUploadUtil.saveFile(uploadDir, fileName, file);
        }

        return "redirect:/admin/products";

    }

    @GetMapping("/products/{productcode}")
    private String getProduct(@PathVariable("productcode") String productcode, Model model) {
        model.addAttribute("product", productService.findProductByCode(productcode));
        return "product/view";
    }

    @GetMapping("/products/{productcode}/edit")
    public String editProduct(@PathVariable("productcode") String productcode, Model model) {
        Product product = productService.findProductByCode(productcode);
        model.addAttribute("product", product);
        return "product/update";
    }

    @PostMapping("/products/update/{productcode}")
    public String updateProduct(@PathVariable("productcode") String productcode, @RequestParam("file") MultipartFile file, @Valid Product product, BindingResult result, Model model) throws IOException {

        Product productexist = productService.findProductByCode(product.getProductCode());

        if (productexist != null && !productexist.getProductCode().equals(productcode)) {
            result.rejectValue("productCode", "error.product", "A product already exists for this productcode.");
        }

        if (result.hasErrors()) {
            product.setProductCode(productcode);
            return "product/update";
        }

        Product savedProduct = productService.findProductByCode(productcode);
        String savedImage = savedProduct.getImage();

        String fileName = null;
        if (!file.isEmpty()) {

            fileName = StringUtils.cleanPath(file.getOriginalFilename());
            product.setImage(fileName);
        } else {
            product.setImage(savedImage);
        }

        Product updatedProduct = productService.update(product);

        if (savedProduct != null && fileName != null) {
            String uploadDir = "product-image/" + updatedProduct.getProductCode();
            FileUploadUtil.saveFile(uploadDir, fileName, file);
        }

        if (savedImage != null && fileName != null) {
            String existingFilePath = "product-image/" + productcode + "/" + savedImage;
            FileUploadUtil.deleteFile(existingFilePath);
        }

        return "redirect:/admin/products";
    }

    @GetMapping("/products/delete/{productcode}")
    public String deleteProduct(@PathVariable("productcode") String productcode, Model model) throws IOException {
        Product product = productService.findProductByCode(productcode);
        String fileName = product.getImage();
        if (product != null) {
            productService.deleteById(productcode);

            String uploadedDirectory = "product-image/" + productcode;
            String existingFilePath = "product-image/" + productcode + "/" + fileName;

            FileUploadUtil.deleteFile(existingFilePath);
            FileUploadUtil.deleteFile(uploadedDirectory);

        }
        return "redirect:/admin/products";
    }
}
