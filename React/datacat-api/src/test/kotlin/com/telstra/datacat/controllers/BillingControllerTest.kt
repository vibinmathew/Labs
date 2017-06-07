package com.telstra.datacat.controllers

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.telstra.datacat.domain.Billing
import com.telstra.datacat.domain.DataIdentifier
import com.telstra.datacat.domain.gateways.BillingGateway
import com.telstra.datacat.domain.services.DataIdentifierService
import io.restassured.module.mockmvc.RestAssuredMockMvc
import io.restassured.module.mockmvc.RestAssuredMockMvc.`when`
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.equalTo
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME


class BillingControllerTest {

    val billingGateway: BillingGateway = mock()
    val dataIdentifierService: DataIdentifierService = mock()

    @Before
    fun setup() {
        RestAssuredMockMvc.standaloneSetup(BillingController(billingGateway, dataIdentifierService))
    }

    @Test
    fun billingEndpoint() {

        val date = LocalDate.parse("2017-02-27")

        whenever(dataIdentifierService.loadFromToken("token123")).thenReturn(DataIdentifier("token123", "0298453212", date))

        whenever(billingGateway.billing("0298453212", date, 0, "token123"))
                .thenReturn(Billing(BigInteger("5600000"),
                        BigInteger("7000000"),
                        BigDecimal("49.95"),
                        LocalDate.parse("2017-02-16"),
                        LocalDate.parse("2017-03-15"),
                        ZonedDateTime.parse("2017-03-14T08:50Z", ISO_ZONED_DATE_TIME),
                        "token123/1",
                        "token123/-1"))

        `when`()
                .get("/api/billing/token123")
                .then()
                .body("usage", equalTo(5600000))
                .body("allowance", equalTo(7000000))
                .body("start", equalTo("2017-02-16"))
                .body("end", equalTo("2017-03-15"))
                .body("updatedAt", equalTo("2017-03-14T08:50Z"))
                .body("nextBillingCycleUrl", equalTo("token123/1"))
                .body("previousBillingCycleUrl", equalTo("token123/-1"))
                .body("baseBill", `is`(49.95f))

        verify(billingGateway).billing("0298453212", date, 0, "token123")
    }

    @Test
    fun billingEndpoint_shouldHandleIndexes() {

        val date = LocalDate.parse("2017-02-27")

        whenever(dataIdentifierService.loadFromToken("token123")).thenReturn(DataIdentifier("token123", "0298453212", date))

        whenever(billingGateway.billing("0298453212", date, 1, "token123"))
                .thenReturn(Billing(BigInteger("5600000"),
                        BigInteger("7000000"),
                        BigDecimal("49.95"),
                        LocalDate.parse("2017-02-16"),
                        LocalDate.parse("2017-03-15"),
                        ZonedDateTime.parse("2017-03-14T08:50Z", ISO_ZONED_DATE_TIME),
                        "token123/2",
                        "token123/0"))

        `when`()
                .get("/api/billing/token123/1")
                .then()
                .statusCode(200)

        verify(billingGateway).billing("0298453212", date, 1, "token123")
    }
}
