package com.telstra.datacat.adapters.databases

import java.time.LocalDate

interface BillingRepository {
    fun fetchBilling(msisdn: String, date: LocalDate, index: Int): BillingRow?
}