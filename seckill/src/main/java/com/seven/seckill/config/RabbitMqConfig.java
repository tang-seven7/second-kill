package com.seven.seckill.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    public static final String QUEUE_ORDER = "order_queue";
    public static final String EXCHANGE_ORDER = "order_exchange";

    @Bean("queue_order")
    public Queue orderQueue(){
        return QueueBuilder.durable(QUEUE_ORDER).build();
    }
    @Bean("exchange_order")
    public DirectExchange orderExchange() {
        return new DirectExchange(EXCHANGE_ORDER);
    }

    //声明队列绑定交换机
    //直接交换机，路由匹配模式
    @Bean
    public Binding orderQueueBinding(@Qualifier("queue_order") Queue queue_order,
                                @Qualifier("exchange_order") DirectExchange exchange_order) {
        return BindingBuilder.bind(queue_order).to(exchange_order).with("order_route");
    }
}
