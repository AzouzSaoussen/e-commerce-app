package com.azouz.ecommerce.customer.controller;

import com.azouz.ecommerce.CustomerApplication;
import com.azouz.ecommerce.customer.dto.CustomerRequest;
import com.azouz.ecommerce.customer.model.Address;
import com.azouz.ecommerce.customer.model.Customer;
import com.azouz.ecommerce.customer.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = CustomerApplication.class)
@AutoConfigureMockMvc
class CustomerControllerTest {
    // Perform real HTTP calls to your controller
    @Autowired
    private MockMvc mockMvc;
    // Converts your Java objects to JSON and back (for request/response bodies)
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository repository;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }
    @Test
    void shouldCreateCustomer() throws Exception {
        //GIVEN
        Address address = new Address("Main St", "22", "1000");
        CustomerRequest request = new CustomerRequest(
                null,
                "John",
                "Doe",
                "john.doe@example.com",
                address
        );
        //WHEN + THEN
        mockMvc.perform(post("/api/v1/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.notNullValue()));
    }
    @Test
    void shouldUpdateCustomer() throws Exception {
        //GIVEN
        Address address = new Address("Main St", "22", "1000");
        Customer customer = new Customer(
                null,
                "John",
                "Doe",
                "john.doe@example.com",
                address
        );
        Customer savedCustomer = repository.save(customer);

        //WHEN
        CustomerRequest updateRequest = new CustomerRequest(
                savedCustomer.getId(),
                "Johnny",
                "Doe",
                "johnny.doe@example.com",
                new Address("Updated St", "32", "2000")
        );

        //THEN
        mockMvc.perform(put("/api/v1/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isAccepted());
        //AND : verify in DB
        Optional<Customer> updatedOpt = repository.findById(savedCustomer.getId());
        assertTrue(updatedOpt.isPresent(), "Updated customer should be present");
        updatedOpt.ifPresent(updated ->{
            assertEquals("Johnny", updated.getFirstname());
            assertEquals("Doe", updated.getLastname());
            assertEquals("Updated St", updated.getAddress().getStreet());
        });
    }
    @Test
    void shouldFindAllCustomers() throws Exception {
        //GIVEN
        List<Customer> customers = List.of(
                Customer.builder().id("id1").firstname("John").lastname("Doe").email("john.doe@example.com").address(new Address("Street", "123", "45678")).build(),
                Customer.builder().id("id2").firstname("Jane").lastname("Smith").email("jane.smith@example.com").address(new Address("Street", "456", "78901")).build()
        );
        repository.saveAll(customers);

        //WHEN + THEN: Call GET /api/v1/customer
        mockMvc.perform(get("/api/v1/customer"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstname").value("John"))
                .andExpect(jsonPath("$[1].firstname").value("Jane"));
    }
    @Test
    void shouldFindCustomerById() throws Exception {
        //GIVEN
        Customer customer = new Customer(
                null,
                "John",
                "Doe",
                "john.doe@example.com",
                new Address("Street", "123", "45678")
        );
        Customer saved = repository.save(customer);

        //WHEN + THEN: call the GET /api/v1/customer/{customer-id}
        mockMvc.perform(get("/api/v1/customer/{customer-id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstname").value("John"))
                .andExpect(jsonPath("$.lastname").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.address.street").value("Street"));
    }
    @Test
    void shouldReturnTrueIfCustomerExists() throws Exception{
        //GIVEN
        Customer customer = new Customer(
                null,
                "John",
                "Doe",
                "john.doe@example.com",
                new Address("Street", "123", "45678")
        );
        Customer saved = repository.save(customer);
        //WHEN + THEN: Call GET /api/v1/customer/exists/{customer-id} -> expected true
        mockMvc.perform(get("/api/v1/customer/exists/{customer-id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
    @Test
    void shouldReturnFalseIfCustomerDoesNotExist() throws Exception {
        // GIVEN: Nothing in DB (or clean DB)

        // WHEN + THEN: Perform GET /api/v1/customer/exists/{customer-id} -> expect false
        mockMvc.perform(get("/api/v1/customer/exists/{customer-id}", "nonexistent-id"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }
    @Test
    void shouldDeleteCustomer() throws Exception {
        //GIVEN
        Customer customer = new Customer(
                null,
                "John",
                "Doe",
                "john.doe@example.com",
                new Address("Street", "123", "45678")
        );
        Customer saved = repository.save(customer);
        //WHEN + THEN : Perform DELETE /api/v1/customer/{id} -> expected 202 ACCEPTED
        mockMvc.perform(delete("/api/v1/customer/{customer-id}", saved.getId()))
                .andExpect(status().isAccepted());
        assertFalse(repository.findById(saved.getId()).isPresent());
    }
    @Test
    void shouldReturn404WhenDeletingNonExistingCustomer() throws Exception {
        // GIVEN: Clean DB (no customers)

        // WHEN + THEN: Try to delete an invalid ID
        mockMvc.perform(delete("/api/v1/customer/{customer-id}", "nonexistent-id"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.businessErrorCode").value(1002))
                .andExpect(jsonPath("$.businessErrorDescription").value("Customer not found"));
    }
}

