package com.telstra.datacat.adapters.databases

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.telstra.datacat.domain.Bill
import org.assertj.core.api.Assertions
import org.junit.Test
import java.math.BigDecimal
import java.sql.Timestamp
import java.time.LocalDate

class BillsAdapterTest {
    val repository: BillsRepository = mock()

    val adapter = BillsAdapter(repository)

    @Test
    fun bills_shouldReturnLast3Bills() {
        val billsRows = listOf(
                BillRow("0478895612", "120", Timestamp.valueOf("2017-03-15 00:00:00"), "100", "20", "1000000"),
                BillRow("0478895612", "150", Timestamp.valueOf("2017-02-15 00:00:00"), "100", "50", "2000000"),
                BillRow("0478895612", "160", Timestamp.valueOf("2017-01-15 00:00:00"), "100", "50", "5000000")
        )
        whenever(repository.lastBills("0478895612")).thenReturn(billsRows)

        val bills = listOf(
                Bill(LocalDate.parse("2017-01-15"), BigDecimal("160")),
                Bill(LocalDate.parse("2017-02-15"), BigDecimal("150")),
                Bill(LocalDate.parse("2017-03-15"), BigDecimal("120"))
                )

        Assertions.assertThat(adapter.lastBills("0478895612")).isEqualTo(bills)

        verify(repository).lastBills("0478895612")
    }
}