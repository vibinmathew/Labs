package com.telstra.datacat.controllers

import com.telstra.datacat.domain.Billing
import com.telstra.datacat.domain.gateways.BillingGateway
import com.telstra.datacat.domain.services.DataIdentifierService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class BillingController(val billingGateway: BillingGateway, val dataIdentifierService: DataIdentifierService) {

    @RequestMapping("/api/billing/{token}/{index}")
    fun getUsage(@PathVariable token: String, @PathVariable index: Int = 0): Billing {
        val dataIdentifier = dataIdentifierService.loadFromToken(token)
        return billingGateway.billing(dataIdentifier.msisdn, dataIdentifier.date, index, token)
    }

    @RequestMapping("/api/billing/{token}")
    fun getDefaultUsage(@PathVariable token: String): Billing {
        val dataIdentifier = dataIdentifierService.loadFromToken(token)
        return billingGateway.billing(dataIdentifier.msisdn, dataIdentifier.date, 0, token)
    }
}