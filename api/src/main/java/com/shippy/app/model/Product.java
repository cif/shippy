package com.shippy.app.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.Builder.Default;

@Data
@Entity
@Table(name = "products")
public class Product {
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

  @Default
  private Boolean isEligible = false;

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