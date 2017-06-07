package com.telstra.datacat.adapters.databases

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.telstra.datacat.adapters.databases.repositories.DataCategories.Email
import com.telstra.datacat.adapters.databases.repositories.DataCategories.FileSharing
import com.telstra.datacat.adapters.databases.repositories.DataCategories.Gaming
import com.telstra.datacat.domain.CategoryResult
import com.telstra.datacat.domain.DayResult
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.math.BigInteger
import java.time.LocalDate
import java.util.*

class DataCategorisationAdapterTest {

    val mockRepository: DataCategorisationRepository = mock()

    val adapter: DataCategorisationAdapter = DataCategorisationAdapter(mockRepository)

    @Test
    fun fetchLastMonthOfData_shouldFetchByDayCategory() {
        whenever(mockRepository.fetchLastMonthOfData("0400000000", LocalDate.parse("2017-02-15"), LocalDate.parse("2017-03-14")))
                .thenReturn(Arrays.asList(
                        DayDataRow("2017-02-15", 3, "987654321"),
                        DayDataRow("2017-02-15", 5, "8765432"),
                        DayDataRow("2017-02-16", 4, "100")
                ))

        val actualResult = adapter.lastMonth("0400000000", LocalDate.parse("2017-02-15"), LocalDate.parse("2017-03-14"))

        val day1 = DayResult(LocalDate.parse("2017-02-15"), Arrays.asList(
                CategoryResult(Email, BigInteger("987654321")),
                CategoryResult(Gaming, BigInteger("8765432"))
        ))
        val day2 = DayResult(LocalDate.parse("2017-02-16"), Arrays.asList(
                CategoryResult(FileSharing, BigInteger("100"))
        ))

        assertThat(actualResult.dayResults).containsExactlyInAnyOrder(day1, day2)

        verify(mockRepository).fetchLastMonthOfData("0400000000", LocalDate.parse("2017-02-15"), LocalDate.parse("2017-03-14"))
    }

    @Test
    fun fetchLastMonthOfData_shouldExcludeUnwantedCategories() {
        whenever(mockRepository.fetchLastMonthOfData("0400000000", LocalDate.parse("2017-02-15"), LocalDate.parse("2017-03-14")))
                .thenReturn(Arrays.asList(
                        DayDataRow("2017-02-15", 3, "987654321"),
                        DayDataRow("2017-02-15", 5, "8765432"),
                        DayDataRow("2017-02-16", 4, "100"),
                        DayDataRow("2017-02-15", 12, "948584938")
                ))

        val actualResult = adapter.lastMonth("0400000000", LocalDate.parse("2017-02-15"), LocalDate.parse("2017-03-14"))

        val day1 = DayResult(LocalDate.parse("2017-02-15"), Arrays.asList(
                CategoryResult(Email, BigInteger("987654321")),
                CategoryResult(Gaming, BigInteger("8765432"))

        ))
        val day2 = DayResult(LocalDate.parse("2017-02-16"), Arrays.asList(
                CategoryResult(FileSharing, BigInteger("100"))
        ))

        assertThat(actualResult.dayResults).containsExactlyInAnyOrder(day1, day2)
    }
}