package com.messageBroker.rabbitMQ.basic.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.messageBroker.rabbitMQ.basic.model.Account;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RabbitMQJsonConsumer {

    @RabbitListener(queues = {"${rabbitmq.queue.json.name}"})
    public void consumeJsonMessage(Account account) {
        log.info(String.format("Received json message -> %s", account.toString()));
    }

}
