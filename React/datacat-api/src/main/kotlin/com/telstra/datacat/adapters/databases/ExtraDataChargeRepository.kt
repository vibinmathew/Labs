package com.telstra.datacat.adapters.databases

interface ExtraDataChargeRepository {
    fun lastExtraDataCharges(msisdn: String): List<BillRow>
}