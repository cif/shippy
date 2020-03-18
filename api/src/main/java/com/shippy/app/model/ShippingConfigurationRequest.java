package com.shippy.app.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ShippingConfigurationRequest {

  @NotNull
  private Double setPrice;

  @NotEmpty
  private ProductCategory[] categories;

}