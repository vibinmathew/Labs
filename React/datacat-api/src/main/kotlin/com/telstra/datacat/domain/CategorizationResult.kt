package com.telstra.datacat.domain

data class CategorizationResult(val overall: List<CategoryResult>, val dailyUsage: List<DailyUsageResult>)