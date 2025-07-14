package com.azouz.ecommerce.customer.dto;

import com.azouz.ecommerce.customer.model.Address;


public record CustomerResponse (
        String id,
        String firstname,
        String lastname,
        String email,
        Address address
){
}
