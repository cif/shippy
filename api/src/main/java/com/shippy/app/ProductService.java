package com.shippy.app;

import com.shippy.app.model.Product;
import com.shippy.app.model.ShippingConfigurationRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.validation.Valid;
import java.util.List;

@RestController
public class ProductService {

  @Autowired
  ProductRepository productRepository;

  ProductShippingEligibility eligibilityServcie;

  Logger logger = LoggerFactory.getLogger(ProductService.class);

  @GetMapping("/products")
  public List<Product> getAllProducts() {
    return productRepository.findAll();
  }

  @PostMapping("/products")
  public Product validateProduct(@Valid @RequestBody Product product) {
    // use the eligibility servcie to to determine if
    // the product is eligible for free shipping
    product.setIsEligible(eligibilityServcie.isElisibleForFreeShiping(product));
    return productRepository.save(product);
  }

  @PostMapping("/products/config/update")
  public ShippingConfigurationRequest updateMinimumPrice(@Valid @RequestBody ShippingConfigurationRequest req) {

    return req;
  }
}