package com.azouz.ecommerce.product.repository;

import com.azouz.ecommerce.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
