package com.product.datajpa.repository;

import com.product.datajpa.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long>, JpaSpecificationExecutor<Product>, CustomProductRepository {
    Optional<Product> findBySku(String skucode);

    Optional<List<Product>> findByTags(String tag);

    Page<Product> findByCategory(String category, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category=:category AND p.price > :min AND p.price < :max")
    Optional<List<Product>> findByCategoryAndPriceGreaterThan(@Param("category") String category, @Param("min") double min, @Param("max") double max);

    @Query("SELECT DISTINCT p.category FROM Product p")
    List<String> findDistinctCategories();
}
