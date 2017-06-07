package com.telstra.datacat.adapters.databases

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.telstra.datacat.domain.BaseBill
import org.assertj.core.api.Assertions
import org.junit.Test
import java.math.BigDecimal
import java.sql.Timestamp
import java.time.LocalDate


class BaseBillAdapterTest {

    val repository: BaseBillRepository = mock()

    val adapter = BaseBillAdapter(repository)

    @Test
    fun lastBaseBills() {
        val billsRows = listOf(
                BillRow("0478895612", "120", Timestamp.valueOf("2017-03-15 00:00:00"), "100", "50", "1000000"),
                BillRow("0478895612", "150", Timestamp.valueOf("2017-02-15 00:00:00"), "100", "40", "2000000"),
                BillRow("0478895612", "160", Timestamp.valueOf("2017-01-15 00:00:00"), "100", "60", "5000000")
        )
        whenever(repository.lastBaseBills("0478895612")).thenReturn(billsRows)

        val baseBills = listOf(
                BaseBill(LocalDate.parse("2017-01-15"), BigDecimal("100")),
                BaseBill(LocalDate.parse("2017-02-15"), BigDecimal("100")),
                BaseBill(LocalDate.parse("2017-03-15"), BigDecimal("100"))
        )

        Assertions.assertThat(adapter.lastBaseBills("0478895612")).isEqualTo(baseBills)

        verify(repository).lastBaseBills("0478895612")
    }
}