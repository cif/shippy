package com.shippy.app.model;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShippingConfiguration {

  @NotNull
  @Default
  private Double minimumPrice = 100.00;

  @NotEmpty
  private List<ProductCategory> categories;

}