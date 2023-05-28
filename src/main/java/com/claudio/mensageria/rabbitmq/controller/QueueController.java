package com.claudio.mensageria.rabbitmq.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("publisher")
public class QueueController {
    private final RabbitTemplate rabbitTemplate;

    @Value("${exchange.games}")
    private String exchangeGamesName;


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
            this.rabbitTemplate.convertAndSend(exchangeGamesName, "games" , body);
        }
        return "Messages sended.";
    }
}
