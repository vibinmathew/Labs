package com.telstra.datacat.adapters.databases.repositories

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.telstra.datacat.adapters.databases.UsageNotificationRow
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.Matchers.eq
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.sql.Timestamp
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

class OndbRepositoryTest {

    val jdbcTemplate: NamedParameterJdbcTemplate = mock()
    val mapper = UsageNotificationRowMapper()
    val clock = Clock.fixed(ZonedDateTime.now().toInstant(), ZoneId.systemDefault())

    val repo = OndbRepository(jdbcTemplate, mapper, clock, "anyname", 3)

    @Test
    fun fetchUsageNotifications() {
        val expectedStartTime = LocalDateTime.now(clock).minusDays(3)

        val notifs = Arrays.asList(
                UsageNotificationRow("0404040404"),
                UsageNotificationRow(System.currentTimeMillis().toString()))

        whenever(jdbcTemplate.query(
                eq("SELECT DISTINCT msisdn FROM anyname WHERE percent >= 50 AND gploaded_time > :startTime"),
                any<MutableMap<String, Any>>(), eq(mapper)))
                .thenReturn(notifs)

        assertThat(repo.fetchUsageNotifications()).isEqualTo(notifs)

        argumentCaptor<MutableMap<String, Any>>().apply {
            verify(jdbcTemplate).query(eq("SELECT DISTINCT msisdn FROM anyname WHERE percent >= 50 AND gploaded_time > :startTime"),
                    capture(),
                    eq(mapper))

            val actualStartTime: Timestamp = firstValue["startTime"] as Timestamp

            assertThat(actualStartTime).isEqualTo(Timestamp.valueOf(expectedStartTime))
        }
    }
}