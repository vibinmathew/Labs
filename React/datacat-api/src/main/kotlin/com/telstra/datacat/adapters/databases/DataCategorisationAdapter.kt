package com.telstra.datacat.adapters.databases

import com.telstra.datacat.adapters.databases.repositories.DataCategories
import com.telstra.datacat.domain.CategoryResult
import com.telstra.datacat.domain.DataCategorisationResult
import com.telstra.datacat.domain.DayResult
import com.telstra.datacat.domain.gateways.DataCategorisationGateway
import org.springframework.stereotype.Component
import java.math.BigInteger
import java.time.LocalDate
import java.util.*

@Component
class DataCategorisationAdapter(val repository: DataCategorisationRepository) : DataCategorisationGateway {

    override fun lastMonth(msisdn: String, from: LocalDate, to: LocalDate): DataCategorisationResult {
        val dayResultsByDate: MutableMap<String, MutableList<CategoryResult>> = HashMap()

        val dayDataRows = repository.fetchLastMonthOfData(msisdn, from, to)
        for ((day, catId, amount) in dayDataRows) {
            var dayResult = dayResultsByDate[day]
            if (dayResult == null) {
                dayResult = ArrayList()
                dayResultsByDate.put(day, dayResult)
            }
            if (DataCategories.getById(catId)?.required ?: false) {
                dayResult.add(CategoryResult(DataCategories.getById(catId)!!, BigInteger(amount)))
            }
        }

        val dayResults: MutableList<DayResult> = dayResultsByDate.entries.mapTo(ArrayList()) { DayResult(LocalDate.parse(it.key), it.value) }
        return DataCategorisationResult(dayResults)
    }
}