package com.telstra.datacat.adapters.databases.repositories

import com.telstra.datacat.adapters.databases.DataCategorisationRepository
import com.telstra.datacat.adapters.databases.DayDataRow
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.sql.Date
import java.time.Clock
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Component
class RtiRepository(val rtiJdbcTemplate: NamedParameterJdbcTemplate,
                    val dayDataRowMapper: DayDataRowMapper, val clock: Clock) : DataCategorisationRepository {

    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMM")

    override fun fetchLastMonthOfData(msisdn: String, from: LocalDate, to: LocalDate): List<DayDataRow> {
        val previousTableSuffix = from.format(formatter)
        val latestTableSuffix = to.format(formatter)

        val latest_table = "month_by_day_$latestTableSuffix"
        val previous_table = "month_by_day_$previousTableSuffix"

        var query = "(SELECT day, (category_id / 100) as cat_id, sum(bytes_up + bytes_down) AS amount" +
                " FROM $previous_table" +
                " WHERE msisdn = :msisdn " +
                " AND day >= :from" +
                " AND day <= :to" +
                " GROUP BY day, cat_id" +
                " ORDER BY day, cat_id)"

        val now = LocalDate.now(clock)
        if (now.month >= to.month || now.year > to.year) {
            query =  "SELECT * FROM " +
                    "(SELECT day, (category_id / 100) as cat_id, sum(bytes_up + bytes_down) AS amount" +
                    " FROM $latest_table" +
                    " WHERE msisdn = :msisdn " +
                    " AND day >= :from" +
                    " AND day <= :to" +
                    " GROUP BY day, cat_id" +
                    " ORDER BY day, cat_id) foo " +
                    " UNION" + query
        }

        val parameters : MutableMap<String, Any> = HashMap()
        parameters.put("msisdn", msisdn)
        parameters.put("from", Date.valueOf(from))
        parameters.put("to", Date.valueOf(to))

        return rtiJdbcTemplate.query(query, parameters, dayDataRowMapper)
    }
}