package com.telstra.datacat.domain.services.converters

import com.telstra.datacat.adapters.databases.repositories.DataCategories
import com.telstra.datacat.domain.CategoryResult
import com.telstra.datacat.domain.DailyUsageResult
import com.telstra.datacat.domain.DataCategorisationResult
import org.springframework.stereotype.Component
import java.math.BigInteger
import java.util.*

@Component
class DailyUsageBreakdownConverterImpl : DailyUsageBreakdownConverter {

    override fun convert(input: DataCategorisationResult, categories: List<DataCategories>): List<DailyUsageResult> {
        val dailyUsageList: MutableList<DailyUsageResult> = ArrayList()

        input.dayResults.forEach { day ->
            val usageMap: MutableMap<DataCategories, BigInteger> = HashMap()
            var othersUsage = BigInteger.ZERO
            day.categoryResults.forEach { cr ->
                if (categories.contains(cr.category.parentCategory())) {
                    val amount = usageMap[cr.category.parentCategory()] ?: BigInteger.ZERO
                    usageMap.put(cr.category.parentCategory(), amount.add(cr.amount))
                } else {
                    othersUsage = othersUsage.add(cr.amount)
                }
            }
            usageMap.put(DataCategories.Others, othersUsage)

            val list = usageMap.mapTo(ArrayList<CategoryResult>()) { CategoryResult(it.key, it.value) }

            dailyUsageList.add(DailyUsageResult(day.date.toString(), list))
        }

        return dailyUsageList.sortedWith(compareBy { it.date })
    }
}