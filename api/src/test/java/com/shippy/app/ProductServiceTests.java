package com.shippy.app;

import com.shippy.app.model.Product;
import com.shippy.app.model.ProductCategory;
import com.shippy.app.model.ShippingConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import javassist.expr.NewArray;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductServiceTests {

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void noDefaultRoute() throws Exception {
		this.mockMvc.perform(get("/"))
			.andExpect(status().is(404));
	}

	@Test
	public void validatesBadPost() throws Exception {
		Product badOne = new Product();
		this.mockMvc.perform(
			post("/products")
				.contentType("application/json")
				.content(asJsonString(badOne))
			)
			.andExpect(status().is(400));
	}

	@Test
	public void acceptsGoodPost() throws Exception {
		Product goodOne = new Product(
			"test.title",
			"test.seller",
			 ProductCategory.OUTERWEAR,
			 15.55
		);
		this.mockMvc.perform(
			post("/products")
				.contentType("application/json")
				.content(asJsonString(goodOne))
			)
			.andExpect(status().is(200));
	}

	@Test
	public void persistedProduct() throws Exception {
		this.mockMvc.perform(get("/products"))
			.andExpect(status().is(200))
			.andExpect(jsonPath("$", hasSize(greaterThan(0))));
	}

	@Test
	public void productPriceDeterminesCorrectEligibility() throws Exception {
		// set the price high
		List<ProductCategory> categories = new ArrayList<ProductCategory>();
		categories.add(ProductCategory.OUTERWEAR);
		ShippingConfiguration config = new ShippingConfiguration(200.09, categories);
		this.mockMvc.perform(
			post("/products/config/update")
				.contentType("application/json")
				.content(asJsonString(config))
		)
		.andExpect(status().is(200));

		Product tooLow = new Product(
			"test.product",
			"test.seller",
				ProductCategory.OUTERWEAR,
				9.99
		);
		this.mockMvc.perform(
			post("/products")
				.contentType("application/json")
				.content(asJsonString(tooLow))
		)
		.andExpect(status().is(200))
		.andExpect(jsonPath("$.isEligible", is(false)));

		Product highEnough = new Product(
			"test.product",
			"test.seller",
				ProductCategory.OUTERWEAR,
				99999999.99
		);
		this.mockMvc.perform(
			post("/products")
				.contentType("application/json")
				.content(asJsonString(highEnough))
		)
		.andExpect(status().is(200))
		.andExpect(jsonPath("$.isEligible", is(true)));
	}
}