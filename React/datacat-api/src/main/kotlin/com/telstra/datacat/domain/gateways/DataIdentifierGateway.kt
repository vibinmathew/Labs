package com.telstra.datacat.domain.gateways

import com.telstra.datacat.domain.DataIdentifier
import java.time.LocalDate

interface DataIdentifierGateway {
    fun load(token: String): DataIdentifier
    fun saveToken(token: String, msisdn: String, date: LocalDate)
}