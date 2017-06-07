package com.telstra.datacat.adapters.databases.repositories

import com.telstra.datacat.adapters.databases.MsisdnFilterRepository
import com.telstra.datacat.adapters.databases.MsisdnFilterRow
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.util.HashMap

@Component
class MsisdnFilterRepositoryImpl(val msisdnFilterRowMapper: MsisdnFilterRowMapper, val fakeJdbcTemplate: NamedParameterJdbcTemplate) : MsisdnFilterRepository {
    override fun addMsisdnToFilterlist(msisdn: String, toSend: Boolean) {

        val paramsMap: MutableMap<String, Any> = HashMap()
        paramsMap.put("msisdn", msisdn)
        paramsMap.put("to_send", toSend)

        fakeJdbcTemplate.update("INSERT INTO msisdn_filter(msisdn, to_send) VALUES (:msisdn, :to_send))", paramsMap)
    }

    override fun check(msisdn: String): Boolean {

        val parameters: MutableMap<String, Any> = HashMap()
        parameters.put("msisdn", msisdn)

        val msisdnFilterRows: List<MsisdnFilterRow> = fakeJdbcTemplate.query("SELECT msisdn, to_send FROM msisdn_filter WHERE msisdn = :msisdn",
                parameters, msisdnFilterRowMapper)

        if (msisdnFilterRows.size > 0) {
            return msisdnFilterRows[0].toSend
        } else {
            return true
        }
    }
}