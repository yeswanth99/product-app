package com.product.datajpa.repository;

import com.product.datajpa.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomProductRepository {
    Page<Product> findProductsByCriteria(String title, String category, Double min, Double max, Double rating, Pageable pageable);
}

