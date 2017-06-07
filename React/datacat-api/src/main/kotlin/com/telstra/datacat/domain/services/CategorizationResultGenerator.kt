package com.telstra.datacat.domain.services

import com.telstra.datacat.domain.CategorizationResult
import java.time.LocalDate

interface CategorizationResultGenerator {
    fun generate(msisdn: String, date: LocalDate, index: Int = 0): CategorizationResult
}