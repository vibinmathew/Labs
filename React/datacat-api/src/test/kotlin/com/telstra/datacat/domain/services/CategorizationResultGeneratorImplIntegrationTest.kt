package com.telstra.datacat.domain.services

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.telstra.datacat.adapters.databases.repositories.DataCategories.FileSharing
import com.telstra.datacat.adapters.databases.repositories.DataCategories.Gaming
import com.telstra.datacat.adapters.databases.repositories.DataCategories.General
import com.telstra.datacat.adapters.databases.repositories.DataCategories.Marketing
import com.telstra.datacat.adapters.databases.repositories.DataCategories.Others
import com.telstra.datacat.adapters.databases.repositories.DataCategories.Social
import com.telstra.datacat.adapters.databases.repositories.DataCategories.Streaming
import com.telstra.datacat.adapters.databases.repositories.DataCategories.Uncategorised
import com.telstra.datacat.adapters.databases.repositories.DataCategories.VOIP
import com.telstra.datacat.domain.Billing
import com.telstra.datacat.domain.CategoryResult
import com.telstra.datacat.domain.DataCategorisationResult
import com.telstra.datacat.domain.DayResult
import com.telstra.datacat.domain.gateways.BillingGateway
import com.telstra.datacat.domain.gateways.DataCategorisationGateway
import com.telstra.datacat.domain.services.converters.DailyUsageBreakdownConverterImpl
import com.telstra.datacat.domain.services.converters.DataCategorizationBreakdownConverter
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Arrays.asList

class CategorizationResultGeneratorImplIntegrationTest {

    val dataCatGateway: DataCategorisationGateway = mock()
    val billingGateway: BillingGateway = mock()

    val categorizationResultGenerator = CategorizationResultGeneratorImpl(
            dataCatGateway,
            DataCategorizationBreakdownConverter(),
            DailyUsageBreakdownConverterImpl(),
            billingGateway
    )

    @Before
    fun setup() {
        reset(dataCatGateway)
    }

    @Test
    fun generate() {
        val dayResults = asList(
                DayResult(LocalDate.parse("2017-02-16"), asList(
                        CategoryResult(VOIP, BigInteger("10")),
                        CategoryResult(Uncategorised, BigInteger("10")),
                        CategoryResult(Marketing, BigInteger("12")),
                        CategoryResult(FileSharing, BigInteger("3")),
                        CategoryResult(Streaming, BigInteger("15")),
                        CategoryResult(Social, BigInteger("6"))
                )),
                DayResult(LocalDate.parse("2017-02-17"), asList(
                        CategoryResult(FileSharing, BigInteger("7")),
                        CategoryResult(Streaming, BigInteger("20")),
                        CategoryResult(Gaming, BigInteger("4")),
                        CategoryResult(General, BigInteger("13"))
                ))
        )
        val dataCategorisationResult = DataCategorisationResult(dayResults)

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

        val actual = categorizationResultGenerator.generate("321654987", LocalDate.parse("2017-02-25"))

        assertThat(actual.overall).containsExactlyInAnyOrder(
                CategoryResult(Streaming, BigInteger("35")),
                CategoryResult(General, BigInteger("23")),
                CategoryResult(Social, BigInteger("6")),
                CategoryResult(Gaming, BigInteger("4")),
                CategoryResult(Others, BigInteger("32"))
        )

        val day1 = actual.dailyUsage[0]
        assertThat(day1.date).isEqualTo("2017-02-16")
        assertThat(day1.usage).containsExactlyInAnyOrder(
                CategoryResult(Others, BigInteger("32")),
                CategoryResult(Streaming, BigInteger("15")),
                CategoryResult(General, BigInteger("3")),
                CategoryResult(Social, BigInteger("6"))
        )

        val day2 = actual.dailyUsage[1]
        assertThat(day2.date).isEqualTo("2017-02-17")
        assertThat(day2.usage).containsExactlyInAnyOrder(
                CategoryResult(Streaming, BigInteger("20")),
                CategoryResult(General, BigInteger("20")),
                CategoryResult(Gaming, BigInteger("4")),
                CategoryResult(Others, BigInteger("0"))
        )

        verify(dataCatGateway).lastMonth("321654987", LocalDate.parse("2017-02-15"), LocalDate.parse("2017-03-14"))
        verify(billingGateway).billing("321654987", LocalDate.parse("2017-02-25"), 0, "")
    }
}