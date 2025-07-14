package com.azouz.ecommerce.customer.service;

import com.azouz.ecommerce.customer.dto.CustomerRequest;
import com.azouz.ecommerce.customer.dto.CustomerResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface CustomerService {

    String createCustomer(@Valid CustomerRequest request);

    void updateCustomer(@Valid CustomerRequest request);

    List<CustomerResponse> findAllCustomers();

    Boolean existsById(String id);

    CustomerResponse findById(String id);

    void deleteCustomer(String id);
}
