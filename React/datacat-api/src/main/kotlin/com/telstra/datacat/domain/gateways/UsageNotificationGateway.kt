package com.telstra.datacat.domain.gateways

interface UsageNotificationGateway {
    fun msisdnToNotify(): List<String>
}