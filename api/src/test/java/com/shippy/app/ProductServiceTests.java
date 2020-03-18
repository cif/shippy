package com.shippy.app;

import com.shippy.app.model.Product;
import com.shippy.app.model.ProductCategory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
		this.mockMvc.perform(get("/")).andExpect(status().is(404));
	}

	@Test
	public void validatesBadPost() throws Exception {
		Product badOne = new Product();
		this.mockMvc.perform(post("/products").contentType("application/json").content(asJsonString(badOne)))
				.andExpect(status().is(400));
	}

	@Test
	public void acceptsGoodPost() throws Exception {
		Product goodOne = new Product("test.title", "test.seller", ProductCategory.OUTERWEAR, 15.55);
		this.mockMvc.perform(post("/products").contentType("application/json").content(asJsonString(goodOne)))
				.andExpect(status().is(200));
	}
}