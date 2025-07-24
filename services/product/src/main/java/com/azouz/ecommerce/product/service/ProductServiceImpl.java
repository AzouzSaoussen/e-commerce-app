package com.azouz.ecommerce.product.service;

import com.azouz.ecommerce.product.dto.ProductPurchaseRequest;
import com.azouz.ecommerce.product.dto.ProductPurchaseResponse;
import com.azouz.ecommerce.product.dto.ProductRequest;
import com.azouz.ecommerce.product.dto.ProductResponse;
import com.azouz.ecommerce.product.exception.BusinessErrorCodes;
import com.azouz.ecommerce.product.exception.ProductNotFoundException;
import com.azouz.ecommerce.product.exception.ProductPurchaseException;
import com.azouz.ecommerce.product.mapper.ProductMapper;
import com.azouz.ecommerce.product.model.Product;
import com.azouz.ecommerce.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        if(requests == null || requests.isEmpty()){
            return List.of();
        }
        Map<Integer, ProductPurchaseRequest> requestMap = requests.stream()
                .collect(Collectors.toMap(ProductPurchaseRequest::productId, Function.identity()));

        List<Integer> productIds = new ArrayList<>(requestMap.keySet());
        List<Product> storedProducts = repository.findAllByIdInOrderById(productIds);
        if ((storedProducts.size() != productIds.size())) {
            throw new ProductPurchaseException(BusinessErrorCodes.PRODUCT_PURCHASE_VALIDATION_FAILED);
        }
        List<ProductPurchaseResponse> responses = new ArrayList<>();
        for(Product product : storedProducts){
            ProductPurchaseRequest request = requestMap.get(product.getId());
            double requestedQty = request.quantity();
            if (product.getAvailableQuantity() < requestedQty) {
                throw new ProductPurchaseException(BusinessErrorCodes.INSUFFICIENT_STOCK);
            }
            product.setAvailableQuantity(product.getAvailableQuantity() - requestedQty);
            responses.add(mapper.toProductPurchaseResponse(product, requestedQty));
        }
        repository.saveAll(storedProducts);
        return responses;
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
