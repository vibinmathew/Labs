package com.telstra.datacat.adapters.databases.repositories

import com.telstra.datacat.adapters.databases.BillingRepository
import com.telstra.datacat.adapters.databases.BillingRow
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.sql.Date
import java.time.LocalDate

@Component
class MdumRepository(val fakeJdbcTemplate: NamedParameterJdbcTemplate,
                     val billingRowMapper: BillingRowMapper) : BillingRepository {

    override fun fetchBilling(msisdn: String, date: LocalDate, index: Int): BillingRow? {

        val indexDate = date.plusMonths(index.toLong())

        val params: Map<String, Any> = mapOf("msisdn" to msisdn, "date" to Date.valueOf(indexDate))

        val query = "SELECT usage, allowance, base_bill, billing_start, billing_end, updated_at FROM mdum " +
                "WHERE msisdn = :msisdn AND billing_start <= :date AND billing_end > :date LIMIT 1"

        return fakeJdbcTemplate.query(query, params, billingRowMapper).firstOrNull()
    }
}