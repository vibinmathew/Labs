package com.telstra.datacat.adapters.databases.repositories

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.telstra.datacat.adapters.databases.DayDataRow
import org.assertj.core.api.Assertions
import org.junit.Test
import java.sql.ResultSet


class DayDataRowMapperTest {

    @Test
    fun mapper() {
        val expectedDayDataRow = DayDataRow("string1", 123, "10")

        val resultSet: ResultSet = mock()

        whenever(resultSet.getString("day")).thenReturn("string1")
        whenever(resultSet.getInt("cat_id")).thenReturn(123)
        whenever(resultSet.getString("amount")).thenReturn("10")

        val rowMapper = DayDataRowMapper()
        Assertions.assertThat(rowMapper.mapRow(resultSet, 0)).isEqualTo(expectedDayDataRow)
    }
}