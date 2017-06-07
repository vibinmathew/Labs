package com.telstra.datacat.domain.services.converters

import com.telstra.datacat.adapters.databases.repositories.DataCategories
import com.telstra.datacat.domain.DailyUsageResult
import com.telstra.datacat.domain.DataCategorisationResult


interface DailyUsageBreakdownConverter {
    fun convert (input: DataCategorisationResult, categories: List<DataCategories>) : List<DailyUsageResult>
}