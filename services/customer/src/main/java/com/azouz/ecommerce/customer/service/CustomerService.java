package com.azouz.ecommerce.customer.service;

import com.azouz.ecommerce.customer.dto.CustomerRequest;
import jakarta.validation.Valid;

public interface CustomerService {

    String createCustomer(@Valid CustomerRequest request);

    void updateCustomer(@Valid CustomerRequest request);
}
