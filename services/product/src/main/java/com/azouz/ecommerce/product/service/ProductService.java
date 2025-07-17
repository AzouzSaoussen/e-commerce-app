package com.azouz.ecommerce.product.service;

import com.azouz.ecommerce.product.dto.ProductPurchaseRequest;
import com.azouz.ecommerce.product.dto.ProductPurchaseResponse;
import com.azouz.ecommerce.product.dto.ProductRequest;
import com.azouz.ecommerce.product.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    Integer createProduct(ProductRequest request);

    List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> requests);

    ProductResponse findById(Integer productId);

    Page<ProductResponse> findAll(Pageable pageable);
}
