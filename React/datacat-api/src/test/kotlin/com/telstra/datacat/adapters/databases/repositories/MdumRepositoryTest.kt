package com.telstra.datacat.adapters.databases.repositories

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.telstra.datacat.adapters.databases.BillingRow
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.sql.Date.valueOf
import java.sql.Timestamp
import java.time.LocalDate
import java.util.*

class MdumRepositoryTest {

    val jdbcTemplate: NamedParameterJdbcTemplate = mock()

    val billingRowMapper = BillingRowMapper()
    val repository = MdumRepository(jdbcTemplate, billingRowMapper)
    val billingRow = BillingRow(
            "123456789",
            "987654321",
            "45.95",
            Timestamp.valueOf("2017-04-15 00:00:00"),
            Timestamp.valueOf("2017-05-15 00:00:00"),
            Timestamp.valueOf("2017-03-14 08:50:00")
    )

    @Test
    fun fetchBilling_handlesCurrentCycle() {
        val date = LocalDate.parse("2017-04-25")
        val singletonMap: Map<String, Any> = mapOf("msisdn" to "0412345678", "date" to valueOf(date))

        whenever(jdbcTemplate.query("SELECT usage, allowance, base_bill, billing_start, billing_end, updated_at FROM mdum " +
                "WHERE msisdn = :msisdn AND billing_start <= :date AND billing_end > :date LIMIT 1",
                singletonMap, billingRowMapper))
                .thenReturn(listOf(billingRow))

        assertThat(repository.fetchBilling("0412345678", date, 0)).isEqualTo(billingRow)
    }

    @Test
    fun fetchBilling_handlesNoCycle() {
        val date = LocalDate.parse("2017-04-25")
        val singletonMap: Map<String, Any> = mapOf("msisdn" to "0412345678", "date" to valueOf(date))

        whenever(jdbcTemplate.query("SELECT usage, allowance, base_bill, billing_start, billing_end, updated_at FROM mdum " +
                "WHERE msisdn = :msisdn AND billing_start <= :date AND billing_end > :date LIMIT 1",
                singletonMap, billingRowMapper))
                .thenReturn(Collections.emptyList())

        assertThat(repository.fetchBilling("0412345678", date, 0)).isNull()
    }

    @Test
    fun fetchBilling_handlesPreviousCycle() {
        val date = LocalDate.parse("2017-04-25")
        val prevMonth = LocalDate.parse("2017-03-25")

        val singletonMap: Map<String, Any> = mapOf("msisdn" to "0412345678", "date" to valueOf(prevMonth))

        whenever(jdbcTemplate.query("SELECT usage, allowance, base_bill, billing_start, billing_end, updated_at FROM mdum " +
                "WHERE msisdn = :msisdn AND billing_start <= :date AND billing_end > :date LIMIT 1",
                singletonMap, billingRowMapper))
                .thenReturn(listOf(billingRow))

        assertThat(repository.fetchBilling("0412345678", date, -1)).isEqualTo(billingRow)
    }

    @Test
    fun fetchBilling_handlesnextCycle() {
        val date = LocalDate.parse("2017-04-25")
        val nextMonth = LocalDate.parse("2017-05-25")

        val singletonMap: Map<String, Any> = mapOf("msisdn" to "0412345678", "date" to valueOf(nextMonth))

        whenever(jdbcTemplate.query("SELECT usage, allowance, base_bill, billing_start, billing_end, updated_at FROM mdum " +
                "WHERE msisdn = :msisdn AND billing_start <= :date AND billing_end > :date LIMIT 1",
                singletonMap, billingRowMapper))
                .thenReturn(listOf(billingRow))

        assertThat(repository.fetchBilling("0412345678", date, 1)).isEqualTo(billingRow)
    }
}