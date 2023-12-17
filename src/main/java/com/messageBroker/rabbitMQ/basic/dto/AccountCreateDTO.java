package com.messageBroker.rabbitMQ.basic.dto;

import org.springframework.stereotype.Component;

import com.messageBroker.rabbitMQ.basic.model.Account;

@Component
public class AccountCreateDTO {

    public AccountDTO create(Account account){
        return AccountDTO.builder()
                .id(account.getId())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .customerId(account.getCustomerId())
                .build();
    }
}
