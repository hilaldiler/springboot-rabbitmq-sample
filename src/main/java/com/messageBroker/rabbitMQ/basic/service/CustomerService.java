package com.messageBroker.rabbitMQ.basic.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.messageBroker.rabbitMQ.basic.dto.CustomerCreateDTO;
import com.messageBroker.rabbitMQ.basic.dto.CustomerDTO;
import com.messageBroker.rabbitMQ.basic.model.Customer;
import com.messageBroker.rabbitMQ.basic.repository.CustomerRepository;

import jakarta.transaction.Transactional;

@Service
public class CustomerService {

	private final CustomerRepository customerRepository;
	private final CustomerCreateDTO customerCreateDTO;

    public CustomerService(CustomerRepository customerRepository, CustomerCreateDTO customerCreateDTO) {
        this.customerRepository = customerRepository;
        this.customerCreateDTO = customerCreateDTO;
    }
    
    protected Customer getCustomerById(String id){
        return customerRepository.findById(id).orElse(new Customer());
    }
    
    @Transactional
    public CustomerDTO getCustomerCreateDTOById(String id) {
        Optional<Customer> customerOptional = customerRepository.findById(id);

        return customerOptional.map(customerCreateDTO::create).orElse(new CustomerDTO());
    }
}
