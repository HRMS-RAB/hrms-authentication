package com.hrms.auth.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Exposes queue names for SpEL - "#{rabbitConfig.employeeCreatedQueue}"
 * and wires the employee queue / DLQ.
 */
@EnableRabbit
@Configuration("rabbitConfig")          // ← registers bean with that exact name
public class RabbitMQConfig {

    /* ───────── values from application.properties ───────── */

    @Value("${hrms.rabbitmq.employee.exchange}")
    private String employeeExchange;

    @Value("${hrms.rabbitmq.employee.queue}")
    private String employeeCreatedQueue;

    @Value("${hrms.rabbitmq.employee.routing-key}")
    private String employeeRoutingKey;

    /* ───────── getter used by SpEL ───────── */

    public String getEmployeeCreatedQueue() {
        return employeeCreatedQueue;
    }

    /* ───────── AMQP infrastructure ───────── */

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(employeeExchange, true, false);
    }

    @Bean
    public Queue employeeQueue() {
        return QueueBuilder.durable(employeeCreatedQueue)
                .withArgument("x-dead-letter-exchange", employeeExchange)
                .withArgument("x-dead-letter-routing-key", "employee.dlq")
                .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(employeeCreatedQueue + ".dlq").build();
    }

    @Bean
    public Binding binding(Queue employeeQueue, DirectExchange exchange) {
        return BindingBuilder.bind(employeeQueue)
                             .to(exchange)
                             .with(employeeRoutingKey);
    }

    @Bean
    public Binding dlqBinding(Queue deadLetterQueue, DirectExchange exchange) {
        return BindingBuilder.bind(deadLetterQueue)
                             .to(exchange)
                             .with("employee.dlq");
    }

    /* ───────── JSON converter & listener factory ───────── */

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory cf,
            Jackson2JsonMessageConverter converter
    ) {
        SimpleRabbitListenerContainerFactory f = new SimpleRabbitListenerContainerFactory();
        f.setConnectionFactory(cf);
        f.setMessageConverter(converter);
        f.setDefaultRequeueRejected(false);   // send failed msgs to DLQ
        return f;
    }

    /* ───────── Optional template for publishing ───────── */

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf,
                                         Jackson2JsonMessageConverter converter) {
        RabbitTemplate rt = new RabbitTemplate(cf);
        rt.setMessageConverter(converter);
        return rt;
    }
}


/*package com.hrms.auth.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitMQConfig {

    @Value("${hrms.rabbitmq.employee.exchange}")
    private String employeeExchange;

    @Value("${hrms.rabbitmq.employee.queue}")
    private String employeeQueue;

    @Value("${hrms.rabbitmq.employee.routing-key}")
    private String employeeRoutingKey;   // e.g. "employee.*" (topic) or "employee.created" (direct)

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(employeeExchange, true, false);
    }

//    @Bean
  //  public Queue employeeQueue() {
    //    return QueueBuilder.durable(employeeQueue).build();
    //}
//added from here to avoid app crash instead of the above 
    @Bean
    public Queue queue() {
        return QueueBuilder.durable(employeeQueue)
            .withArgument("x-dead-letter-exchange", employeeExchange)
            .withArgument("x-dead-letter-routing-key", "employee.dlq")
            .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable("hrms.employee.dlq").build();
    }

    @Bean
    public Binding dlqBinding(Queue deadLetterQueue, TopicExchange exchange) {
        return BindingBuilder
            .bind(deadLetterQueue)
            .to(exchange)
            .with("employee.dlq");
    }
// added till here 
    
    @Bean
    public Binding binding(Queue employeeQueue, TopicExchange exchange) {
        return BindingBuilder.bind(employeeQueue).to(exchange).with(employeeRoutingKey);
    }

    // Optional – only needed if this service ever publishes 
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf) {
        RabbitTemplate rt = new RabbitTemplate(cf);
        rt.setMessageConverter(messageConverter());
        return rt;
    }
}*/
