package com.claudio.mensageria.rabbitmq.config;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConfig {
    private final AmqpAdmin amqpAdmin;

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
        factory.setMaxConcurrentConsumers(15);
        return factory;
    }

    @PostConstruct
    private void adicionaFilas() {
        Queue jogos = QueueBuilder.durable("jogos")
                .withArgument("x-dead-letter-exchange", "deadLetterExchange")
                .withArgument("x-dead-letter-routing-key", "deadLetterJogos")
                .build();

        Queue jogosDlq = queueBuilder("jogosDlq");

        DirectExchange jogosExchange = new DirectExchange("jogosExchange");
        DirectExchange jogosDeadLetterExchange = new DirectExchange("jogosDlx");

        this.amqpAdmin.declareExchange(jogosExchange);
        this.amqpAdmin.declareExchange(jogosDeadLetterExchange);

        this.amqpAdmin.declareQueue(jogos);
        this.amqpAdmin.declareBinding(BindingBuilder.bind(jogos).to(jogosExchange).with("jogos"));
        this.amqpAdmin.declareQueue(jogosDlq);
        this.amqpAdmin.declareBinding(BindingBuilder.bind(jogosDlq).to(jogosDeadLetterExchange)
                .with("deadLetterJogos"));
    }
}
