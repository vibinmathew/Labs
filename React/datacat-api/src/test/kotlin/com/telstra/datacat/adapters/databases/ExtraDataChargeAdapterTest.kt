package com.telstra.datacat.adapters.databases

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.telstra.datacat.domain.ExtraDataCharge
import org.assertj.core.api.Assertions
import org.junit.Test
import java.math.BigDecimal
import java.sql.Timestamp
import java.time.LocalDate

class ExtraDataChargeAdapterTest {
    val repository: ExtraDataChargeRepository = mock()

    val adapter = ExtraDataChargeAdapter(repository)

    @Test
    fun lastExtraDataCharges() {
        val billsRows = listOf(
                BillRow("0478895612", "120", Timestamp.valueOf("2017-03-15 00:00:00"), "100", "50", "1000000"),
                BillRow("0478895612", "150", Timestamp.valueOf("2017-02-15 00:00:00"), "100", "40", "2000000"),
                BillRow("0478895612", "160", Timestamp.valueOf("2017-01-15 00:00:00"), "100", "60", "5000000")
        )
        whenever(repository.lastExtraDataCharges("0478895612")).thenReturn(billsRows)

        val extraDataCharges = listOf(
                ExtraDataCharge(LocalDate.parse("2017-01-15"), BigDecimal("60")),
                ExtraDataCharge(LocalDate.parse("2017-02-15"), BigDecimal("40")),
                ExtraDataCharge(LocalDate.parse("2017-03-15"), BigDecimal("50"))
        )

        Assertions.assertThat(adapter.lastExtraDataCharges("0478895612")).isEqualTo(extraDataCharges)

        verify(repository).lastExtraDataCharges("0478895612")
    }
}