package com.azouz.ecommerce.orderline;

import java.util.List;

public interface OrderLineService {
    void saveOrderLine(OrderLineRequest orderLineRequest);
    List<OrderLineResponse> findByOrderId(Integer orderId);
}
