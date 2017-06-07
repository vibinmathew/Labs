package com.telstra.datacat.adapters.databases.repositories

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.telstra.datacat.adapters.databases.BillRow
import org.assertj.core.api.Assertions
import org.junit.Test
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.sql.Timestamp


class BillsRepositoryImplTest {

    val jdbcTemplate: NamedParameterJdbcTemplate = mock()
    val billRowMapper = BillRowMapper()
    val repository = BillsRepositoryImpl(billRowMapper, jdbcTemplate)

    @Test
    fun lastBills() {
        val billRows = listOf(
                BillRow("0478895612", "120", Timestamp.valueOf("2017-03-15 00:00:00"), "100", "20", "1000000"),
                BillRow("0478895612", "150", Timestamp.valueOf("2017-02-15 00:00:00"), "100", "50", "2000000"),
                BillRow("0478895612", "160", Timestamp.valueOf("2017-01-15 00:00:00"), "100", "60", "5000000")
        )

        val singletonMap: Map<String, Any> = mapOf("msisdn" to "0412345678")

        whenever(jdbcTemplate.query("SELECT msisdn, bill, date, base_bill, extra_data_charge, extra_data FROM bills " +
                "WHERE msisdn = :msisdn order by date DESC LIMIT 3",
                singletonMap, billRowMapper))
                .thenReturn(billRows)

        Assertions.assertThat(repository.lastBills("0412345678")).isEqualTo(billRows)
    }
}