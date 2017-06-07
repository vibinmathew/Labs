package com.telstra.datacat.domain.gateways

interface MsisdnFilterGateway {
    fun add(msisdn: String, toSend: Boolean)
    fun check(msisdn: String): Boolean
}