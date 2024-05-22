package com.ren.monitor.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    /**
     * 使用Header交換器，用於根據職位來配發消息到佇列
     *
     * @return 返回Header交換器
     */
    @Bean
    public HeadersExchange headersExchange() {
        return new HeadersExchange("adminHeadersExchange");
    }

    /**
     * 建立消息傳遞佇列
     *
     * @return 給所有員工的佇列
     */
    @Bean("queue_Employee")
    public Queue queueEmployee() {
        return new Queue("queue_Employee", true);
    }

    /**
     * 建立消息傳遞佇列
     *
     * @return 給全職員工的佇列
     */
    @Bean("queue_FullTime")
    public Queue queueFullTime() {
        return new Queue("queue_FullTime", true);
    }

    /**
     * 建立消息傳遞佇列
     *
     * @return 給經理的佇列
     */
    @Bean("queue_Manager")
    public Queue queueManager() {
        return new Queue("queue_Manager", true);
    }

    /**
     * 建立消息傳遞佇列
     *
     * @return 給老闆的佇列
     */
    @Bean("queue_Boss")
    public Queue queueBoss() {
        return new Queue("queue_Boss", true);
    }

    /**
     * 將給員工的佇列透過消息內Header與Header交換器綁定，
     * 綁定方式為透過消息的Header綁定，Header為一組Key-Value
     * 其中Header exchange可以透過消息的Header綁定路由:
     * 1.where:單一鍵符合
     * 2.whereAny:複數鍵中其中一個符合
     * 3.whereAll:複數鍵都符合
     * Value的部分又有:
     * 1.matches:完美匹配(支援正則表達式寫法)
     * 2.notMatches:不匹配指定值
     * 另外還有
     * 1.exists:key存在
     * 2.doesNotExist:key不存在
     *
     * 此佇列用法為指綁定員工
     *
     * @param queue 員工佇列
     * @param exchange Header交換器
     * @return 返回綁定物件
     */
    @Bean
    public Binding bindingEmployee(@Qualifier("queue_Employee") Queue queue, HeadersExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).where("title").matches("employee");
    }


    @Bean
    public Binding bindingFullTime1(@Qualifier("queue_FullTime") Queue queue, HeadersExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).where("title").matches("employee");
    }

    @Bean
    public Binding bindingFullTime2(@Qualifier("queue_FullTime") Queue queue, HeadersExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).where("title").matches("fullTime");
    }

    @Bean
    public Binding bindingManager1(@Qualifier("queue_Manager") Queue queue, HeadersExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).where("title").matches("employee");
    }

    @Bean
    public Binding bindingManager2(@Qualifier("queue_Manager") Queue queue, HeadersExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).where("title").matches("fullTime");
    }

    @Bean
    public Binding bindingManager3(@Qualifier("queue_Manager") Queue queue, HeadersExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).where("title").matches("manager");
    }

    @Bean
    public Binding bindingBoss1(@Qualifier("queue_Boss") Queue queue, HeadersExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).where("title").matches("employee");
    }

    @Bean
    public Binding bindingBoss2(@Qualifier("queue_Boss") Queue queue, HeadersExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).where("title").matches("fullTime");
    }

    @Bean
    public Binding bindingBoss3(@Qualifier("queue_Boss") Queue queue, HeadersExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).where("title").matches("manager");
    }

    @Bean
    public Binding bindingBoss4(@Qualifier("queue_Boss") Queue queue, HeadersExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).where("title").matches("boss");
    }
}
