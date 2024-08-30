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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CustomProductRepositoryImpl implements CustomProductRepository {

    private static final Logger logger = LoggerFactory.getLogger(CustomProductRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Product> findProductsByCriteria(String title, String category, Double min, Double max, Double rating, Pageable pageable) {
        logger.info("Starting custom product query with filters - title: {}, category: {}, min: {}, max: {}, rating: {}",
                title, category, min, max, rating);

        // Using Criteria API for custom query
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // Define the root of the query (i.e., the Product entity)
        CriteriaQuery<Product> criteriaQuery = cb.createQuery(Product.class);
        Root<Product> productRoot = criteriaQuery.from(Product.class);

        // Build predicates
        List<Predicate> validPredicates = new ArrayList<>();

        if (title != null && !title.isEmpty()) {
            Predicate titlePredicate = cb.like(cb.lower(productRoot.get("title")), "%" + title.toLowerCase() + "%");
            validPredicates.add(titlePredicate);
            logger.info("Added title predicate: {}", titlePredicate);
        }
        if (category != null && !category.isEmpty()) {
            Predicate categoryPredicate = cb.equal(cb.lower(productRoot.get("category")), category.toLowerCase());
            validPredicates.add(categoryPredicate);
            logger.info("Added category predicate: {}", categoryPredicate);
        }
        if (min != null && min != 0.0) {
            Predicate minPredicate = cb.greaterThanOrEqualTo(productRoot.get("price"), min);
            validPredicates.add(minPredicate);
            logger.info("Added min price predicate: {}", minPredicate);
        }
        if (max != null  && max != 0.0) {
            Predicate maxPredicate = cb.lessThanOrEqualTo(productRoot.get("price"), max);
            validPredicates.add(maxPredicate);
            logger.info("Added max price predicate: {}", maxPredicate);
        }
        if (rating != null) {
            Predicate ratingPredicate = cb.greaterThanOrEqualTo(productRoot.get("rating"), rating);
            validPredicates.add(ratingPredicate);
            logger.info("Added rating predicate: {}", ratingPredicate);
        }

        // Count query for total results
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Product> countRoot = countQuery.from(Product.class);
        // Example: Only using simple fields in the where clause, no joins
        List<Predicate> predicates = new ArrayList<>();
        if (category != null && !category.isEmpty()) {
            predicates.add(cb.equal(countRoot.get("category"), category));
        }
        if (max != null && max!= 0.0) {
            predicates.add(cb.lessThanOrEqualTo(countRoot.get("price"), max));
        }
        if (min != null && min!= 0.0) {
            predicates.add(cb.greaterThanOrEqualTo(countRoot.get("price"), min));
        }
        if (title != null && !title.isEmpty()) {
            predicates.add(cb.like(countRoot.get("title"), title));
        }

        countQuery.select(cb.count(countRoot)).where(predicates.toArray(new Predicate[0]));

        try {
            Long total = entityManager.createQuery(countQuery).getSingleResult();
            logger.info("Total products matching criteria: {}", total);

            // Pagination query
            criteriaQuery.select(productRoot).where(validPredicates.toArray(new Predicate[0]));

            // Apply sorting (Optional)
            if (pageable.getSort().isSorted()) {
                pageable.getSort().forEach(order -> {
                    if (order.isAscending()) {
                        criteriaQuery.orderBy(cb.asc(productRoot.get(order.getProperty())));
                    } else {
                        criteriaQuery.orderBy(cb.desc(productRoot.get(order.getProperty())));
                    }
                });
            }

            TypedQuery<Product> query = entityManager.createQuery(criteriaQuery);
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());

            List<Product> products = query.getResultList();
            logger.info("Fetched {} products for the current page", products.size());

            return new PageImpl<>(products, pageable, total);

        } catch (Exception e) {
            logger.error("Error occurred while querying products", e);
            return Page.empty(pageable);
        }
    }
}
