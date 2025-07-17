package com.azouz.ecommerce.product.mapper;

import com.azouz.ecommerce.product.dto.ProductRequest;
import com.azouz.ecommerce.product.dto.ProductResponse;
import com.azouz.ecommerce.product.exception.CategoryNotFoundException;
import com.azouz.ecommerce.product.model.Product;
import com.azouz.ecommerce.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    private final CategoryRepository categoryRepository;
    public Product toProduct(ProductRequest request){
        var category = categoryRepository.findById(request.categoryId()).orElseThrow(CategoryNotFoundException::new);
        return Product.builder().
                id(request.id())
                .name(request.name())
                .description(request.description())
                .availableQuantity(request.availableQuantity())
                .price(request.price())
                .category(category)
        .build();
    }

    public ProductResponse toProductResponse( Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getAvailableQuantity(),
                product.getPrice(),
                product.getCategory().getId(),
                product.getCategory().getName(),
                product.getCategory().getDescription()
        );
    }
}
