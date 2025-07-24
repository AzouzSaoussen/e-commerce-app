package com.azouz.ecommerce.order.service;

import com.azouz.ecommerce.order.dto.OrderRequest;
import com.azouz.ecommerce.order.dto.OrderResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface OrderService {
    Integer createOrder(@Valid OrderRequest request);

    List<OrderResponse> findAll();

    OrderResponse findById(Integer orderId);
}
