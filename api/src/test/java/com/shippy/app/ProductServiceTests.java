package com.shippy.app;

import com.shippy.app.model.Product;
import com.shippy.app.model.ProductCategory;
import com.shippy.app.model.ShippingConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
			"spring.test.seller",
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
		// given default price is 100
		// and categories default to full enum values
		ShippingConfiguration config = new ShippingConfiguration(
			100.00,
			new ProductCategory[] { ProductCategory.OUTERWEAR }
		);
		this.mockMvc.perform(
			put("/products/config")
				.contentType("application/json")
				.content(asJsonString(config))
		)
		.andExpect(status().is(200));

		// meet the price threshold
		Product highEnough = new Product(
			"test.product",
			"spring.test.seller",
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

		// set the price too low, expect false
		Product tooLow = new Product(
			"test.product",
			"spring.test.seller",
				ProductCategory.OUTERWEAR,
				9.99
		);
		this.mockMvc.perform(
			post("/products")
				.contentType("application/json")
				.content(asJsonString(tooLow))
		)
		.andExpect(status().is(200))
		.andExpect(jsonPath("$.isEligible", is(false)))
		.andExpect(jsonPath(
			"$.eligibilityStatus",
			is(Product.EligibilityStatus.PRICE_DOES_NOT_MEET_THRESHOLD.toString())
		));
	}

	@Test
	public void productCategoryDeterminesEligibility() throws Exception {
		// given, updated configuration to
		// only accept fly fishing or skiing gear
		ShippingConfiguration config = new ShippingConfiguration(
			100.0,
			new ProductCategory[] { ProductCategory.FLY_FISHING, ProductCategory.SKIING }
		);
		this.mockMvc.perform(
			put("/products/config")
				.contentType("application/json")
				.content(asJsonString(config))
		)
		.andExpect(status().is(200));

		// good category, above price
		Product happyCategory = new Product(
			"test.product",
			"spring.test.seller",
				ProductCategory.SKIING,
				101.10
		);
		this.mockMvc.perform(
			post("/products")
				.contentType("application/json")
				.content(asJsonString(happyCategory))
		)
		.andExpect(status().is(200))
		.andExpect(jsonPath("$.isEligible", is(true)));

		// category not in config but high enough price
		Product sadCategory = new Product(
			"test.product",
			"spring.test.seller",
			ProductCategory.SURFING,
			101.10
		);
		this.mockMvc.perform(
			post("/products")
				.contentType("application/json")
				.content(asJsonString(sadCategory))
		)
		.andExpect(status().is(200))
		.andExpect(jsonPath("$.isEligible", is(false)))
		.andExpect(jsonPath(
			"$.eligibilityStatus",
			is(Product.EligibilityStatus.PRODUCT_CATEGORY_EXCLUDED.toString())
		));
	}

	@Test
	public void notEnrolledScenario() throws Exception {
		// given, updated configuration to
		// only accept fly fishing or skiing gear
		ShippingConfiguration config = new ShippingConfiguration(
			100.0,
			new ProductCategory[] { ProductCategory.FLY_FISHING, ProductCategory.SKIING }
		);
		this.mockMvc.perform(
			put("/products/config")
				.contentType("application/json")
				.content(asJsonString(config))
		)
		.andExpect(status().is(200));

		// good category, above price
		Product happyCategory = new Product(
			"test.product",
			"spring.test.seller.unenrolled",
			ProductCategory.SKIING,
			101.10
		);
		this.mockMvc.perform(
			post("/products")
				.contentType("application/json")
				.content(asJsonString(happyCategory))
		)
		.andExpect(status().is(200))
		.andExpect(jsonPath("$.isEligible", is(false)))
		.andExpect(jsonPath(
			"$.eligibilityStatus",
			is(Product.EligibilityStatus.SELLER_NOT_ENROLLED.toString())
		));
	}

	@Test
	public void getsCategories() throws Exception {
		this.mockMvc.perform(get("/categories"))
			.andExpect(status().is(200))
			.andExpect(jsonPath("$", hasSize(greaterThan(0))));
	}

	@Test
	public void getsConfig() throws Exception {
		this.mockMvc.perform(get("/products/config"))
			.andExpect(status().is(200))
			.andExpect(jsonPath("$.categories", hasSize(greaterThan(0))))
			.andExpect(jsonPath("$.minimumPrice", is(greaterThan(0.01))));

	}
}