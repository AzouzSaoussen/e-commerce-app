package com.azouz.ecommerce.order.service;

import com.azouz.ecommerce.customer.CustomerClient;
import com.azouz.ecommerce.kafka.OrderConfirmation;
import com.azouz.ecommerce.kafka.OrderProducer;
import com.azouz.ecommerce.order.dto.OrderResponse;
import com.azouz.ecommerce.order.exception.BusinessException;
import com.azouz.ecommerce.order.dto.OrderRequest;
import com.azouz.ecommerce.order.mapper.OrderMapper;
import com.azouz.ecommerce.order.repository.OrderRepository;
import com.azouz.ecommerce.orderline.OrderLineRequest;
import com.azouz.ecommerce.orderline.OrderLineService;
import com.azouz.ecommerce.product.ProductClient;
import com.azouz.ecommerce.product.PurchaseRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.azouz.ecommerce.order.exception.BusinessErrorCodes.CUSTOMER_NOT_FOUND;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;

    @Transactional
    @Override
    public Integer createOrder(OrderRequest request) {
        // 1. Validate customer via customerClient
        var customer = customerClient.findCustomerById(request.customerId())
                .orElseThrow(() -> new BusinessException(CUSTOMER_NOT_FOUND));

        // 2. Purchase products via productClient
        var purchaseProducts = productClient.purchaseProducts(request.products());

        // 3. Persist order
        var order = repository.save(mapper.toOrder(request));
        // 4. Save order lines
        for (PurchaseRequest purchaseRequest : request.products()) {
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    )
            );
        }

        //todo start payment process --> payment-ms

        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        request.reference(),
                        request.totalAmount(),
                        request.paymentMethod(),
                        customer,
                        purchaseProducts
                )
        );
        return order.getId();
    }

    @Override
    public List<OrderResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::fromOrder)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse findById(Integer orderId) {
        return repository.findById(orderId)
                .map(mapper::fromOrder)
                .orElseThrow(()-> new EntityNotFoundException(String.format("No order found with the provided ID: %d", orderId)));
    }
}
