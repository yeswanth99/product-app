package com.product.datajpa.model.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Meta {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String barcode;
    private String qrCode;
}
