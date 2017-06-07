package com.telstra.datacat.adapters.internal

import com.telstra.datacat.adapters.RabbitConfiguration
import com.telstra.datacat.domain.SmsMessage
import com.telstra.datacat.domain.gateways.SmsGateway
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Profile("internal")
@Component
class InternalSmsReceiver(val interSmsGateway: SmsGateway<InternalSmsRequest>,
                          val internalMessageGenerator: InternalSmsMessageGenerator,
                          val rabbitTemplate: RabbitTemplate,
                          @Value("\${rabbitmq.send-sms.retry-max}") val retryMax: Int) {

    val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    fun receiveMessage(smsMessage: SmsMessage) {
        try {
            val content = internalMessageGenerator.generate(smsMessage.msisdn)
            logger.debug("Queueing $content")
            rabbitTemplate.convertAndSend(RabbitConfiguration.TOPIC, RabbitConfiguration.SMS_QUEUE_NAME,
                    InternalQueueMessage(content))
        } catch(e: Exception) {
            logger.error("Issue when generating SMS message = $e")
            if (smsMessage.retryCounter < retryMax) {
                rabbitTemplate.convertAndSend(RabbitConfiguration.TOPIC, RabbitConfiguration.MESSAGING_HOLDING_QUEUE_NAME,
                        SmsMessage(smsMessage.msisdn, smsMessage.retryCounter + 1))
            }
        }
    }

    fun receiveMessage(smsMessage: InternalQueueMessage) {
        try {
            logger.debug("Sending $smsMessage")
            interSmsGateway.send(smsMessage.internalSmsRequest)
        } catch(e: Exception) {
            logger.error("Issue when sending SMS = $e")
            if (smsMessage.retryCounter < retryMax) {
                rabbitTemplate.convertAndSend(RabbitConfiguration.TOPIC, RabbitConfiguration.SMS_HOLDING_QUEUE_NAME,
                        InternalQueueMessage(smsMessage.internalSmsRequest, smsMessage.retryCounter + 1))
            }
        }
    }

    fun receiveMessage(smsMessage: Any) {
        logger.error("Unknown message = $smsMessage")
    }
}
