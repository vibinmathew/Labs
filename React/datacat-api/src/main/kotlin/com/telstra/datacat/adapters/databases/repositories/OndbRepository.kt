package com.telstra.datacat.adapters.databases.repositories

import com.telstra.datacat.adapters.databases.UsageNotificationRepository
import com.telstra.datacat.adapters.databases.UsageNotificationRow
import org.springframework.beans.factory.annotation.Value
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime
import java.util.*

@Component
class OndbRepository(val ondbJdbcTemplate: NamedParameterJdbcTemplate,
                     val mapper: UsageNotificationRowMapper,
                     val clock: Clock,
                     @Value("\${data-usage.notifications.table-name}") val tableName: String,
                     @Value("\${data-usage.notifications.numberOfDaysBefore}") val numberOfDaysBefore: Long): UsageNotificationRepository {
    override fun fetchUsageNotifications(): List<UsageNotificationRow> {
        val startTime = LocalDateTime.now(clock).minusDays(numberOfDaysBefore)

        val parameters : MutableMap<String, Any> = HashMap()
        parameters.put("startTime", java.sql.Timestamp.valueOf(startTime))

        return ondbJdbcTemplate.query("SELECT DISTINCT msisdn FROM $tableName WHERE percent >= 50 AND gploaded_time > :startTime",
                parameters, mapper)
    }
}