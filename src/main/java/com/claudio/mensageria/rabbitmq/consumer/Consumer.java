package com.claudio.mensageria.rabbitmq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Consumer {

    @RabbitListener(queues = "${queue.games}", containerFactory = "listenerContainerFactory")
    public void jogosConsumer(String message) {
        log.info("Mensagem recebida: ");
        log.info(message);
    }
}
