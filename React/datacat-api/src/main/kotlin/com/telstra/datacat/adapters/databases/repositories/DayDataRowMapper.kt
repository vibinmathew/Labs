package com.telstra.datacat.adapters.databases.repositories

import com.telstra.datacat.adapters.databases.DayDataRow
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class DayDataRowMapper : RowMapper<DayDataRow> {
    override fun mapRow(rs: ResultSet, rowNum: Int): DayDataRow {
        return DayDataRow(rs.getString("day"), rs.getInt("cat_id"), rs.getString("amount"))
    }
}