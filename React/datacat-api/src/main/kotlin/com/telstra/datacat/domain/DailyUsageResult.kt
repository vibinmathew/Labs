package com.telstra.datacat.domain

data class DailyUsageResult(val date: String, val usage: List<CategoryResult>)