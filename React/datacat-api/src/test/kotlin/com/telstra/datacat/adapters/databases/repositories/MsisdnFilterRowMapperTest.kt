package com.telstra.datacat.adapters.databases.repositories

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.telstra.datacat.adapters.databases.MsisdnFilterRow
import org.assertj.core.api.Assertions
import org.junit.Test
import java.sql.ResultSet

class MsisdnFilterRowMapperTest {
    @Test
    fun msisdnFilterRowMapper() {

        val msisdnFilterRow = MsisdnFilterRow("04000000", true)

        val resultSet: ResultSet = mock()

        whenever(resultSet.getString("msisdn")).thenReturn("04000000")
        whenever(resultSet.getBoolean("to_send")).thenReturn(true)

        val rowMapper = MsisdnFilterRowMapper()
        Assertions.assertThat(rowMapper.mapRow(resultSet, 0)).isEqualTo(msisdnFilterRow)
    }
}