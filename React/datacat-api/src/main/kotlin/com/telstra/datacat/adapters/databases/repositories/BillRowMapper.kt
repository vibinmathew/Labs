package com.telstra.datacat.adapters.databases.repositories

import com.telstra.datacat.adapters.databases.BillRow
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class BillRowMapper : RowMapper<BillRow> {
    override fun mapRow(rs: ResultSet, rowNum: Int): BillRow {
        return BillRow(
                rs.getString("msisdn"),
                rs.getString("bill"),
                rs.getTimestamp("date"),
                rs.getString("base_bill"),
                rs.getString("extra_data_charge"),
                rs.getString("extra_data")
        )
    }
}