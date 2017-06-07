package com.telstra.datacat.adapters.databases

import java.time.LocalDate

interface DataCategorisationRepository {
    fun fetchLastMonthOfData(msisdn: String, from: LocalDate, to: LocalDate): List<DayDataRow>
}