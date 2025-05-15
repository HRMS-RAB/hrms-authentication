package com.hrms.auth.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitMQConfig {

    /* — same values as producer — */
    private static final String EXCHANGE   = "hrms.employee.exchange";
    private static final String QUEUE      = "hrms.employee.queue";
    private static final String ROUTINGKEY = "employee.created";

    @Bean DirectExchange employeeExchange() { return new DirectExchange(EXCHANGE, true, false); }
    @Bean Queue employeeQueue()            { return QueueBuilder.durable(QUEUE).build();       }
    @Bean Binding employeeBinding(DirectExchange e, Queue q) {
        return BindingBuilder.bind(q).to(e).with(ROUTINGKEY);
    }

    /* — listener container uses the same JSON converter — */
    @Bean Jackson2JsonMessageConverter jacksonConverter() { return new Jackson2JsonMessageConverter(); }

    @Bean SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory cf, Jackson2JsonMessageConverter conv) {
        SimpleRabbitListenerContainerFactory f = new SimpleRabbitListenerContainerFactory();
        f.setConnectionFactory(cf);
        f.setMessageConverter(conv);
        f.setConcurrentConsumers(2);
        return f;
    }
}
