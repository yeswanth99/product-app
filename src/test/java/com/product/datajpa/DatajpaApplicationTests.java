package com.product.datajpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@ActiveProfiles("local")
@Testcontainers
@AutoConfigureMockMvc
class DatajpaApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Container
	static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
			.withDatabaseName("testdb")
			.withUsername("testuser")
			.withPassword("testpass")
			.withInitScript("schema-test.sql");

	@DynamicPropertySource
	static void registerMySQLProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", mysql::getJdbcUrl);
		registry.add("spring.datasource.username", mysql::getUsername);
		registry.add("spring.datasource.password", mysql::getPassword);
	}

	@Test
	void contextLoads() {
	}

	@Test
	void searchProducts_shouldReturnListOfProducts() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/product/search")
				.param("category","beauty"))
				.andDo(print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Essence Mascara Lash Princess"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].category").value("beauty"));
	}

	@Test
	void getProducts_shouldReturnListOfProducts() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/product/retrieveProducts"))
				.andDo(print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Essence Mascara Lash Princess"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].category").value("beauty"));
	}
}
