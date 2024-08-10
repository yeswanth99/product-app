package com.product.datajpa.service;

import com.product.datajpa.config.RestTemplateConfig;
import com.product.datajpa.model.Product.ProductResponse;
import com.product.datajpa.model.Product.Products;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DataUpdateService {

    @Value("${products.url}")
    private String url;

    @Autowired
    private RestTemplateConfig restTemplateConfig;

    @Autowired
    private ProductService productService;

    @PostConstruct
    public void init(){
        Products products= restTemplateConfig.getRestTemplate()
                .getForObject(url, Products.class);
        List<ProductResponse> productResponses = products.getProducts();
        //productService.updateData(productResponses);
    }

}
