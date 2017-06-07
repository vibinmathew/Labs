package com.telstra.datacat.domain.services

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.telstra.datacat.domain.BaseBill
import com.telstra.datacat.domain.Bill
import com.telstra.datacat.domain.BillBreakdown
import com.telstra.datacat.domain.ExtraData
import com.telstra.datacat.domain.ExtraDataCharge
import com.telstra.datacat.domain.gateways.BaseBillGateway
import com.telstra.datacat.domain.gateways.BillsGateway
import com.telstra.datacat.domain.gateways.ExtraDataChargeGateway
import com.telstra.datacat.domain.gateways.ExtraDataGateway
import org.assertj.core.api.Assertions
import org.junit.Test
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate

class BillBreakdownServiceImplTest {
    val billsGateway: BillsGateway = mock()
    val baseBillGateway: BaseBillGateway = mock()
    val extraDataChargeGateway: ExtraDataChargeGateway = mock()
    val extraDataGateway: ExtraDataGateway = mock()

    val billBreakdownService = BillBreakdownServiceImpl(billsGateway, baseBillGateway, extraDataChargeGateway, extraDataGateway)

    @Test
    fun lastBillsBreakdown() {
        val bills = listOf(
                Bill(LocalDate.parse("2017-01-15"), BigDecimal("160")),
                Bill(LocalDate.parse("2017-02-15"), BigDecimal("140")),
                Bill(LocalDate.parse("2017-03-15"), BigDecimal("150"))
        )

        val baseBills = listOf(
                BaseBill(LocalDate.parse("2017-01-15"), BigDecimal("100")),
                BaseBill(LocalDate.parse("2017-02-15"), BigDecimal("100")),
                BaseBill(LocalDate.parse("2017-03-15"), BigDecimal("100"))
        )

        val extraDataCharges = listOf(
                ExtraDataCharge(LocalDate.parse("2017-01-15"), BigDecimal("60")),
                ExtraDataCharge(LocalDate.parse("2017-02-15"), BigDecimal("40")),
                ExtraDataCharge(LocalDate.parse("2017-03-15"), BigDecimal("50"))
        )

        val extraData = listOf(
                ExtraData(LocalDate.parse("2017-01-15"), BigInteger("1000000")),
                ExtraData(LocalDate.parse("2017-02-15"), BigInteger("2000000")),
                ExtraData(LocalDate.parse("2017-03-15"), BigInteger("500000"))
        )

        val expectedBillsBreakdown = listOf(
                BillBreakdown(LocalDate.parse("2017-01-15"), BigDecimal("160"), BigDecimal("100"), BigDecimal("60"), BigInteger("1000000")),
                BillBreakdown(LocalDate.parse("2017-02-15"), BigDecimal("140"), BigDecimal("100"), BigDecimal("40"), BigInteger("2000000")),
                BillBreakdown(LocalDate.parse("2017-03-15"), BigDecimal("150"), BigDecimal("100"), BigDecimal("50"), BigInteger("500000"))
        )

        whenever(billsGateway.lastBills("0400000000")).thenReturn(bills)
        whenever(baseBillGateway.lastBaseBills("0400000000")).thenReturn(baseBills)
        whenever(extraDataChargeGateway.lastExtraDataCharges("0400000000")).thenReturn(extraDataCharges)
        whenever(extraDataGateway.lastExtraDataList("0400000000")).thenReturn(extraData)

        Assertions.assertThat(billBreakdownService.lastBillBreakdowns("0400000000")).isEqualTo(expectedBillsBreakdown)

        verify(billsGateway).lastBills("0400000000")
        verify(baseBillGateway).lastBaseBills("0400000000")
        verify(extraDataChargeGateway).lastExtraDataCharges("0400000000")
        verify(extraDataGateway).lastExtraDataList("0400000000")
    }

    @Test
    fun lastBillsBreakdown_shouldHandleZeroExtra() {
        val bills = listOf(
                Bill(LocalDate.parse("2017-01-15"), BigDecimal("160")),
                Bill(LocalDate.parse("2017-02-15"), BigDecimal("140")),
                Bill(LocalDate.parse("2017-03-15"), BigDecimal("150"))
        )

        val baseBills = listOf(
                BaseBill(LocalDate.parse("2017-01-15"), BigDecimal("100")),
                BaseBill(LocalDate.parse("2017-02-15"), BigDecimal("100")),
                BaseBill(LocalDate.parse("2017-03-15"), BigDecimal("100"))
        )

        val extraDataCharges = listOf(
                ExtraDataCharge(LocalDate.parse("2017-01-15"), BigDecimal("60")),
                ExtraDataCharge(LocalDate.parse("2017-03-15"), BigDecimal("50"))
        )

        val extraData = listOf(
                ExtraData(LocalDate.parse("2017-01-15"), BigInteger("1000000")),
                ExtraData(LocalDate.parse("2017-02-15"), BigInteger("2000000")),
                ExtraData(LocalDate.parse("2017-03-15"), BigInteger("500000"))
        )

        val expectedBillsBreakdown = listOf(
                BillBreakdown(LocalDate.parse("2017-01-15"), BigDecimal("160"), BigDecimal("100"), BigDecimal("60"), BigInteger("1000000")),
                BillBreakdown(LocalDate.parse("2017-02-15"), BigDecimal("140"), BigDecimal("100"), BigDecimal.ZERO, BigInteger("2000000")),
                BillBreakdown(LocalDate.parse("2017-03-15"), BigDecimal("150"), BigDecimal("100"), BigDecimal("50"), BigInteger("500000"))
        )

        whenever(billsGateway.lastBills("0400000000")).thenReturn(bills)
        whenever(baseBillGateway.lastBaseBills("0400000000")).thenReturn(baseBills)
        whenever(extraDataChargeGateway.lastExtraDataCharges("0400000000")).thenReturn(extraDataCharges)
        whenever(extraDataGateway.lastExtraDataList("0400000000")).thenReturn(extraData)

        Assertions.assertThat(billBreakdownService.lastBillBreakdowns("0400000000")).isEqualTo(expectedBillsBreakdown)
    }
}