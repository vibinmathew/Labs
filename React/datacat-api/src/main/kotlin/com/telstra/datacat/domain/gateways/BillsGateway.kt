package com.telstra.datacat.domain.gateways

import com.telstra.datacat.domain.Bill

interface BillsGateway {
    fun lastBills(msisdn: String): List<Bill>
}