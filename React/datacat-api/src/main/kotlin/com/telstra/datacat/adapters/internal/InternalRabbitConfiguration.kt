package com.telstra.datacat.adapters.internal


import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Profile("internal")
@Configuration
open class InternalRabbitConfiguration {

    @Bean
    fun listenerAdapter(smsReceiver: InternalSmsReceiver): MessageListenerAdapter {
        return MessageListenerAdapter(smsReceiver, "receiveMessage")
    }
}