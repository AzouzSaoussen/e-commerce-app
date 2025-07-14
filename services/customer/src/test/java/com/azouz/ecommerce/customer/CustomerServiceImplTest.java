package com.azouz.ecommerce.customer;

import com.azouz.ecommerce.customer.dto.CustomerRequest;
import com.azouz.ecommerce.customer.dto.CustomerResponse;
import com.azouz.ecommerce.customer.exception.CustomerNotFoundException;
import com.azouz.ecommerce.customer.exception.EmailAlreadyExistsException;
import com.azouz.ecommerce.customer.model.Address;
import com.azouz.ecommerce.customer.model.Customer;
import com.azouz.ecommerce.customer.mapper.CustomerMapper;
import com.azouz.ecommerce.customer.repository.CustomerRepository;
import com.azouz.ecommerce.customer.service.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {
    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private CustomerServiceImpl customerService;
    @Mock
    private CustomerMapper mapper;

    // -- createCustomer tests
    @Test
    public void shouldCreateCustomerSuccessfully(){
        //Given
        CustomerRequest request = new CustomerRequest(
                null,
                "John",
                "Doe",
                "john.doe@example.com",
                new Address("Street", "123", "45678" )
        );
        when(customerRepository.existsByEmail("john.doe@example.com")).thenReturn(false);

        Customer toSaveCustomer = Customer.builder()
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .address(request.address())
                .build();
        when(mapper.toEntity(request)).thenReturn(toSaveCustomer);

        Customer savedCustomer = Customer.builder().id("generated-id")
                    .firstname("John")
                    .lastname("Doe")
                    .email("john.doe@example.com")
                    .address(request.address())
                    .build();
        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        // WHEN
        String customerId = customerService.createCustomer(request);

        // THEN
        assertEquals("generated-id", customerId);
        verify(customerRepository).existsByEmail("john.doe@example.com");
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void shouldThrowExceptionIfEmailExists(){
        //GIVEN
        CustomerRequest request = new CustomerRequest(
                null,
                "Jane",
                "Smith",
                "jane.smith@example.com",
                new Address("Street", "456", "78901")
        );
        when(customerRepository.existsByEmail("jane.smith@example.com")).thenReturn(true);

        EmailAlreadyExistsException exception = assertThrows(
                EmailAlreadyExistsException.class,
                () -> customerService.createCustomer(request)
        );
        assertEquals("Email already exists", exception.getMessage());
        verify(customerRepository).existsByEmail("jane.smith@example.com");
        verify(customerRepository, never()).save(any());
        verify(mapper, never()).toEntity(any());
    }
    // -- updateCustomer tests
    @Test
    void shouldUpdateCustomerSuccessfully(){
        // GIVEN
        Customer existingCustomer = Customer.builder()
                .id("existing-id")
                .firstname("OldFirst")
                .lastname("OldLast")
                .email("old@example.com")
                .address(new Address("OldStreet", "1", "00000"))
                .build();
        when(customerRepository.findById("existing-id")).thenReturn(Optional.of(existingCustomer));

        // Incoming request with NEW data
        CustomerRequest request = new CustomerRequest(
                "existing-id",
                "NewFirst",
                "NewLast",
                "new@example.com",
                new Address("NewStreet", "2", "11111")
        );

        //WHEN
        customerService.updateCustomer(request);
        // Then - verify fields were updated
        assertEquals("NewFirst", existingCustomer.getFirstname());
        assertEquals("NewLast", existingCustomer.getLastname());
        assertEquals("new@example.com", existingCustomer.getEmail());
        assertEquals("NewStreet", existingCustomer.getAddress().getStreet());

        verify(customerRepository).save(existingCustomer);
    }
    @Test
    void shouldIgnoreBlankFieldsInUpdate() {
        // Existing in DB
        Customer existingCustomer = Customer.builder()
                .id("existing-id")
                .firstname("John")
                .lastname("Doe")
                .email("john@example.com")
                .address(new Address("Street", "123", "45678"))
                .build();

        when(customerRepository.findById("existing-id"))
                .thenReturn(Optional.of(existingCustomer));

        // Incoming request with only email to change
        CustomerRequest request = new CustomerRequest(
                "existing-id",
                "  ",    // blank, should ignore
                "",      // blank, should ignore
                "new-email@example.com",
                null     // null address, should ignore
        );

        // When
        customerService.updateCustomer(request);

        // Then - email updated, others untouched
        assertEquals("John", existingCustomer.getFirstname());
        assertEquals("Doe", existingCustomer.getLastname());
        assertEquals("new-email@example.com", existingCustomer.getEmail());
        assertEquals("Street", existingCustomer.getAddress().getStreet());

        verify(customerRepository).save(existingCustomer);
    }
    @Test
    void shouldThrowExceptionIfCustomerNotFound() {
        // Given: DB has no customer with ID
        when(customerRepository.findById("missing-id")).thenReturn(Optional.empty());

        // When + Then
        assertThrows(CustomerNotFoundException.class, () ->
                customerService.updateCustomer(new CustomerRequest(
                        "missing-id",
                        "First",
                        "Last",
                        "email@example.com",
                        new Address("Street", "Num", "Zip")
                ))
        );

        verify(customerRepository).findById("missing-id");
        verify(customerRepository, never()).save(any());
    }
    // -- findAllCustomers tests
    @Test
    void shouldReturnAllCustomers(){
        //GIVEN
        List<Customer> customers = List.of(
                Customer.builder().id("id1").firstname("John").lastname("Doe").email("john.doe@example.com").address(new Address("Street", "123", "45678")).build(),
                Customer.builder().id("id2").firstname("Jane").lastname("Smith").email("jane.smith@example.com").address(new Address("Street", "456", "78901")).build()
        );
        when(customerRepository.findAll()).thenReturn(customers);
        when(mapper.fromCustomer(any(Customer.class)))
                .thenAnswer(invocation ->{
                    Customer c = invocation.getArgument(0);
                    return new CustomerResponse(c.getId(), c.getFirstname(), c.getLastname(), c.getEmail(), c.getAddress());
                });
        //WHEN
        List<CustomerResponse> result = customerService.findAllCustomers();

        //THEN
        assertEquals(customers.size(), result.size());
        assertEquals(customers.get(0).getId(), result.get(0).id());
        assertEquals(customers.get(1).getId(), result.get(1).id());
        verify(customerRepository).findAll();
        verify(mapper, times(2)).fromCustomer(any(Customer.class));

    }
    // -- existsById tests
    @Test
    void shouldReturnTrueWhenCustomerExists() {
        when(customerRepository.findById("existing-id"))
                .thenReturn(Optional.of(new Customer()));

        assertTrue(customerService.existsById("existing-id"));
        verify(customerRepository).findById("existing-id");
    }

    @Test
    void shouldReturnFalseWhenCustomerDoesNotExist() {
        when(customerRepository.findById("missing-id"))
                .thenReturn(Optional.empty());

        assertFalse(customerService.existsById("missing-id"));
        verify(customerRepository).findById("missing-id");
    }
    // -- findById tests
    @Test
    void shouldReturnCustomerById(){
        //GIVEN
        Customer customer = Customer.builder()
                .id("id1")
                .firstname("John")
                .build();
        when(customerRepository.findById("id1")).thenReturn(Optional.of(customer));
        when(mapper.fromCustomer(customer)).thenReturn(
                new CustomerResponse("id1", "John", "Doe", "email@example.com", null)
        );
        //WHEN
        CustomerResponse response = customerService.findById("id1");
        //THEN
        assertEquals("id1", response.id());
        assertEquals("John", response.firstname());
        assertEquals("Doe", response.lastname());
        verify(customerRepository).findById("id1");
        verify(mapper).fromCustomer(customer);
    }
    @Test
    void shouldThrowExceptionWhenCustomerNotFound() {
        when(customerRepository.findById("missing-id")).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () ->
                customerService.findById("missing-id")
        );

        verify(customerRepository).findById("missing-id");
        verifyNoInteractions(mapper);
    }
    // -- deleteCustomer tests
    @Test
    void shouldDeleteCustomerById() {
        String id = "delete-id";

        // When
        customerService.deleteCustomer(id);

        // Then
        verify(customerRepository).deleteById(id);
    }

}