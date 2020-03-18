package com.shippy.app;

// import com.shippy.app.exception.ProductNotFoundException;
import com.shippy.app.model.Product;
import com.shippy.app.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
  private RedisTemplate<String, String> redis;
  Logger logger = LoggerFactory.getLogger(ProductService.class);

  @GetMapping("/products")
  public List<Product> getAllProducts() {
    return productRepository.findAll();
  }

  @PostMapping("/products")
  public Product validateProduct(@Valid @RequestBody Product product) {
    if (!redis.hasKey("test")) {
      product.setIsEligible(true);
      redis.opsForList().leftPush("test", "cool");
    }
    return productRepository.save(product);
  }
}