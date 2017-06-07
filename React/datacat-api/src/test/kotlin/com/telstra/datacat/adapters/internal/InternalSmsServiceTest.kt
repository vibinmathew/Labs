package com.telstra.datacat.adapters.internal

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.telstra.datacat.domain.gateways.SmsGateway
import com.telstra.datacat.domain.services.MessageGenerator
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import java.util.*

class InternalSmsServiceTest {

    val smsGateway: SmsGateway<InternalSmsRequest> = mock()
    val messageGenerator: MessageGenerator<InternalSmsRequest> = mock()

    val smsService = InternalSmsService(smsGateway, messageGenerator)

    @Before
    fun setup() {
        reset(smsGateway)
        reset(messageGenerator)
    }

    @Test
    fun send_returnsTrueWhenSuccessfulySend() {
        val message = InternalSmsRequest("dkd", "type", InternalSmsRequest.Recipient("39393"), Collections.emptyList())
        whenever(messageGenerator.generate("0400000000")).thenReturn(message)
        whenever(smsGateway.send(message)).thenReturn(true)

        val result = smsService.send("0400000000")

        Assertions.assertThat(result).isTrue()
        verify(smsGateway).send(message)
        verify(messageGenerator).generate("0400000000")
    }

    @Test
    fun send_returnsFalseWhenFailedToSend() {
        val message = InternalSmsRequest("dkd", "type", InternalSmsRequest.Recipient("39393"), Collections.emptyList())
        whenever(messageGenerator.generate("0400000001")).thenReturn(message)
        whenever(smsGateway.send(message)).thenReturn(false)

        Assertions.assertThat(smsService.send("0400000001")).isFalse()

        verify(smsGateway).send(message)
        verify(messageGenerator).generate("0400000001")
    }
}
