package com.ren.administrator.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class RabbitConfig {

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("adminExchange");
    }

    @Bean("queue_PartTime")
    public Queue queuePartTime() {
        return new Queue("queue_PartTime", true);
    }

    @Bean("queue_FullTime")
    public Queue queueFullTime() {
        return new Queue("queue_FullTime", true);
    }

    @Bean("queue_Manager")
    public Queue queueManager() {
        return new Queue("queue_Manager", true);
    }

    @Bean("queue_Boss")
    public Queue queueBoss() {
        return new Queue("queue_Boss", true);
    }

    @Bean
    public Binding bindingParTime(@Qualifier("queue_PartTime") Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("1");
    }

    @Bean
    public Binding bindingFullTime(@Qualifier("queue_FullTime") Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("2");
    }

    @Bean
    public Binding bindingManager(@Qualifier("queue_Manager") Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("3");
    }

    @Bean
    public Binding bindingBoss(@Qualifier("queue_Boss") Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("4");
    }
}
