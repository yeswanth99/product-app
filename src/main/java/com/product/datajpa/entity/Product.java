package com.product.datajpa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Entity
@Table(name = "products", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"sku", "title"})
})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String description;
    private String category;
    private double price;
    private double discountPercentage;
    private double rating;
    private int stock;

    @ElementCollection
    private List<String> tags;

    private String brand;
    private String sku;
    private double weight;

    @Embedded
    private Dimensions dimensions;

    private String warrantyInformation;
    private String shippingInformation;
    private String availabilityStatus;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews;

    private String returnPolicy;
    private int minimumOrderQuantity;

    @Embedded
    private Meta meta;

    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image")
    private List<String> images;

    private String thumbnail;
}
