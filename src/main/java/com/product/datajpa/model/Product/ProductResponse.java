package com.product.datajpa.model.Product;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ProductResponse {
    private int id;
    private String title;
    private String description;
    private String category;
    private double price;
    private double discountPercentage;
    private double rating;
    private int stock;
    private List<String> tags;
    private String brand;
    @NotNull
    @Column(unique = true)
    private String sku;
    private double weight;
    private Dimensions dimensions;
    private String warrantyInformation;
    private String shippingInformation;
    private String availabilityStatus;
    private List<Review> reviews;
    private String returnPolicy;
    private int minimumOrderQuantity;
    private Meta meta;
    private List<String> images;
    private String thumbnail;

    @Override
    public String toString() {
        return "ProductResponse{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", discountPercentage=" + discountPercentage +
                ", rating=" + rating +
                ", stock=" + stock +
                ", tags=" + tags +
                ", brand='" + brand + '\'' +
                ", sku='" + sku + '\'' +
                ", weight=" + weight +
                ", dimensions=" + dimensions +
                ", warrantyInformation='" + warrantyInformation + '\'' +
                ", shippingInformation='" + shippingInformation + '\'' +
                ", availabilityStatus='" + availabilityStatus + '\'' +
                ", reviews=" + reviews +
                ", returnPolicy='" + returnPolicy + '\'' +
                ", minimumOrderQuantity=" + minimumOrderQuantity +
                ", meta=" + meta +
                ", images=" + images +
                ", thumbnail='" + thumbnail + '\'' +
                '}';
    }
}
