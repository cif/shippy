package com.shippy.app;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.shippy.app.model.Product;
import com.shippy.app.model.ProductCategory;
import com.shippy.app.model.ShippingConfiguration;

import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ProductShippingEligibility {

  private static final String SHIPPING_CONFIG_MAP_KEY = "SHIPPING_CONFIG";

  @Value("${redis.connection}")
  private String redisConnection;

  public Boolean isElisibleForFreeShiping(Product product) {
    // fetch configuration from redis
    ShippingConfiguration config = getCurrentShippingRequirementsConfig();

    // convert categories to list
    List<ProductCategory> acceptableCategories = Arrays.asList(config.getCategories());
    System.out.println("ACCEPTABLE CATEGORIES ARE");
    List<String> cats = Arrays.stream(config.getCategories()).map(cat ->
      cat.toString()
    ).collect(Collectors.toList());
    System.out.println(cats);
    return
      acceptableCategories.contains(product.getCategory()) &&
      config.getMinimumPrice() <= product.getPrice();
  }

  public ShippingConfiguration updateConfiguration (ShippingConfiguration newConfig) {
    Config conf = new Config();
    conf.useSingleServer().setAddress(redisConnection);

    RedissonClient redisson = Redisson.create(conf);
    RMap<String, ShippingConfiguration> configMap = redisson.getMap(SHIPPING_CONFIG_MAP_KEY);
    ShippingConfiguration updated = configMap.put(SHIPPING_CONFIG_MAP_KEY, newConfig);
    return updated;
  }

  private ShippingConfiguration getCurrentShippingRequirementsConfig() {
    // get the configured price from redis
    Config conf = new Config();
    conf.useSingleServer().setAddress(redisConnection);

    RedissonClient redisson = Redisson.create(conf);
    RMap<String, ShippingConfiguration> configMap = redisson.getMap(SHIPPING_CONFIG_MAP_KEY);
    ShippingConfiguration shippingConfig = configMap.get(SHIPPING_CONFIG_MAP_KEY);
    // return defaults if redis ran away
    if (null == shippingConfig) {
      shippingConfig = new ShippingConfiguration();
    }
    return shippingConfig;
  }
}