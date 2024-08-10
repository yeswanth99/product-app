package com.product.datajpa.model.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    private int rating;
    private String comment;
    private LocalDateTime date;
    private String reviewerName;
    private String reviewerEmail;
}
