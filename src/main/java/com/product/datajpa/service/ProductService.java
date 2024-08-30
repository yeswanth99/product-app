package com.product.datajpa.service;

import com.product.datajpa.entity.Dimensions;
import com.product.datajpa.entity.Meta;
import com.product.datajpa.entity.Product;
import com.product.datajpa.entity.Review;
import com.product.datajpa.exception.ProductNotFoundException;
import com.product.datajpa.model.Product.ProductResponse;
import com.product.datajpa.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Service
@Slf4j
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public List<Product> getAllProducts(){
        List<Product> products = productRepository.findAll();
        return products;
    }

    @Transactional
    public Product findbySku(String skucode){
        Optional<Product> productOptional = productRepository.findBySku(skucode);
        if (productOptional.isPresent()){
            return productOptional.get();
        }
        else {
            throw new ProductNotFoundException();
        }
    }

    @Transactional
    public void updateData(List<ProductResponse> productResponses){
        for (ProductResponse pr: productResponses){
            Optional<Product> productOptional= productRepository.findById((long) pr.getId());
            if (!productOptional.isPresent()){
                addProduct(pr);
                log.info("Product added!!!!!");
            }
        }
    }

    @Transactional
    public void addProduct(ProductResponse request) {

        log.info("Product Log -->");
        try{
            // Create a Product without reviews
            Product product = Product.builder()
                    .title(request.getTitle())
                    .category(request.getCategory())
                    .price(request.getPrice())
                    .discountPercentage(request.getDiscountPercentage())
                    .rating(request.getRating())
                    .stock(request.getStock())
                    .tags(request.getTags())
                    .brand(request.getBrand())
                    .sku(request.getSku())
                    .weight(request.getWeight())
                    .dimensions(getDimensions(request.getDimensions()))
                    .warrantyInformation(request.getWarrantyInformation())
                    .shippingInformation(request.getShippingInformation())
                    .availabilityStatus(request.getAvailabilityStatus())
                    .returnPolicy(request.getReturnPolicy())
                    .minimumOrderQuantity(request.getMinimumOrderQuantity())
                    .meta(getMeta(request.getMeta()))
                    .images(request.getImages())
                    .thumbnail(request.getThumbnail())
                    .build();
            log.info("Product Log --> {}",product);
            // Save the product first
            Product savedProduct = productRepository.save(product);

            // Create and set reviews
            List<Review> reviews = getReview(request.getReviews(), savedProduct);
            savedProduct.setReviews(reviews);

            // Update the product with reviews
            productRepository.save(savedProduct);

        } catch (Exception e) {
            log.error("Error saving product: ", e);
            throw e;
        }
    }

    private Meta getMeta(com.product.datajpa.model.Product.Meta meta) {
        Meta m = new Meta();
        m.setBarcode(meta.getBarcode());
        m.setQrCode(meta.getQrCode());
        m.setCreatedAt(meta.getCreatedAt());
        m.setUpdatedAt(meta.getUpdatedAt());
        log.info("Meta log {} ", m);
        return m;
    }

    private Dimensions getDimensions(com.product.datajpa.model.Product.Dimensions dimensions) {
        return Dimensions.builder()
                .depth(dimensions.getDepth())
                .width(dimensions.getWidth())
                .height(dimensions.getHeight()).build();
    }

    private List<Review> getReview(List<com.product.datajpa.model.Product.Review> reviews, Product product) {
        List<Review> reviewList = new ArrayList<>();
        reviews.stream()
                .map(review -> mapToEntityReview(review, product))
                .forEach(reviewList::add);

        return reviewList;
    }

    private Review mapToEntityReview(com.product.datajpa.model.Product.Review review1, Product product) {
        Review r = new Review();
        r.setComment(review1.getComment());
        r.setDate(review1.getDate());
        r.setReviewerName(review1.getReviewerName());
        r.setRating(review1.getRating());
        r.setReviewerEmail(review1.getReviewerEmail());
        r.setProduct(product); // Set the product reference

        return r;
    }

    public List<Product> findbyTag(String tag) {
        Optional<List<Product>> products = productRepository.findByTags(tag);
        if (products.isPresent()){
            return products.get();
        }
        else {
            throw new RuntimeException("No Products Found");
        }
    }

    @Transactional
    public Page<Product> findProductsByPage(int pageNum, int pageSize){
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        Page<Product> productList = productRepository.findAll(pageable);
        return productList;
    }

    @Transactional
    public List<String> findCategory(){
        return productRepository.findDistinctCategories();
    }

    @Transactional
    public Page<Product> findProductsByPageAndSort(int pageNum, int pageSize){
        Pageable pageable = PageRequest.of(pageNum,pageSize, Sort.Direction.DESC, "id");
        Page<Product> productList = productRepository.findAll(pageable);
        return productList;
    }

    @Transactional
    public Page<Product> findProductsByPageAndSortCategory(int pageNum, int pageSize, String category){
        Pageable pageable = PageRequest.of(pageNum,pageSize, Sort.by(Sort.Direction.ASC,"id"));
        Page<Product> productList = productRepository.findByCategory(category, pageable);
        return productList;
    }

    @Transactional
    public List<Product> findProductsByCategoryAndPrice(String category, double min, double max){
        Optional<List<Product>> productList = productRepository.findByCategoryAndPriceGreaterThan(category, min, max);
        return productList.get();
    }

    @Transactional
    public List<Product> searchProductsByDynamicQuery(Optional<String> title,
                                                      Optional<String> category,
                                                      Optional<Double> min,
                                                      Optional<Double> max,
                                                      Optional<Double> rating){
        Specification<Product> productSpecification = ProductSpecification
                .getProductByDynamicQuery(title.orElse(null),
                        category.orElse(null),
                        min.orElse(Double.MIN_VALUE),
                        max.orElse(Double.MAX_VALUE),
                        rating.orElse(0.0));
        List<Product> productList = productRepository.findAll(productSpecification);
        return productList;
    }

    @Transactional
    public Page<Product> searchProductsByDynamicQueryPage(Optional<String> title,
                                                      Optional<String> category,
                                                      Optional<Double> min,
                                                      Optional<Double> max,
                                                      Optional<Double> rating,
                                                          Optional<Integer> pageNum,
                                                          Optional<Integer> pageSize){

        Pageable pageable = PageRequest.of(pageNum.get(), pageSize.get());
        Page<Product> productPage = productRepository.findProductsByCriteria(title.orElse(null),
                category.orElse(null),
                min.orElse(0.0),
                max.orElse(0.0),
                rating.orElse(null),
                pageable);
        System.out.println(productPage.get());
        return productPage;
    }
}
