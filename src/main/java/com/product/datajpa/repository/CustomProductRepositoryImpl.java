package com.product.datajpa.repository;

import com.product.datajpa.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public class CustomProductRepositoryImpl implements CustomProductRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Product> findProductsByCriteria(String title, String category, Double min, Double max, Double rating, Pageable pageable) {

        // Using criteria API for custom query
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // predicates
        List<Predicate> validPredicates = new ArrayList<>();
        if (title != null && !title.isEmpty()) {
            Predicate titlePredicate = cb.like(cb.lower(cb.literal(title)), "%" + title.toLowerCase() + "%");
            validPredicates.add(titlePredicate);
        }
        if (category != null && !category.isEmpty()) {
            Predicate categoryPredicate = cb.equal(cb.lower(cb.literal(category)), category.toLowerCase());
            validPredicates.add(categoryPredicate);
        }
        if (min != null) {
            Predicate minPredicate = cb.greaterThanOrEqualTo(cb.literal(min), min);
            validPredicates.add(minPredicate);
        }
        if (max != null) {
            Predicate maxPredicate = cb.lessThanOrEqualTo(cb.literal(max), max);
            validPredicates.add(maxPredicate);
        }
        if (rating != null) {
            Predicate ratingPredicate = cb.greaterThanOrEqualTo(cb.literal(rating), rating);
            validPredicates.add(ratingPredicate);
        }

        // count query
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        //Root represents the root entity in the query, equivalent to the FROM clause in SQL.
        Root<Product> countRoot = countQuery.from(Product.class);
        countQuery.select(cb.count(countRoot)).where(validPredicates.toArray(new Predicate[0]));
        Long total = entityManager.createQuery(countQuery).getSingleResult();

        // pagination query
        CriteriaQuery<Product> criteriaQuery = cb.createQuery(Product.class);
        Root<Product> productRoot = criteriaQuery.from(Product.class);
        criteriaQuery.select(productRoot).where(validPredicates.toArray(new Predicate[0]));

        TypedQuery<Product> query = entityManager.createQuery(criteriaQuery);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Product> products = query.getResultList();

        return new PageImpl<>(products, pageable, total);

    }
}
