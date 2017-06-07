package com.telstra.datacat.domain

import java.time.LocalDate

data class DayResult(val date : LocalDate, val categoryResults: List<CategoryResult>)