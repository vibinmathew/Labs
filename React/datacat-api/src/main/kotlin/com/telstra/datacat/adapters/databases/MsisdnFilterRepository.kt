package com.telstra.datacat.adapters.databases

interface MsisdnFilterRepository {
    fun addMsisdnToFilterlist(msisdn: String, toSend: Boolean)
    fun check(msisdn: String): Boolean
}