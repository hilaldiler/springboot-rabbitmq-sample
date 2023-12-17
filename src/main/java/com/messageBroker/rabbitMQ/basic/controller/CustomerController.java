package com.messageBroker.rabbitMQ.basic.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.messageBroker.rabbitMQ.basic.dto.CustomerDTO;
import com.messageBroker.rabbitMQ.basic.service.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable String id){
        return ResponseEntity.ok(customerService.getCustomerCreateDTOById(id));
    }
  

}
