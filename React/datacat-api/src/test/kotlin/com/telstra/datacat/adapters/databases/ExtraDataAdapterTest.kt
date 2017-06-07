package com.telstra.datacat.adapters.databases

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.telstra.datacat.domain.ExtraData
import org.assertj.core.api.Assertions
import org.junit.Test
import java.math.BigInteger
import java.sql.Timestamp
import java.time.LocalDate

class ExtraDataAdapterTest {
    val repository: ExtraDataRepository = mock()

    val adapter = ExtraDataAdapter(repository)

    @Test
    fun lastExtraDataList() {
        val billsRows = listOf(
                BillRow("0478895612", "120", Timestamp.valueOf("2017-03-15 00:00:00"), "100", "50", "1000000"),
                BillRow("0478895612", "150", Timestamp.valueOf("2017-02-15 00:00:00"), "100", "40", "2000000"),
                BillRow("0478895612", "160", Timestamp.valueOf("2017-01-15 00:00:00"), "100", "60", "5000000")
        )
        whenever(repository.lastExtraDataList("0478895612")).thenReturn(billsRows)

        val extraDataList = listOf(
                ExtraData(LocalDate.parse("2017-01-15"), BigInteger("5000000")),
                ExtraData(LocalDate.parse("2017-02-15"), BigInteger("2000000")),
                ExtraData(LocalDate.parse("2017-03-15"), BigInteger("1000000"))
        )

        Assertions.assertThat(adapter.lastExtraDataList("0478895612")).isEqualTo(extraDataList)

        verify(repository).lastExtraDataList("0478895612")
    }
}