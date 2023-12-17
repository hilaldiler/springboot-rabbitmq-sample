package com.messageBroker.rabbitMQ.basic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.messageBroker.rabbitMQ.basic.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, String> {

}
