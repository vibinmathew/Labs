package com.telstra.datacat.adapters.databases.repositories

import com.telstra.datacat.adapters.databases.BillingRow
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class BillingRowMapper : RowMapper<BillingRow> {
    override fun mapRow(rs: ResultSet, rowNum: Int): BillingRow {
        return BillingRow(
                rs.getString("usage"),
                rs.getString("allowance"),
                rs.getString("base_bill"),
                rs.getTimestamp("billing_start"),
                rs.getTimestamp("billing_end"),
                rs.getTimestamp("updated_at")
        )
    }
}