/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.pandamaster.sample.spring.amqp.web.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author yhida
 */
@Configuration
public class AmqpConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(AmqpConfig.class);

    @Bean
    public ConnectionFactory sampleConnectionFactory() {
        
        CachingConnectionFactory connectionFactory =  new CachingConnectionFactory("localhost");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        return connectionFactory;
    }

    @Bean
    public AmqpAdmin sampleAmqpAdmin() {
        return new RabbitAdmin(sampleConnectionFactory());
    }

    @Bean
    public RabbitTemplate sampleRabbitTemplate() {
        return new RabbitTemplate(sampleConnectionFactory());
    }

    @Bean
    public DirectExchange sampleDirect() {
        return new DirectExchange("sample");
    }
}
