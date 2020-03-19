package com.shippy.app;

import com.shippy.app.model.Product;
import com.shippy.app.model.ProductCategory;
import com.shippy.app.model.ShippingConfiguration;

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

  @Autowired
  EligibilityService eligibilityService;

  Logger logger = LoggerFactory.getLogger(ProductService.class);

  @GetMapping("/products")
  public List<Product> getAllProducts() {
    return productRepository.findAll();
  }


  @PostMapping("/products")
  public Product validateProduct(@Valid @RequestBody Product product) {
    // use the eligibility servcie to to determine if
    // the product is eligible for free shipping and persist
    product.setIsEligible(eligibilityService.isElisibleForFreeShiping(product));
    return productRepository.save(product);
  }

  @GetMapping("/products/config")
  public ShippingConfiguration getShippingConfiguration() {
    return eligibilityService.getShippingConfiguration();
  }

  @PutMapping("/products/config")
  public ShippingConfiguration updateShippingConfiguration(@Valid @RequestBody ShippingConfiguration req) {
    return eligibilityService.updateConfiguration(req);
  }

  @GetMapping("/categories")
  public ProductCategory[] getCategories() {
    return ProductCategory.values();
  }
}