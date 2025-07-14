package com.azouz.ecommerce.customer.mapper;

import com.azouz.ecommerce.customer.dto.CustomerRequest;
import com.azouz.ecommerce.customer.dto.CustomerResponse;
import com.azouz.ecommerce.customer.model.Address;
import com.azouz.ecommerce.customer.model.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class CustomerMapperTest {
    @InjectMocks
    private CustomerMapper mapper;
    @Test
    void shouldMapRequestToEntity() {
        // Given
        CustomerRequest request = new CustomerRequest(
                null,
                "Alice",
                "Smith",
                "alice@example.com",
                new Address("Main St", "22", "1000")
        );

        // When
        Customer entity = mapper.toEntity(request);

        // Then
        assertNotNull(entity);
        assertEquals("Alice", entity.getFirstname());
        assertEquals("Smith", entity.getLastname());
        assertEquals("alice@example.com", entity.getEmail());
        assertEquals("Main St", entity.getAddress().getStreet());
        assertEquals("22", entity.getAddress().getHouseNumber());
        assertEquals("1000", entity.getAddress().getZipCode());
    }

    @Test
    void shouldMapEntityToResponse() {
        // Given
        Customer customer = Customer.builder()
                .id("customer-id")
                .firstname("Bob")
                .lastname("Brown")
                .email("bob@example.com")
                .address(new Address("Elm St", "44", "2000"))
                .build();

        // When
        CustomerResponse response = mapper.fromCustomer(customer);

        // Then
        assertNotNull(response);
        assertEquals("customer-id", response.id());
        assertEquals("Bob", response.firstname());
        assertEquals("Brown", response.lastname());
        assertEquals("bob@example.com", response.email());
        assertEquals("Elm St", response.address().getStreet());
        assertEquals("44", response.address().getHouseNumber());
        assertEquals("2000", response.address().getZipCode());
    }

}