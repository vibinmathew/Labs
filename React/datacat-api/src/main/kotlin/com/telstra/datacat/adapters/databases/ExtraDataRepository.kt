package com.telstra.datacat.adapters.databases

interface ExtraDataRepository {
    fun lastExtraDataList(msisdn: String): List<BillRow>
}