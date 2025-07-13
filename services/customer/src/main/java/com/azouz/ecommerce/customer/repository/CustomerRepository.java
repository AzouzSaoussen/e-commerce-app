package com.azouz.ecommerce.customer.repository;

import com.azouz.ecommerce.customer.model.Customer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {
    boolean existsByEmail(@NotNull(message = "Customer email is required") @Email(message = "Customer email is not a valid email address") String email);
}
