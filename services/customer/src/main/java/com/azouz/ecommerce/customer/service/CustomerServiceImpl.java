package com.azouz.ecommerce.customer.service;

import com.azouz.ecommerce.customer.dto.CustomerResponse;
import com.azouz.ecommerce.customer.exception.CustomerNotFoundException;
import com.azouz.ecommerce.customer.exception.EmailAlreadyExistsException;
import com.azouz.ecommerce.customer.model.Customer;
import com.azouz.ecommerce.customer.mapper.CustomerMapper;
import com.azouz.ecommerce.customer.repository.CustomerRepository;
import com.azouz.ecommerce.customer.dto.CustomerRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository repository;
    private final CustomerMapper mapper;
    @Override
    public String createCustomer(CustomerRequest request) {
        if(repository.existsByEmail(request.email())){
            throw new EmailAlreadyExistsException();
        }
        Customer customer = mapper.toEntity(request);
        return repository.save(customer).getId();
    }

    @Override
    public void updateCustomer(CustomerRequest request) {
       var customer = repository.findById(request.id()).orElseThrow(CustomerNotFoundException::new);
        mergerCustomer(customer, request);
        repository.save(customer);
    }

    @Override
    public List<CustomerResponse> findAllCustomers() {
        return repository.findAll()
                .stream()
                .map(mapper::fromCustomer)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean existsById(String id) {
        return repository.findById(id).isPresent();
    }

    @Override
    public CustomerResponse findById(String id) {
        return repository.findById(id).map(mapper::fromCustomer).orElseThrow(CustomerNotFoundException::new);
    }

    @Override
    public void deleteCustomer(String id) {
        if (!repository.existsById(id)) {
            throw new CustomerNotFoundException();
        }
        repository.deleteById(id);
    }

    private void mergerCustomer(Customer customer, CustomerRequest request) {
        if(StringUtils.isNotBlank(request.firstname())){
            customer.setFirstname(request.firstname());
        }
        if(StringUtils.isNotBlank(request.lastname())){
            customer.setLastname(request.lastname());
        }
        if(StringUtils.isNotBlank(request.email())){
            customer.setEmail(request.email());
        }
        if(request.address() != null){
            customer.setAddress(request.address());
        }
    }
}
