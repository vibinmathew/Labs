package com.telstra.datacat.adapters.external

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.telstra.datacat.domain.gateways.SmsGateway
import com.telstra.datacat.domain.services.MessageGenerator
import org.apache.commons.lang3.RandomStringUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test


class ExternalSmsServiceTest {

    val smsGateway: SmsGateway<ExternalSmsRequest> = mock()
    val messageGenerator: MessageGenerator<ExternalSmsRequest> = mock()

    val smsService = ExternalSmsService(smsGateway, messageGenerator)

    @Before
    fun setup() {
        reset(smsGateway)
        reset(messageGenerator)
    }

    @Test
    fun send_returnsTrueWhenSuccessfullySend() {
        val message = RandomStringUtils.randomAlphabetic(30)
        whenever(messageGenerator.generate("0400000000")).thenReturn(ExternalSmsRequest("0400000000", message))
        whenever(smsGateway.send(ExternalSmsRequest("0400000000", message))).thenReturn(true)

        val result = smsService.send("0400000000")

        assertThat(result).isTrue()
        verify(smsGateway).send(ExternalSmsRequest("0400000000", message))
        verify(messageGenerator).generate("0400000000")
    }

    @Test
    fun send_returnsFalseWhenFailedToSend() {
        val message = RandomStringUtils.randomAlphabetic(30)
        whenever(messageGenerator.generate("0400000001")).thenReturn(ExternalSmsRequest("0400000000", message))
        whenever(smsGateway.send(ExternalSmsRequest("0400000000", message))).thenReturn(false)

        assertThat(smsService.send("0400000001")).isFalse()

        verify(smsGateway).send(ExternalSmsRequest("0400000000", message))
        verify(messageGenerator).generate("0400000001")
    }
}
