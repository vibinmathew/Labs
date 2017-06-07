package com.telstra.datacat.adapters.external

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.Matchers.eq
import org.springframework.security.oauth2.client.OAuth2RestTemplate
import org.springframework.web.client.RestClientException

class ExternalSmsAdapterTest {

    val mockRestTemplate: OAuth2RestTemplate = mock()

    val adapter: ExternalSmsAdapter = ExternalSmsAdapter(mockRestTemplate, "sms-url")

    @Test
    fun send_returnsTrueWhenSuccessfullySend() {
        val send = adapter.send(ExternalSmsRequest("0400000000", "Hello from Data Cat"))
        assertThat(send).isTrue()
        val request = ExternalSmsRequest("0400000000", "Hello from Data Cat")
        verify(mockRestTemplate).postForObject(eq("sms-url"),
                eq(request), eq(ExternalSmsResponse::class.java))
    }

    @Test
    fun send_returnsFalseWhenFailedToSend() {
        val request = ExternalSmsRequest("0400000000", "Hello from Data Cat")
        whenever(mockRestTemplate.postForObject("sms-url",
                request, ExternalSmsResponse::class.java)).thenThrow(RestClientException("oops"))
        val send = adapter.send(ExternalSmsRequest("0400000000", "Hello from Data Cat"))
        assertThat(send).isFalse()
    }
}