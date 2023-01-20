package com.claudio.mensageria.rabbitmq.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class QueueController {
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public QueueController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/")
    public String oi() {
        return "oi";
    }

    @PostMapping("/{numberOfMessages}")
    public String sendMessagesToQueue(@PathVariable Long numberOfMessages, @RequestBody String body) {
        for (int i = 0; i < numberOfMessages; i++) {
            rabbitTemplate.convertAndSend("jogos", body);
        }
        return "Messages sended.";
    }
}
