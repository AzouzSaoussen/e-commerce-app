package com.azouz.ecommerce.orderline;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderLineRequest(
        Integer id,
        @NotNull(message = "Product is mandatory")
        Integer productId,
        @NotNull(message = "Order is mandatory")
        Integer orderId,
        @Positive(message = "Quantity is mandatory ")
        double quantity) {
}
