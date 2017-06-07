package com.telstra.datacat.domain.services.converters

import com.telstra.datacat.adapters.databases.repositories.DataCategories.FileSharing
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

class DataCategorizationBreakdownConverterTest {

    val converter: DataCategorizationBreakdownConverter = DataCategorizationBreakdownConverter()

    @Test
    fun convert() {
        val dayResults: MutableList<DayResult> = asList(
                DayResult(LocalDate.of(2017, Month.FEBRUARY, 21), asList(
                        CategoryResult(Streaming, BigInteger("30")),
                        CategoryResult(Social, BigInteger("20"))
                )),
                DayResult(LocalDate.of(2017, Month.FEBRUARY, 20), asList(
                        CategoryResult(Streaming, BigInteger("20")),
                        CategoryResult(General, BigInteger("30"))
                ))
        ).toMutableList()
        val dataCategorisationResult = DataCategorisationResult(dayResults)


        val result = converter.convert(dataCategorisationResult, 3)

        val expectedResult = asList(
                CategoryResult(Streaming, BigInteger("50")),
                CategoryResult(General, BigInteger("30")),
                CategoryResult(Social, BigInteger("20")),
                CategoryResult(Others, BigInteger.ZERO)
        )

        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun convert_alwaysHaveOthers() {
        val dayResults = asList(
                DayResult(LocalDate.of(2017, Month.FEBRUARY, 21), asList(
                        CategoryResult(Streaming, BigInteger("30")),
                        CategoryResult(Social, BigInteger("20"))
                )),
                DayResult(LocalDate.of(2017, Month.FEBRUARY, 20), asList(
                        CategoryResult(Streaming, BigInteger("20")),
                        CategoryResult(General, BigInteger("30"))
                ))
        )
        val dataCategorisationResult = DataCategorisationResult(dayResults)

        val result = converter.convert(dataCategorisationResult, 1)

        assertThat(result.size).isEqualTo(2)
        assertThat(result.last()).isEqualTo(CategoryResult(Others, BigInteger("50")))
    }

    @Test
    fun convert_ignoresUnwanted() {
        val dayResults = asList(
                DayResult(LocalDate.of(2017, Month.FEBRUARY, 21), asList(
                        CategoryResult(VOIP, BigInteger("10")),
                        CategoryResult(Uncategorised, BigInteger("10")),
                        CategoryResult(Marketing, BigInteger("10")),
                        CategoryResult(Streaming, BigInteger("15")),
                        CategoryResult(Social, BigInteger("5"))
                )),
                DayResult(LocalDate.of(2017, Month.FEBRUARY, 20), asList(
                        CategoryResult(Streaming, BigInteger("20")),
                        CategoryResult(General, BigInteger("30"))
                ))
        )
        val dataCategorisationResult = DataCategorisationResult(dayResults)


        val result = converter.convert(dataCategorisationResult, 3)

        val expectedResult = asList(
                CategoryResult(Streaming, BigInteger("35")),
                CategoryResult(General, BigInteger("30")),
                CategoryResult(Social, BigInteger("5")),
                CategoryResult(Others, BigInteger("30"))
        )

        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun convert_withFewerElementsThanRequired() {
        val dayResults: List<DayResult> = asList(
                DayResult(LocalDate.of(2017, Month.FEBRUARY, 21), asList(

                )),
                DayResult(LocalDate.of(2017, Month.FEBRUARY, 20), asList(

                ))
        )
        val dataCategorisationResult = DataCategorisationResult(dayResults)

        val result = converter.convert(dataCategorisationResult, 3)

        assertThat(result).isEqualTo(asList(CategoryResult(Others, BigInteger("100"))))
    }

    @Test
    fun convert_whenNoData() {
        val dayResults: List<DayResult> = asList()
        val dataCategorisationResult = DataCategorisationResult(dayResults)

        val result = converter.convert(dataCategorisationResult, 3)

        assertThat(result).isEqualTo(asList(CategoryResult(Others, BigInteger("100"))))
    }

    @Test
    fun convert_mergesCategories() {
        val dayResults = asList(
                DayResult(LocalDate.of(2017, Month.FEBRUARY, 21), asList(
                        CategoryResult(FileSharing, BigInteger("13")),
                        CategoryResult(General, BigInteger("17")),
                        CategoryResult(Streaming, BigInteger("15")),
                        CategoryResult(Social, BigInteger("5"))
                )),
                DayResult(LocalDate.of(2017, Month.FEBRUARY, 20), asList(
                        CategoryResult(Streaming, BigInteger("20")),
                        CategoryResult(General, BigInteger("30"))
                ))
        )
        val dataCategorisationResult = DataCategorisationResult(dayResults)


        val result = converter.convert(dataCategorisationResult, 4)

        val expectedResult = asList(
                CategoryResult(General, BigInteger("60")),
                CategoryResult(Streaming, BigInteger("35")),
                CategoryResult(Social, BigInteger("5")),
                CategoryResult(Others, BigInteger("0"))
        )

        assertThat(result).isEqualTo(expectedResult)
    }
}
