package com.telstra.datacat.controllers

import com.telstra.datacat.domain.CategorizationResult
import com.telstra.datacat.domain.services.CategorizationResultGenerator
import com.telstra.datacat.domain.services.DataIdentifierService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CategorizationController(val categorizationResultGenerator: CategorizationResultGenerator, val dataIdentifierService: DataIdentifierService) {

    @RequestMapping("/api/categorization/{token}")
    fun getCategorization(@PathVariable token: String): CategorizationResult {
        val dataIdentifier = dataIdentifierService.loadFromToken(token)
        return categorizationResultGenerator.generate(dataIdentifier.msisdn, dataIdentifier.date)
    }

    @RequestMapping("/api/categorization/{token}/{index}")
    fun getCategorization(@PathVariable token: String, @PathVariable index: Int = 0): CategorizationResult {
        val dataIdentifier = dataIdentifierService.loadFromToken(token)
        return categorizationResultGenerator.generate(dataIdentifier.msisdn, dataIdentifier.date, index)
    }
}