package com.telstra.datacat.domain.gateways

interface SmsGateway<T> {
    fun send(request: T): Boolean
}