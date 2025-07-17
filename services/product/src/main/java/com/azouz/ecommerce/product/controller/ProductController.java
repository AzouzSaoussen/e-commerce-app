package com.azouz.ecommerce.product.controller;

import com.azouz.ecommerce.product.dto.ProductPurchaseRequest;
import com.azouz.ecommerce.product.dto.ProductPurchaseResponse;
import com.azouz.ecommerce.product.dto.ProductRequest;
import com.azouz.ecommerce.product.dto.ProductResponse;
import com.azouz.ecommerce.product.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

    @PostMapping
    public ResponseEntity<Integer> createProduct(@RequestBody @Valid ProductRequest request){
        return ResponseEntity.ok(service.createProduct(request));
    }

    @PostMapping("/purchase")
    public ResponseEntity<List<ProductPurchaseResponse>> purchaseProducts(
            @RequestBody List<ProductPurchaseRequest> requests
            ){
        return ResponseEntity.ok(service.purchaseProducts(requests));
    }

    @GetMapping("/{product-id}")
    public  ResponseEntity<ProductResponse> findById(
            @PathVariable("product-id") Integer productId
    ){
        return ResponseEntity.ok(service.findById(productId));
    }
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> findAll(
            @PageableDefault(size = 10, page = 0, sort = "name") Pageable pageable
    ){
        return ResponseEntity.ok(service.findAll(pageable));
    }
}
