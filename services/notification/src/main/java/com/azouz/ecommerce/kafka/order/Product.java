package com.azouz.ecommerce.kafka.order;

import java.math.BigDecimal;

public record Product(
        Integer ProductId,
        String name,
        String description,
        BigDecimal price,
        double quantity
) {
}
