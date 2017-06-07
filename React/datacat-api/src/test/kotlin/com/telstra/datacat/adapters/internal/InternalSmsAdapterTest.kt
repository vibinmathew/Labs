package com.telstra.datacat.adapters.internal

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.telstra.datacat.adapters.internal.InternalSmsRequest.ContentParameter
import com.telstra.datacat.adapters.internal.InternalSmsRequest.Recipient
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.client.OAuth2RestTemplate
import org.springframework.web.client.RestClientException
import java.util.Arrays.asList

class InternalSmsAdapterTest {

    val restTemplate: OAuth2RestTemplate = mock()

    val adapter = InternalSmsAdapter(restTemplate, "sms-url")

    val request = InternalSmsRequest(
            "B2BSMSP_PILOT_1",
            "NOT_APPLICABLE",
            Recipient("321654987"),
            asList(ContentParameter("data_type_1", "Streaming Video & Audio"),
                    ContentParameter("data_type_2", "Social Media"),
                    ContentParameter("data_type_3", "Gaming"),
                    ContentParameter("data_type_4", "File Sharing"),
                    ContentParameter("data_type_5", "Others")))

    @Before
    fun setup() {
        reset(restTemplate)
    }

    @Test
    fun send_returnsTrueWhenSuccessfullySend() {
        whenever(restTemplate.postForEntity("sms-url", request, String::class.java))
                .thenReturn(ResponseEntity<String>("", HttpStatus.ACCEPTED))

        assertThat(adapter.send(request)).isTrue()

        verify(restTemplate).postForEntity("sms-url", request, String::class.java)
    }

    @Test
    fun send_returnsFalseWhenErrorOccurs() {
        whenever(restTemplate.postForEntity("sms-url", request, String::class.java))
                .thenThrow(RestClientException("oops"))

        assertThat(adapter.send(request)).isFalse()
    }

    @Test
    fun send_returnsFalseWhenDownstreamReturnsError() {
        whenever(restTemplate.postForEntity("sms-url", request, String::class.java))
                .thenReturn(ResponseEntity<String>("foo", HttpStatus.BAD_REQUEST))

        assertThat(adapter.send(request)).isFalse()
    }
}