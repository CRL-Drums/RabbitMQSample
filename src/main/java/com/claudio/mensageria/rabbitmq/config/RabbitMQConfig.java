package com.claudio.mensageria.rabbitmq.config;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConfig {
    private final AmqpAdmin amqpAdmin;

    @Value("${exchange.games}")
    private String exchangeGamesName;

    @Value("${queue.games}")
    private String queueGamesName;

    public RabbitMQConfig(AmqpAdmin amqpAdmin) {
        this.amqpAdmin = amqpAdmin;
    }

    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    private Queue queueBuilder(String nomeDaFila) {
        return new Queue(nomeDaFila, true, false, false);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory listenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory =  new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setDefaultRequeueRejected(false);
        factory.setStartConsumerMinInterval(1L);
        factory.setMaxConcurrentConsumers(60);
        return factory;
    }

    @PostConstruct
    private void addQueues() {
        Queue jogos = QueueBuilder.durable(queueGamesName)
                .withArgument("x-dead-letter-exchange", "deadLetterExchange")
                .withArgument("x-dead-letter-routing-key", "deadLetterGames")
                .build();

        Queue jogosDlq = queueBuilder(queueGamesName + ".dlq");

        DirectExchange jogosExchange = new DirectExchange(exchangeGamesName);
        DirectExchange jogosDeadLetterExchange = new DirectExchange(exchangeGamesName + ".dlx");

        this.amqpAdmin.declareExchange(jogosExchange);
        this.amqpAdmin.declareExchange(jogosDeadLetterExchange);

        this.amqpAdmin.declareQueue(jogos);
        this.amqpAdmin.declareBinding(BindingBuilder.bind(jogos).to(jogosExchange).with("games"));
        this.amqpAdmin.declareQueue(jogosDlq);
        this.amqpAdmin.declareBinding(BindingBuilder.bind(jogosDlq).to(jogosDeadLetterExchange)
                .with("deadLetterJogos"));
    }
}
