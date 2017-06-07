package com.telstra.datacat.adapters.databases

interface BaseBillRepository {
    fun lastBaseBills(msisdn: String): List<BillRow>
}