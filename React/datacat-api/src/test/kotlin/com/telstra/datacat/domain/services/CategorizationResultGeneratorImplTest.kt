package com.telstra.datacat.domain.services

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.telstra.datacat.adapters.databases.repositories.DataCategories.FileSharing
import com.telstra.datacat.adapters.databases.repositories.DataCategories.Gaming
import com.telstra.datacat.adapters.databases.repositories.DataCategories.Social
import com.telstra.datacat.adapters.databases.repositories.DataCategories.Streaming
import com.telstra.datacat.domain.Billing
import com.telstra.datacat.domain.CategorizationResult
import com.telstra.datacat.domain.CategoryResult
import com.telstra.datacat.domain.DailyUsageResult
import com.telstra.datacat.domain.DataCategorisationResult
import com.telstra.datacat.domain.gateways.BillingGateway
import com.telstra.datacat.domain.gateways.DataCategorisationGateway
import com.telstra.datacat.domain.services.converters.Converter
import com.telstra.datacat.domain.services.converters.DailyUsageBreakdownConverter
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.Arrays.asList

class CategorizationResultGeneratorImplTest {

    val dataCatGateway: DataCategorisationGateway = mock()
    val billingGateway: BillingGateway = mock()
    val converter: Converter = mock()
    val dailyUsageBreakdownConverter: DailyUsageBreakdownConverter = mock()

    val categorizationResultGenerator = CategorizationResultGeneratorImpl(
            dataCatGateway,
            converter,
            dailyUsageBreakdownConverter,
            billingGateway
    )

    @Before
    fun setup() {
        reset(dataCatGateway)
    }

    @Test
    fun generate() {
        val dataCategorisationResult = DataCategorisationResult(emptyList())

        val billing = Billing(
                BigInteger("120"),
                BigInteger("100"),
                BigDecimal("49.95"),
                LocalDate.parse("2017-02-15"),
                LocalDate.parse("2017-03-14"),
                ZonedDateTime.parse("2017-03-11T08:50Z", DateTimeFormatter.ISO_ZONED_DATE_TIME),
                "",
                ""
        )

        whenever(billingGateway.billing("321654987", LocalDate.parse("2017-02-25"), 0))
                .thenReturn(billing)

        whenever(dataCatGateway.lastMonth("321654987", LocalDate.parse("2017-02-15"), LocalDate.parse("2017-03-14")))
                .thenReturn(dataCategorisationResult)

        val list = asList(
                CategoryResult(Streaming, BigInteger("15")),
                CategoryResult(Social, BigInteger("5")),
                CategoryResult(Gaming, BigInteger("5")),
                CategoryResult(FileSharing, BigInteger("5"))
        )

        whenever(converter.convert(dataCategorisationResult, 4)).thenReturn(list)

        val dayUsageList = Collections.emptyList<DailyUsageResult>()

        whenever(dailyUsageBreakdownConverter.convert(dataCategorisationResult, asList(
                Streaming,
                Social,
                Gaming,
                FileSharing
        )))
                .thenReturn(dayUsageList)

        Assertions.assertThat(categorizationResultGenerator.generate("321654987", LocalDate.parse("2017-02-25")))
                .isEqualTo(CategorizationResult(list, dayUsageList))

        verify(dataCatGateway).lastMonth("321654987", LocalDate.parse("2017-02-15"), LocalDate.parse("2017-03-14"))
        verify(billingGateway).billing("321654987", LocalDate.parse("2017-02-25"), 0, "")
    }
}