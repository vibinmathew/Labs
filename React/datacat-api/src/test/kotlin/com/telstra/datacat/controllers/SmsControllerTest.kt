package com.telstra.datacat.controllers

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import com.telstra.datacat.domain.SmsMessage
import com.telstra.datacat.domain.gateways.MsisdnFilterGateway
import com.telstra.datacat.domain.gateways.UsageNotificationGateway
import com.telstra.datacat.domain.services.SmsService
import io.restassured.module.mockmvc.RestAssuredMockMvc
import io.restassured.module.mockmvc.RestAssuredMockMvc.`when`
import org.junit.Before
import org.junit.Test
import org.springframework.amqp.rabbit.core.RabbitTemplate
import java.util.*


class SmsControllerTest {

    val smsService: SmsService = mock()
    val usageNotificationGateway: UsageNotificationGateway = mock()
    val msisdnFilterGateway: MsisdnFilterGateway = mock()
    val rabbitTemplate: RabbitTemplate = mock()

    @Before
    fun before() {
        RestAssuredMockMvc.standaloneSetup(SmsController(smsService, usageNotificationGateway, msisdnFilterGateway, rabbitTemplate))
        reset(smsService)
    }

    @Test
    fun singleSmsShouldReturnOK() {
        whenever(smsService.send("61400000001")).thenReturn(true)

        `when`()
                .post("/api/sms/61400000001")
                .then()
                .statusCode(200)

        verify(smsService).send("61400000001")

    }

    @Test
    fun singleSmsShouldReturnNotFoundForInvalidNumber() {
        `when`()
                .post("/api/sms/0400000001")
                .then()
                .statusCode(404)

        verifyZeroInteractions(smsService)
    }

    @Test
    fun singleSmsShould502WhenUnableToSend() {
        whenever(smsService.send("61400000000")).thenReturn(false)

        `when`()
                .post("/api/sms/61400000000")
                .then()
                .statusCode(502)

        verify(smsService).send("61400000000")
    }

    @Test
    fun dataUsageEndPointShouldReturnOK() {
        whenever(usageNotificationGateway.msisdnToNotify()).thenReturn(Arrays.asList("0303030303", "0404040404", "0505050505"))
        whenever(msisdnFilterGateway.check("0303030303")).thenReturn(true)
        whenever(msisdnFilterGateway.check("0404040404")).thenReturn(true)
        whenever(msisdnFilterGateway.check("0505050505")).thenReturn(false)

        `when`()
                .get("/api/sms")
                .then()
                .statusCode(200)

        verify(usageNotificationGateway).msisdnToNotify()
        verify(rabbitTemplate).convertAndSend("datacat-exchange", "generate-message", SmsMessage("0303030303"))
        verify(rabbitTemplate).convertAndSend("datacat-exchange", "generate-message", SmsMessage("0404040404"))
        verify(rabbitTemplate, never()).convertAndSend("datacat-exchange", "generate-message", SmsMessage("0505050505"))
    }
}
