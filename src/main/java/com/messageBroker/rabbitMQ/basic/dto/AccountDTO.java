package com.messageBroker.rabbitMQ.basic.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class AccountDTO implements Serializable {

    private String id;
    private String customerId;
    private Double balance;
    private String currency;
}
