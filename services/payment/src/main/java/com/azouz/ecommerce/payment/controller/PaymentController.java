package com.azouz.ecommerce.payment.controller;

import com.azouz.ecommerce.payment.dto.PaymentRequest;
import com.azouz.ecommerce.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v2/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService service;
    @PostMapping
    public ResponseEntity<Integer> createPayment(@RequestBody @Valid PaymentRequest request){
        return ResponseEntity.ok(service.createPayment(request));

    }
}
