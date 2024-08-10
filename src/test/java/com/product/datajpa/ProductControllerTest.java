package com.product.datajpa;

import com.product.datajpa.controller.ProductController;
import com.product.datajpa.entity.Product;
import com.product.datajpa.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@WebMvcTest(ProductController.class)
@Import(SecurityTestConfig.class)  // Import the security configuration for tests
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void searchProducts_shouldReturnListOfProducts() throws Exception {

        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "Title1", "Description1", "Category1", 10.0, 5.0, 4.0, 100, null, "Brand1", "SKU1", 1.0, null, "Warranty1", "Shipping1", "Status1", null, "ReturnPolicy1", 1, null, null, "Thumbnail1"));
        products.add(new Product(2, "Title2", "Description2", "Category2", 20.0, 10.0, 3.5, 200, null, "Brand2", "SKU2", 2.0, null, "Warranty2", "Shipping2", "Status2", null, "ReturnPolicy2", 2, null, null, "Thumbnail2"));

        // Mock the service method
        when(productService.searchProductsByDynamicQuery(
                Optional.of("Title1"),
                Optional.of("Category1"),
                Optional.of(10.0),
                Optional.of(20.0),
                Optional.of(4.0)
        )).thenReturn(products);

        // Perform GET request and verify response
        mockMvc.perform(MockMvcRequestBuilders.get("/api/product/search")
                        .param("title", "Title1")
                        .param("category", "Category1")
                        .param("min", "10.0")
                        .param("max", "20.0")
                        .param("rating", "4.0"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Title1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("Title2"));
    }
}
