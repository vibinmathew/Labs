package com.telstra.datacat.adapters.internal

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
import java.util.*

class InternalSmsReceiverTest {

    val smsGateway: SmsGateway<InternalSmsRequest> = mock()
    val messageGenerator: InternalSmsMessageGenerator = mock()
    val rabbitTemplate: RabbitTemplate = mock()
    val smsReciever = InternalSmsReceiver(smsGateway, messageGenerator, rabbitTemplate, 4)

    val request = InternalSmsRequest(
            "B2BSMSP_PILOT_1",
            "NOT_APPLICABLE",
            InternalSmsRequest.Recipient("321654987"),
            Arrays.asList(InternalSmsRequest.ContentParameter("data_type_1", "Streaming Video & Audio"),
                    InternalSmsRequest.ContentParameter("data_type_2", "Social Media"),
                    InternalSmsRequest.ContentParameter("data_type_3", "Gaming"),
                    InternalSmsRequest.ContentParameter("data_type_4", "File Sharing"),
                    InternalSmsRequest.ContentParameter("data_type_5", "Others")))

    @Before
    fun before() {
        reset(smsGateway)
        reset(messageGenerator)
        reset(rabbitTemplate)
    }

    @Test
    fun receiveMessage_SmsMessage_shouldGeneratemessageAndSendToExternalQueueMessage() {
        whenever(messageGenerator.generate("0400000001")).thenReturn(request)

        smsReciever.receiveMessage(SmsMessage("0400000001"))

        verify(messageGenerator).generate("0400000001")
        verify(rabbitTemplate).convertAndSend("datacat-exchange", "send-sms", InternalQueueMessage(request))
    }

    @Test
    fun receiveMessage_ExternalQueueMessage_shouldSendThrowSmsGateway() {
        smsReciever.receiveMessage(InternalQueueMessage(request))

        verify(smsGateway).send(request)
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
        whenever(smsGateway.send(request)).thenThrow(RuntimeException("Hello"))

        smsReciever.receiveMessage(InternalQueueMessage(request))

        verify(smsGateway).send(request)
        verify(rabbitTemplate).convertAndSend("datacat-exchange", "send-sms-holding", InternalQueueMessage(request, 1))
    }

    @Test
    fun receiveMessage_SmsMessage_shouldDiscardMessageWhenReachingMaxRetries() {
        whenever(messageGenerator.generate("0400000001")).thenReturn(request)

        smsReciever.receiveMessage(SmsMessage("0400000001", 4))

        verify(messageGenerator).generate("0400000001")
        verify(rabbitTemplate, never()).convertAndSend("datacat-exchange", "generate-message-holding", SmsMessage("0400000001", 5))
    }

    @Test
    fun receiveMessage_ExternalQueueMessage_shouldDiscardMessageWhenReachingMaxRetries() {
        whenever(smsGateway.send(request)).thenThrow(RuntimeException("Hello"))

        smsReciever.receiveMessage(InternalQueueMessage(request, 4))

        verify(smsGateway).send(request)
        verify(rabbitTemplate, never()).convertAndSend("datacat-exchange", "send-sms-holding", InternalQueueMessage(request, 5))
    }
}


