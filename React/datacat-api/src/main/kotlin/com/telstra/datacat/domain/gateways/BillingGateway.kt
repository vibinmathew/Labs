package com.telstra.datacat.domain.gateways

import com.telstra.datacat.domain.Billing
import java.time.LocalDate

interface BillingGateway {
    fun billing(msisdn: String, date: LocalDate, index: Int, token: String = ""): Billing
}