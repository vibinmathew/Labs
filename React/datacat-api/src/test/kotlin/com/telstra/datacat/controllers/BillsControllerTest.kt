package com.telstra.datacat.controllers

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import com.telstra.datacat.domain.BillBreakdown
import com.telstra.datacat.domain.DataIdentifier
import com.telstra.datacat.domain.services.BillBreakdownService
import com.telstra.datacat.domain.services.DataIdentifierService
import io.restassured.module.mockmvc.RestAssuredMockMvc
import io.restassured.module.mockmvc.RestAssuredMockMvc.`when`
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasItems
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate

class BillsControllerTest {

    val billBreakdownService: BillBreakdownService = mock()
    val dataIdentifierService: DataIdentifierService = mock()

    @Before
    fun setUp() {
        reset(billBreakdownService)
        reset(dataIdentifierService)
    }

    @Test
    fun billsEndpoint() {
        RestAssuredMockMvc.standaloneSetup(BillsController(billBreakdownService, dataIdentifierService, true))

        val date = LocalDate.parse("2017-04-03")

        whenever(dataIdentifierService.loadFromToken("token123")).thenReturn(DataIdentifier("token123", "0298453212", date))

        whenever(billBreakdownService.lastBillBreakdowns("0298453212"))
                .thenReturn(listOf(BillBreakdown(LocalDate.parse("2017-01-16"), BigDecimal("120"), BigDecimal("80"), BigDecimal("40"), BigInteger("1000000")),
                        BillBreakdown(LocalDate.parse("2017-02-16"), BigDecimal("140"), BigDecimal("80"), BigDecimal("60"), BigInteger("2000000")),
                        BillBreakdown(LocalDate.parse("2017-03-16"), BigDecimal("150"), BigDecimal("100"), BigDecimal("50"), BigInteger("3000000"))))

        `when`()
                .get("/api/lastbills/token123")
                .then()
                .body("date", hasItems("2017-01-16", "2017-02-16", "2017-03-16"))
                .body("bill", hasItems(120, 140, 150))
                .body("baseBill", hasItems(80, 80, 100))
                .body("extraDataCharge", hasItems(40, 60, 50))
                .body("extraData", hasItems(1000000, 2000000, 3000000))
    }

    @Test
    fun billsEndpointShouldReturnEmptyResultWhenFlagIsOff () {
        RestAssuredMockMvc.standaloneSetup(BillsController(billBreakdownService, dataIdentifierService, false))
        `when`()
                .get("/api/lastbills/token123")
                .then()
                .body(equalTo("[]"))

        verifyZeroInteractions(dataIdentifierService)
        verifyZeroInteractions(billBreakdownService)
    }
}
