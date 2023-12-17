package com.messageBroker.rabbitMQ.basic.dto;

import org.springframework.stereotype.Component;
import com.messageBroker.rabbitMQ.basic.model.Customer;

@Component
public class CustomerCreateDTO{

    public CustomerDTO create(Customer customer) {
    	CustomerDTO customerDto = new CustomerDTO();
        customerDto.setId(customer.getId());
        customerDto.setName(customer.getName());

        return customerDto;
    }

}
