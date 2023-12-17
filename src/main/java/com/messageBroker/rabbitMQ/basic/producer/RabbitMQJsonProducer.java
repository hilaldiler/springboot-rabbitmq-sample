package com.messageBroker.rabbitMQ.basic.producer;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.messageBroker.rabbitMQ.basic.model.Account;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RabbitMQJsonProducer {

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.json.key}")
    private String routingJsonKey;

    private RabbitTemplate rabbitTemplate;

    public RabbitMQJsonProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendJsonMessage(Object object) {
        log.info(String.format("Json message sent -> %s", object.toString()));
        rabbitTemplate.convertAndSend(exchange, routingJsonKey, object);
    }

}
