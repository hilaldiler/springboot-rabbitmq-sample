package com.messageBroker.rabbitMQ.basic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.messageBroker.rabbitMQ.basic.model.Account;

public interface AccountRepository extends JpaRepository<Account, String> {


}
