package com.telstra.datacat.domain.services

import com.telstra.datacat.domain.BillBreakdown

interface BillBreakdownService {
    fun lastBillBreakdowns(msisdn: String) : List<BillBreakdown>
}