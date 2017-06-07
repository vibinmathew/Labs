package com.telstra.datacat.adapters.databases.repositories

import com.telstra.datacat.adapters.databases.UsageNotificationRow
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class UsageNotificationRowMapper : RowMapper<UsageNotificationRow> {
    override fun mapRow(rs: ResultSet, rowNum: Int): UsageNotificationRow {
        return UsageNotificationRow(rs.getString("msisdn"))
    }
}