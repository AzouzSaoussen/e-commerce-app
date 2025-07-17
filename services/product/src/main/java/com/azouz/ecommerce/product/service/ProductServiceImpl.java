package com.azouz.ecommerce.product.service;

import com.azouz.ecommerce.product.dto.ProductPurchaseRequest;
import com.azouz.ecommerce.product.dto.ProductPurchaseResponse;
import com.azouz.ecommerce.product.dto.ProductRequest;
import com.azouz.ecommerce.product.dto.ProductResponse;
import com.azouz.ecommerce.product.exception.ProductNotFoundException;
import com.azouz.ecommerce.product.mapper.ProductMapper;
import com.azouz.ecommerce.product.model.Product;
import com.azouz.ecommerce.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    @Override
    public Integer createProduct(ProductRequest request) {
        Product product = mapper.toProduct(request);
        return repository.save(product).getId();
    }

    @Override
    public List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> requests) {
        return List.of();
    }

    @Override
    public ProductResponse findById(Integer productId) {
        return repository.findById(productId).map(mapper::toProductResponse).orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public Page<ProductResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toProductResponse);
    }
}
