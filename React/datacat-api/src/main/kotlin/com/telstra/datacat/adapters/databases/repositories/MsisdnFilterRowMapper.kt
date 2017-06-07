package com.telstra.datacat.adapters.databases.repositories

import com.telstra.datacat.adapters.databases.BillingRow
import com.telstra.datacat.adapters.databases.MsisdnFilterRow
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class MsisdnFilterRowMapper : RowMapper<MsisdnFilterRow> {
    override fun mapRow(rs: ResultSet, rowNum: Int): MsisdnFilterRow {
        return MsisdnFilterRow(
                rs.getString("msisdn"),
                rs.getBoolean("to_send")
        )
    }
}