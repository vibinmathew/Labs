package com.telstra.datacat.domain.services

import com.telstra.datacat.adapters.databases.repositories.DataCategories
import com.telstra.datacat.domain.CategorizationResult
import com.telstra.datacat.domain.CategoryResult
import com.telstra.datacat.domain.gateways.BillingGateway
import com.telstra.datacat.domain.gateways.DataCategorisationGateway
import com.telstra.datacat.domain.services.converters.Converter
import com.telstra.datacat.domain.services.converters.DailyUsageBreakdownConverter
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class CategorizationResultGeneratorImpl(val dataCatGateway: DataCategorisationGateway,
                                        val dataCategorizationBreakdownConverter: Converter,
                                        val dailyUsageBreakdownConverter: DailyUsageBreakdownConverter,
                                        val billingGateway: BillingGateway) : CategorizationResultGenerator {

    override fun generate(msisdn: String, date: LocalDate, index: Int): CategorizationResult {
        val billing = billingGateway.billing(msisdn, date, index)
        val result = dataCatGateway.lastMonth(msisdn, billing.start, billing.end)

        val topCategoriesResult = dataCategorizationBreakdownConverter.convert(result, 4)
        val topCategories : List<DataCategories> = topCategoriesResult.map(CategoryResult::category)
        val dailyUsageList = dailyUsageBreakdownConverter.convert(result, topCategories)

        return CategorizationResult(topCategoriesResult, dailyUsageList)
    }

}