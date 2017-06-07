package com.telstra.datacat.adapters.external


import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Profile("!internal")
@Configuration
open class ExternalRabbitConfiguration {

    @Bean
    fun listenerAdapter(smsReceiver: ExternalSmsReceiver): MessageListenerAdapter {
        return MessageListenerAdapter(smsReceiver, "receiveMessage")
    }
}