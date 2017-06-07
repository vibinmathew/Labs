package com.telstra.datacat.adapters.databases.repositories

import com.telstra.datacat.adapters.databases.BaseBillRepository
import com.telstra.datacat.adapters.databases.BillRow
import com.telstra.datacat.adapters.databases.BillsRepository
import com.telstra.datacat.adapters.databases.ExtraDataChargeRepository
import com.telstra.datacat.adapters.databases.ExtraDataRepository
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

@Component
class BillsRepositoryImpl(val billRowMapper: BillRowMapper,
                          val fakeJdbcTemplate: NamedParameterJdbcTemplate) :
        BillsRepository, BaseBillRepository, ExtraDataChargeRepository, ExtraDataRepository {

    override fun lastBills(msisdn: String): List<BillRow> {
        val params: Map<String, Any> = mapOf("msisdn" to msisdn)

        val query = "SELECT msisdn, bill, date, base_bill, extra_data_charge, extra_data FROM bills " +
                "WHERE msisdn = :msisdn order by date DESC LIMIT 3"

        return fakeJdbcTemplate.query(query, params, billRowMapper)
    }

    override fun lastBaseBills(msisdn: String): List<BillRow> {
        return lastBills(msisdn)
    }

    override fun lastExtraDataCharges(msisdn: String): List<BillRow> {
        return lastBills(msisdn)
    }

    override fun lastExtraDataList(msisdn: String): List<BillRow> {
        return lastBills(msisdn)
    }
}
