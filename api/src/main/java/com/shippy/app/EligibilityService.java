package com.shippy.app;

import java.util.Arrays;
import java.util.List;

import com.shippy.app.model.Product;
import com.shippy.app.model.ProductCategory;
import com.shippy.app.model.ShippingConfiguration;

import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EligibilityService {

  private static final String SHIPPING_CONFIG_MAP_KEY = "SHIPPING_CONFIG";

  @Value("${redis.connection}")
  private String redisConnection;

  @Autowired
  private EnrollmentDelegate enrollmentServiceDelegate;

  Logger logger = LoggerFactory.getLogger(EligibilityService.class);

  public Product determineProductEligibility(Product product) {
    logger.info("Determining eligibility for product: " + product.getTitle());

    // see if the user is enrolled in free shipping program via delegate
    if (!enrollmentServiceDelegate.isUserEnrolled(product.getSeller())) {
      logger.info("Seller is not enrolled in the enrollemnt service");
      product.setEligibilityStatus(Product.EligibilityStatus.SELLER_NOT_ENROLLED);
      product.setIsEligible(false);
      return product;
    }

    // fetch configuration from redis
    ShippingConfiguration config = getShippingConfiguration();

    // convert categories to list
    List<ProductCategory> acceptableCategories = Arrays.asList(config.getCategories());
    // System.out.println("ACCEPTABLE CATEGORIES ARE");
    // List<String> cats = Arrays.stream(config.getCategories()).map(cat ->
    //   cat.toString()
    // ).collect(Collectors.toList());
    // System.out.println(cats);
    if (!acceptableCategories.contains(product.getCategory())) {
      product.setEligibilityStatus(Product.EligibilityStatus.PRODUCT_CATEGORY_EXCLUDED);
      product.setIsEligible(false);
      return product;
    }

    if (config.getMinimumPrice() > product.getPrice()) {
      product.setEligibilityStatus(Product.EligibilityStatus.PRICE_DOES_NOT_MEET_THRESHOLD);
      product.setIsEligible(false);
      return product;
    }

    product.setIsEligible(true);
    return product;
  }

  public ShippingConfiguration updateConfiguration (ShippingConfiguration newConfig) {
    RedissonClient redisson = getRedisClient();
    RMap<String, ShippingConfiguration> configMap = redisson.getMap(SHIPPING_CONFIG_MAP_KEY);
    ShippingConfiguration updated = configMap.put(SHIPPING_CONFIG_MAP_KEY, newConfig);
    return updated;
  }

  public ShippingConfiguration getShippingConfiguration() {
    // get the configured price from redis
    RedissonClient redisson = getRedisClient();
    RMap<String, ShippingConfiguration> configMap = redisson.getMap(SHIPPING_CONFIG_MAP_KEY);
    ShippingConfiguration shippingConfig = configMap.get(SHIPPING_CONFIG_MAP_KEY);

    // just return defaults if redis ran away
    if (null == shippingConfig) {
      shippingConfig = new ShippingConfiguration();
    }
    return shippingConfig;
  }

  private RedissonClient getRedisClient() {
    Config conf = new Config();
    conf.useSingleServer().setAddress(redisConnection);
    return Redisson.create(conf);
  }
}