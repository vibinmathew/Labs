package com.telstra.datacat.adapters.databases.repositories

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.telstra.datacat.adapters.databases.MsisdnFilterRow
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Matchers
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.sql.Date
import java.sql.Timestamp
import java.util.HashMap

/**
 * Created by d756351 on 1/05/2017.
 */
class MsisdnFilterRepositoryImplTest {

    val jdbcTemplate: NamedParameterJdbcTemplate = mock()
    val msisdnFilterRowMapper = MsisdnFilterRowMapper()
    val repository = MsisdnFilterRepositoryImpl(msisdnFilterRowMapper, jdbcTemplate)

    @Test
    fun add_shouldAddMsisdnToFilterList() {
        repository.addMsisdnToFilterlist("0400000000", true)

        argumentCaptor<MutableMap<String, Any>>().apply {
            verify(jdbcTemplate).update(Matchers.eq("INSERT INTO msisdn_filter(msisdn, to_send) VALUES (:msisdn, :to_send))"),
                    capture())

            val msisdn: String = firstValue["msisdn"] as String
            val toSend: Boolean = firstValue["to_send"] as Boolean
            assertThat(msisdn).isEqualTo("0400000000")
            assertThat(toSend).isEqualTo(true)
        }
    }

    @Test
    fun check_shouldReturnSavedValueForMsisdn() {
        val msisdnFilterRow = MsisdnFilterRow("1234567890", true)
        val singletonMap: Map<String, Any> = mapOf("msisdn" to "0412345678")
        whenever(jdbcTemplate.query("SELECT msisdn, to_send at FROM msisdn_filter " +
                "WHERE msisdn = :msisdn",
                singletonMap, msisdnFilterRowMapper))
                .thenReturn(listOf(msisdnFilterRow))

        assertThat(repository.check("1234567890")).isEqualTo(true)
    }

    @Test
    fun check_shouldReturnTruewhenNoResultFound() {
        assertThat(repository.check("1234567891")).isEqualTo(true)
    }
}