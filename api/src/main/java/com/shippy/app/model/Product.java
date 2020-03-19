package com.shippy.app.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
@Table(name = "products")
public class Product {

  public enum EligibilityStatus {
    SELLER_NOT_ENROLLED,
    PRICE_DOES_NOT_MEET_THRESHOLD,
    PRODUCT_CATEGORY_EXCLUDED
  }

  @Id
  @GeneratedValue
  private Long id;

  @NotBlank
  private String title;

  @NotBlank
  private String seller;

  @NotNull
  private ProductCategory category;

  @NotNull
  private Double price;

  private Boolean isEligible = false;

  public EligibilityStatus eligibilityStatus;

  public Product() {
    super();
  }

  public Product(String title, String seller, ProductCategory category, Double price) {
    this.title = title;
    this.seller = seller;
    this.category = category;
    this.price = price;
  }
}