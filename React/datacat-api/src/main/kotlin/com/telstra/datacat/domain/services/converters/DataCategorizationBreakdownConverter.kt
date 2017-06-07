package com.telstra.datacat.domain.services.converters

import com.telstra.datacat.adapters.databases.repositories.DataCategories
import com.telstra.datacat.domain.CategoryResult
import com.telstra.datacat.domain.DataCategorisationResult
import org.springframework.stereotype.Component
import java.math.BigInteger
import java.util.*

@Component
class DataCategorizationBreakdownConverter : Converter {
    override fun convert(input: DataCategorisationResult, noOfCategories: Int): List<CategoryResult> {

        val categoryAmounts: MutableMap<DataCategories, BigInteger> = HashMap()
        var totalData = BigInteger.ZERO

        input.dayResults.forEach { day ->
            day.categoryResults.forEach { cr ->
                val category = cr.category.parentCategory()

                val amount = categoryAmounts[category]
                if (amount != null) {
                    categoryAmounts.put(category, amount.add(cr.amount))
                } else {
                    categoryAmounts.put(category, cr.amount)
                }
                totalData = totalData.add(cr.amount)
            }
        }

        val percentMap = categoryAmounts.map { cat -> CategoryResult(cat.key, percent(cat.value, totalData)) }

        val allCategoriesList = percentMap.toList().sortedWith(compareByDescending { it.amount })
        val filteredList = allCategoriesList.filter { it.category.hidden.not() }
        val list = filteredList.subList(0, (if (filteredList.size < noOfCategories) filteredList.size else noOfCategories)).toMutableList()
        val total: BigInteger = list.fold(BigInteger.ZERO) { current, next -> next.amount.add(current) }
        list.add(CategoryResult(DataCategories.Others, BigInteger("100").minus(total)))

        return list
    }

    private fun percent(second: BigInteger, totalData: BigInteger) =
            second.multiply(BigInteger.valueOf(100)).divide(totalData)
}



