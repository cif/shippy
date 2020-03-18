package com.shippy.app.controller;

// import com.shippy.app.exception.ProductNotFoundException;
import com.shippy.app.model.Product;
import com.shippy.app.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.validation.Valid;
import java.util.List;

@RestController
public class ProductService {

  @Autowired
  ProductRepository productRepository;

  Logger logger = LoggerFactory.getLogger(ProductService.class);

  @GetMapping("/products")
  public List<Product> getAllProducts() {
    return productRepository.findAll();
  }

  @PostMapping("/products")
  public Product validateProduct(@Valid @RequestBody Product product) {
    return productRepository.save(product);
  }
}