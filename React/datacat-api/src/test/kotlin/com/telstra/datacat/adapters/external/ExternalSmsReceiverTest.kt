package com.telstra.datacat.adapters.external

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.telstra.datacat.domain.SmsMessage
import com.telstra.datacat.domain.gateways.SmsGateway
import org.junit.Before
import org.junit.Test
import org.springframework.amqp.rabbit.core.RabbitTemplate

class ExternalSmsReceiverTest {

    val smsGateway: SmsGateway<ExternalSmsRequest> = mock()
    val messageGenerator: ExternalMessageGenerator = mock()
    val rabbitTemplate: RabbitTemplate = mock()
    val smsReciever = ExternalSmsReceiver(smsGateway, messageGenerator, rabbitTemplate, 4)

    @Before
    fun before() {
        reset(smsGateway)
        reset(messageGenerator)
        reset(rabbitTemplate)
    }

    @Test
    fun receiveMessage_SmsMessage_shouldGeneratemessageAndSendToExternalQueueMessage() {
        whenever(messageGenerator.generate("0400000001")).thenReturn(ExternalSmsRequest("0400000001", "Hello"))

        smsReciever.receiveMessage(SmsMessage("0400000001"))

        verify(messageGenerator).generate("0400000001")
        verify(rabbitTemplate).convertAndSend("datacat-exchange", "send-sms", ExternalQueueMessage(ExternalSmsRequest("0400000001", "Hello")))
    }

    @Test
    fun receiveMessage_ExternalQueueMessage_shouldSendThrowSmsGateway() {
        whenever(smsGateway.send(ExternalSmsRequest("0400000001", "Hello"))).thenReturn(true)

        smsReciever.receiveMessage(ExternalQueueMessage(ExternalSmsRequest("0400000001", "Hello")))

        verify(smsGateway).send(ExternalSmsRequest("0400000001", "Hello"))
        verify(rabbitTemplate, never()).convertAndSend("datacat-exchange", "send-sms-holding", "0400000001")
    }

    @Test
    fun receiveMessage_SmsMessage_shouldSendToHoldingQueueWhenFailing() {
        whenever(messageGenerator.generate("0400000001")).thenThrow(RuntimeException("Hello"))

        smsReciever.receiveMessage(SmsMessage("0400000001"))

        verify(messageGenerator).generate("0400000001")
        verify(rabbitTemplate).convertAndSend("datacat-exchange", "generate-message-holding", SmsMessage("0400000001", 1))
    }

    @Test
    fun receiveMessage_ExternalQueueMessage_shouldSendToHoldingQueueWhenFailing() {
        whenever(smsGateway.send(ExternalSmsRequest("0400000001", "Hello"))).thenThrow(RuntimeException("Hello"))

        smsReciever.receiveMessage(ExternalQueueMessage(ExternalSmsRequest("0400000001", "Hello")))

        verify(smsGateway).send(ExternalSmsRequest("0400000001", "Hello"))
        verify(rabbitTemplate).convertAndSend("datacat-exchange", "send-sms-holding", ExternalQueueMessage(ExternalSmsRequest("0400000001", "Hello"), 1))
    }

    @Test
    fun receiveMessage_SmsMessage_shouldDiscardMessageWhenReachingMaxRetries() {
        whenever(messageGenerator.generate("0400000001")).thenReturn(ExternalSmsRequest("0400000001", "Hello"))

        smsReciever.receiveMessage(SmsMessage("0400000001", 4))

        verify(messageGenerator).generate("0400000001")
        verify(rabbitTemplate, never()).convertAndSend("datacat-exchange", "generate-message-holding", SmsMessage("0400000001", 5))
    }

    @Test
    fun receiveMessage_ExternalQueueMessage_shouldDiscardMessageWhenReachingMaxRetries() {
        whenever(smsGateway.send(ExternalSmsRequest("0400000001", "Hello"))).thenThrow(RuntimeException("Hello"))

        smsReciever.receiveMessage(ExternalQueueMessage(ExternalSmsRequest("0400000001", "Hello"), 4))

        verify(smsGateway).send(ExternalSmsRequest("0400000001", "Hello"))
        verify(rabbitTemplate, never()).convertAndSend("datacat-exchange", "send-sms-holding", ExternalQueueMessage(ExternalSmsRequest("0400000001", "Hello"), 5))
    }
}


