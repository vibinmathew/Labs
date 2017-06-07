package com.telstra.datacat.adapters.databases

import com.telstra.datacat.domain.DataIdentifier
import com.telstra.datacat.domain.gateways.DataIdentifierGateway
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class DataIdentifierAdapter(val repository: DataIdentifierRepository) : DataIdentifierGateway {

    override fun load(token: String): DataIdentifier {
        val dataRow = repository.findByToken(token)
        return DataIdentifier(token, dataRow.msisdn, dataRow.date)
    }

    override fun saveToken(token: String, msisdn: String, date: LocalDate) {
        repository.saveDataIdentifier(DataIdentifierRow(token, msisdn, date))
    }
}