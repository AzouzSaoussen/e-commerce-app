package com.azouz.ecommerce.product.repository;

import com.azouz.ecommerce.product.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Integer> {
}
