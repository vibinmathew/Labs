package com.telstra.datacat.adapters.databases.repositories

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.telstra.datacat.adapters.databases.BillingRow
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.sql.ResultSet
import java.sql.Timestamp
import java.time.ZonedDateTime

class BillingRowMapperTest {

    @Test
    fun billingRowMapper() {
        val startTimestamp = Timestamp(ZonedDateTime.now().minusDays(30).toEpochSecond())
        val endTimestamp = Timestamp(ZonedDateTime.now().toEpochSecond())
        val updatedTimestamp = Timestamp(ZonedDateTime.now().minusDays(1).toEpochSecond())

        val expectedBillingRow = BillingRow("50", "100", "45.95", startTimestamp, endTimestamp, updatedTimestamp)

        val resultSet: ResultSet = mock()

        whenever(resultSet.getString("usage")).thenReturn("50")
        whenever(resultSet.getString("allowance")).thenReturn("100")
        whenever(resultSet.getString("base_bill")).thenReturn("45.95")
        whenever(resultSet.getTimestamp("billing_start")).thenReturn(startTimestamp)
        whenever(resultSet.getTimestamp("billing_end")).thenReturn(endTimestamp)
        whenever(resultSet.getTimestamp("updated_at")).thenReturn(updatedTimestamp)

        val rowMapper = BillingRowMapper()
        assertThat(rowMapper.mapRow(resultSet, 0)).isEqualTo(expectedBillingRow)
    }
}