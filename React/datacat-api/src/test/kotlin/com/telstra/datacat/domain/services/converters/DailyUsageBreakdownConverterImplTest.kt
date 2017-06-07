package com.telstra.datacat.domain.services.converters

import com.telstra.datacat.adapters.databases.repositories.DataCategories.FileSharing
import com.telstra.datacat.adapters.databases.repositories.DataCategories.Gaming
import com.telstra.datacat.adapters.databases.repositories.DataCategories.General
import com.telstra.datacat.adapters.databases.repositories.DataCategories.Marketing
import com.telstra.datacat.adapters.databases.repositories.DataCategories.Others
import com.telstra.datacat.adapters.databases.repositories.DataCategories.Social
import com.telstra.datacat.adapters.databases.repositories.DataCategories.Streaming
import com.telstra.datacat.adapters.databases.repositories.DataCategories.Uncategorised
import com.telstra.datacat.adapters.databases.repositories.DataCategories.VOIP
import com.telstra.datacat.domain.CategoryResult
import com.telstra.datacat.domain.DataCategorisationResult
import com.telstra.datacat.domain.DayResult
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.math.BigInteger
import java.time.LocalDate
import java.time.Month
import java.util.Arrays.asList


class DailyUsageBreakdownConverterImplTest {

    val converter: DailyUsageBreakdownConverter = DailyUsageBreakdownConverterImpl()

    @Test
    fun convert() {
        val dayResults = asList(
                DayResult(LocalDate.of(2017, Month.FEBRUARY, 21), asList(
                        CategoryResult(Streaming, BigInteger("30")),
                        CategoryResult(Social, BigInteger("20"))
                )),
                DayResult(LocalDate.of(2017, Month.FEBRUARY, 20), asList(
                        CategoryResult(Streaming, BigInteger("10")),
                        CategoryResult(General, BigInteger("30"))
                ))
        )
        val dataCategorisationResult = DataCategorisationResult(dayResults)
        val categories = asList(Social, Streaming, Others)

        val result = converter.convert(dataCategorisationResult, categories)

        val day1 = result[0]
        assertThat(day1.date).isEqualTo("2017-02-20")
        assertThat(day1.usage).containsExactlyInAnyOrder(
                CategoryResult(Streaming, BigInteger("10")),
                CategoryResult(Others, BigInteger("30"))
        )

        val day2 = result[1]
        assertThat(day2.date).isEqualTo("2017-02-21")
        assertThat(day2.usage).containsExactlyInAnyOrder(
                CategoryResult(Social, BigInteger("20")),
                CategoryResult(Streaming, BigInteger("30")),
                CategoryResult(Others, BigInteger.ZERO)
        )
    }

    @Test
    fun convert_lookUpAtParent() {
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
                        CategoryResult(Streaming, BigInteger("22")),
                        CategoryResult(Gaming, BigInteger("4")),
                        CategoryResult(General, BigInteger("13"))
                ))
        )
        val dataCategorisationResult = DataCategorisationResult(dayResults)
        val categories = asList(General, Social, Streaming, Others)

        val result = converter.convert(dataCategorisationResult, categories)

        val day1 = result[0]
        assertThat(day1.date).isEqualTo("2017-02-16")
        assertThat(day1.usage).containsExactlyInAnyOrder(
                CategoryResult(Social, BigInteger("6")),
                CategoryResult(General, BigInteger("3")),
                CategoryResult(Streaming, BigInteger("15")),
                CategoryResult(Others, BigInteger("32"))
        )

        val day2 = result[1]
        assertThat(day2.date).isEqualTo("2017-02-17")
        assertThat(day2.usage).containsExactlyInAnyOrder(
                CategoryResult(General, BigInteger("20")),
                CategoryResult(Streaming, BigInteger("22")),
                CategoryResult(Others, BigInteger("4"))
        )
    }
}