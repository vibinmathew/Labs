package com.telstra.datacat.domain.gateways

import com.telstra.datacat.domain.DataCategorisationResult
import java.time.LocalDate

interface DataCategorisationGateway {
    fun lastMonth(msisdn: String, from: LocalDate, to: LocalDate) : DataCategorisationResult
}