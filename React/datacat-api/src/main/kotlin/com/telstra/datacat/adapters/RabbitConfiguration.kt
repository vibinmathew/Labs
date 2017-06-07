package com.telstra.datacat.adapters


import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
open class RabbitConfiguration {

    companion object {
        val TOPIC = "datacat-exchange"
        val SMS_QUEUE_NAME = "send-sms"
        val SMS_HOLDING_QUEUE_NAME = "send-sms-holding"
        val MESSAGING_QUEUE_NAME = "generate-message"
        val MESSAGING_HOLDING_QUEUE_NAME = "generate-message-holding"
    }

    @Bean
    fun smsQueue(): Queue {
        return Queue(SMS_QUEUE_NAME, false)
    }

    @Bean
    fun smsHoldingQueue(@Value("\${rabbitmq.send-sms.retry-holding-time}") retryHoldingTime: Int): Queue {
        val args = HashMap<String, Any>()
        args.put("x-dead-letter-exchange", TOPIC)
        args.put("x-message-ttl", retryHoldingTime)
        args.put("x-dead-letter-routing-key", SMS_QUEUE_NAME)

        return Queue(SMS_HOLDING_QUEUE_NAME, false, false, false, args)
    }

    @Bean
    fun messageQueue(): Queue {
        return Queue(MESSAGING_QUEUE_NAME, false)
    }

    @Bean
    fun messageHoldingQueue(@Value("\${rabbitmq.send-sms.retry-holding-time}") retryHoldingTime: Int): Queue {
        val args = HashMap<String, Any>()
        args.put("x-dead-letter-exchange", TOPIC)
        args.put("x-message-ttl", retryHoldingTime)
        args.put("x-dead-letter-routing-key", MESSAGING_QUEUE_NAME)

        return Queue(MESSAGING_HOLDING_QUEUE_NAME, false, false, false, args)
    }

    @Bean
    fun exchange(): TopicExchange {
        return TopicExchange(TOPIC)
    }

    @Bean
    fun bindings(exchange: TopicExchange,
                 smsQueue: Queue,
                 smsHoldingQueue: Queue,
                 messageQueue: Queue,
                 messageHoldingQueue: Queue): List<Binding> {

        return Arrays.asList(
                BindingBuilder.bind(smsQueue).to(exchange).with(SMS_QUEUE_NAME),
                BindingBuilder.bind(smsHoldingQueue).to(exchange).with(SMS_HOLDING_QUEUE_NAME),
                BindingBuilder.bind(messageQueue).to(exchange).with(MESSAGING_QUEUE_NAME),
                BindingBuilder.bind(messageHoldingQueue).to(exchange).with(MESSAGING_HOLDING_QUEUE_NAME)
        )
    }

    @Bean
    fun internalContainer(connectionFactory: ConnectionFactory,
                          listenerAdapter: MessageListenerAdapter): SimpleMessageListenerContainer {
        val container = SimpleMessageListenerContainer()

        container.connectionFactory = connectionFactory
        container.setQueueNames(SMS_QUEUE_NAME, MESSAGING_QUEUE_NAME)
        container.messageListener = listenerAdapter
        return container
    }
}