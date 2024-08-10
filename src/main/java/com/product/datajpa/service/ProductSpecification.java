package com.product.datajpa.service;

import com.product.datajpa.entity.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ProductSpecification {

//    Specification: Explain that in JPA, a Specification is a way to define a query using a programmatic and type-safe approach.
//    It allows you to build complex queries in a flexible and reusable manner by combining various criteria.
//    JpaSpecificationExecutor: Mention that JpaSpecificationExecutor is an interface provided by Spring Data JPA
//    that allows you to execute Specifications. It provides methods like findAll(Specification<T> spec) to query entities
//    based on the conditions defined in the Specification
    public static Specification<Product> getProductByDynamicQuery(String title, String category, Double min, Double max, Double rating){

        return (Root<Product> root , CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->{
            List<Predicate> predicates = new ArrayList<>();

            if (title != null && !title.isEmpty()){
                Predicate titlePredicate = criteriaBuilder.like(root.get("title"),"%"+title+"%");
                predicates.add(titlePredicate);
            }
            if (category != null && !category.isEmpty()){
                Predicate categoryPredicate = criteriaBuilder.equal(root.get("category"),category);
                predicates.add(categoryPredicate);
            }
            if (min != null){
                Predicate minPricePredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("price"),min);
                predicates.add(minPricePredicate);
            }
            if (max != null){
                Predicate maxPricePredicate = criteriaBuilder.lessThanOrEqualTo(root.get("price"),max);
                predicates.add(maxPricePredicate);
            }
            if (rating != null){
                Predicate ratingPredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("rating"),rating);
                predicates.add(ratingPredicate);
            }
            // Combine predicates into a single predicate and return
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
