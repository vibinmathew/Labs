package com.telstra.datacat.adapters.databases

interface BillsRepository {
    fun lastBills(msisdn: String): List<BillRow>
}