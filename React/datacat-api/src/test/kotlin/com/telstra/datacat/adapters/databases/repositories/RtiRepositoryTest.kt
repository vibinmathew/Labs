package com.telstra.datacat.adapters.databases.repositories

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.telstra.datacat.adapters.databases.DayDataRow
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.Clock
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

class RtiRepositoryTest {

    val jdbcTemplate: NamedParameterJdbcTemplate = mock()
    var clock = Clock.fixed(ZonedDateTime.of(2017, 2, 21, 10, 15, 30, 0, ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault())

    val dayDataRowBuilder = DayDataRowMapper()
    val repo = RtiRepository(jdbcTemplate, dayDataRowBuilder, clock)

    @Test
    fun fetchLastMonthOfData_generatesUnionQueryWhenBillingEndDateInSameMonth() {
        val toDate = LocalDate.parse("2017-02-24")
        val fromDate = LocalDate.parse("2017-01-24")

        val paramsMap: MutableMap<String, Any> = HashMap()
        paramsMap.put("from", java.sql.Date.valueOf(fromDate))
        paramsMap.put("to", java.sql.Date.valueOf(toDate))
        paramsMap.put("msisdn", "0412345678")

        val sql = "SELECT * FROM (SELECT day, (category_id / 100) as cat_id, sum(bytes_up + bytes_down) AS amount FROM month_by_day_201702 WHERE msisdn = :msisdn  AND day >= :from AND day <= :to GROUP BY day, cat_id ORDER BY day, cat_id) foo  UNION(SELECT day, (category_id / 100) as cat_id, sum(bytes_up + bytes_down) AS amount FROM month_by_day_201701 WHERE msisdn = :msisdn  AND day >= :from AND day <= :to GROUP BY day, cat_id ORDER BY day, cat_id)"

        val mockList: List<DayDataRow> = mock()
        whenever(jdbcTemplate.query(sql, paramsMap, dayDataRowBuilder)).thenReturn(mockList)

        assertThat(repo.fetchLastMonthOfData("0412345678", fromDate, toDate)).isEqualTo(mockList)
    }

    @Test
    fun fetchLastMonthOfData_generatesUnionQueryWhenBillingEndDateInPastMonth() {
        val toDate = LocalDate.parse("2017-01-14")
        val fromDate = LocalDate.parse("2016-12-15")

        val paramsMap: MutableMap<String, Any> = HashMap()
        paramsMap.put("from", java.sql.Date.valueOf(fromDate))
        paramsMap.put("to", java.sql.Date.valueOf(toDate))
        paramsMap.put("msisdn", "0412345678")

        val sql = "SELECT * FROM (SELECT day, (category_id / 100) as cat_id, sum(bytes_up + bytes_down) AS amount FROM month_by_day_201701 WHERE msisdn = :msisdn  AND day >= :from AND day <= :to GROUP BY day, cat_id ORDER BY day, cat_id) foo  UNION(SELECT day, (category_id / 100) as cat_id, sum(bytes_up + bytes_down) AS amount FROM month_by_day_201612 WHERE msisdn = :msisdn  AND day >= :from AND day <= :to GROUP BY day, cat_id ORDER BY day, cat_id)"

        val mockList: List<DayDataRow> = mock()
        whenever(jdbcTemplate.query(sql, paramsMap, dayDataRowBuilder)).thenReturn(mockList)

        assertThat(repo.fetchLastMonthOfData("0412345678", fromDate, toDate)).isEqualTo(mockList)
    }

    @Test
    fun fetchLastMonthOfData_generatesUnionQueryWhenBillingEndDateInPastYear() {
        clock = Clock.fixed(ZonedDateTime.of(2017, 2, 21, 10, 15, 30, 0, ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault())

        val toDate = LocalDate.parse("2016-12-14")
        val fromDate = LocalDate.parse("2016-11-15")

        val paramsMap: MutableMap<String, Any> = HashMap()
        paramsMap.put("from", java.sql.Date.valueOf(fromDate))
        paramsMap.put("to", java.sql.Date.valueOf(toDate))
        paramsMap.put("msisdn", "0412345678")

        val sql = "SELECT * FROM (SELECT day, (category_id / 100) as cat_id, sum(bytes_up + bytes_down) AS amount FROM month_by_day_201612 WHERE msisdn = :msisdn  AND day >= :from AND day <= :to GROUP BY day, cat_id ORDER BY day, cat_id) foo  UNION(SELECT day, (category_id / 100) as cat_id, sum(bytes_up + bytes_down) AS amount FROM month_by_day_201611 WHERE msisdn = :msisdn  AND day >= :from AND day <= :to GROUP BY day, cat_id ORDER BY day, cat_id)"

        val mockList: List<DayDataRow> = Collections.singletonList(DayDataRow("day", 1, "1"))
        whenever(jdbcTemplate.query(sql, paramsMap, dayDataRowBuilder)).thenReturn(mockList)

        val fetchLastMonthOfData = repo.fetchLastMonthOfData("0412345678", fromDate, toDate)
        assertThat(fetchLastMonthOfData).isEqualTo(Collections.singletonList(DayDataRow("day", 1, "1")))
    }

    @Test
    fun fetchLastMonthOfData_generatesSimpleQueryWhenBillingEndDateInFuturMonth() {
        val toDate = LocalDate.parse("2017-03-14")
        val fromDate = LocalDate.parse("2017-02-15")

        val paramsMap: MutableMap<String, Any> = HashMap()
        paramsMap.put("from", java.sql.Date.valueOf(fromDate))
        paramsMap.put("to", java.sql.Date.valueOf(toDate))
        paramsMap.put("msisdn", "0412345678")

        val sql = "(SELECT day, (category_id / 100) as cat_id, sum(bytes_up + bytes_down) AS amount FROM month_by_day_201702 WHERE msisdn = :msisdn  AND day >= :from AND day <= :to GROUP BY day, cat_id ORDER BY day, cat_id)"

        val mockList: List<DayDataRow> = mock()
        whenever(jdbcTemplate.query(sql, paramsMap, dayDataRowBuilder)).thenReturn(mockList)

        assertThat(repo.fetchLastMonthOfData("0412345678", fromDate, toDate)).isEqualTo(mockList)
    }
}