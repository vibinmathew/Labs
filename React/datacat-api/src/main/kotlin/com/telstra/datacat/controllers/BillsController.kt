package com.telstra.datacat.controllers

import com.telstra.datacat.domain.BillBreakdown
import com.telstra.datacat.domain.services.BillBreakdownService
import com.telstra.datacat.domain.services.DataIdentifierService
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class BillsController(val billBreakdownService: BillBreakdownService,
                      val dataIdentifierService: DataIdentifierService,
                      @Value("\${feature.datapacks}") val isEnabled: Boolean) {

    @RequestMapping("/api/lastbills/{token}")
    fun getLastBills(@PathVariable token: String): List<BillBreakdown> {
        if(!isEnabled) {
            return Collections.emptyList()
        }
        val dataIdentifier = dataIdentifierService.loadFromToken(token)
        return billBreakdownService.lastBillBreakdowns(dataIdentifier.msisdn)
    }
}

