package com.azouz.ecommerce.product.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequest(
        Integer id,
        @NotNull(message = "Product name is required")
        String name,
        String description,
        @Positive (message = "Product price should be positive")
        BigDecimal price,
        @Positive(message = "Available quantity should be positive")
        double availableQuantity,
        @NotNull(message = "Product category is required")
        Integer categoryId
) {
}
