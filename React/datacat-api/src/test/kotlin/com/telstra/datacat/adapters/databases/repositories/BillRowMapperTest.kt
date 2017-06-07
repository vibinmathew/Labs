package com.telstra.datacat.adapters.databases.repositories

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.telstra.datacat.adapters.databases.BillRow
import org.assertj.core.api.Assertions
import org.junit.Test
import java.sql.ResultSet
import java.sql.Timestamp
import java.time.ZonedDateTime


class BillRowMapperTest {
    @Test
    fun mapRow() {
        val date = Timestamp(ZonedDateTime.now().toEpochSecond())
        val expectedBillRow = BillRow("0400000001", "100", date, "80", "20", "1000000")
        val resultSet: ResultSet = mock()
        whenever(resultSet.getString("msisdn")).thenReturn("0400000001")
        whenever(resultSet.getString("bill")).thenReturn("100")
        whenever(resultSet.getTimestamp("date")).thenReturn(date)
        whenever(resultSet.getString("base_bill")).thenReturn("80")
        whenever(resultSet.getString("extra_data_charge")).thenReturn("20")
        whenever(resultSet.getString("extra_data")).thenReturn("1000000")

        val rowMapper = BillRowMapper()
        Assertions.assertThat(rowMapper.mapRow(resultSet, 0)).isEqualTo(expectedBillRow)
    }
}