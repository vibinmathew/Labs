package com.telstra.datacat.adapters.internal

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.telstra.datacat.adapters.databases.repositories.DataCategories
import com.telstra.datacat.adapters.internal.InternalSmsRequest.ContentParameter
import com.telstra.datacat.adapters.internal.InternalSmsRequest.Recipient
import com.telstra.datacat.domain.CategorizationResult
import com.telstra.datacat.domain.CategoryResult
import com.telstra.datacat.domain.DailyUsageResult
import com.telstra.datacat.domain.services.CategorizationResultGenerator
import org.assertj.core.api.Assertions
import org.junit.Test
import java.math.BigInteger
import java.time.Clock
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import java.util.Arrays.asList


class InternalSmsMessageGeneratorTest {

    val categorizationResultGenerator: CategorizationResultGenerator = mock()
    val clock = Clock.fixed(ZonedDateTime.of(2017, 3, 12, 15, 30, 0, 0, ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault())

    val internalSmsMessageGenerator: InternalSmsMessageGenerator = InternalSmsMessageGenerator(
            categorizationResultGenerator,
            clock,
            "B2BSMSP_PILOT_2",
            "NOT_APPLICABLE",
            "StaticURL"
    )

    @Test
    fun generate() {
        val cats = asList(
                CategoryResult(DataCategories.Streaming, BigInteger("50")),
                CategoryResult(DataCategories.Social, BigInteger("25")),
                CategoryResult(DataCategories.Gaming, BigInteger("15")),
                CategoryResult(DataCategories.Others, BigInteger("5"))
        )

        whenever(categorizationResultGenerator.generate("321654987", LocalDate.now(clock)))
                .thenReturn(CategorizationResult(cats, Collections.emptyList<DailyUsageResult>()))

        val message = InternalSmsRequest(
                "B2BSMSP_PILOT_2",
                "NOT_APPLICABLE",
                Recipient("321654987"),
                asList(ContentParameter("data_type_1", "Streaming Video & Audio (50%)"),
                        ContentParameter("data_type_2", "Social Media (25%)"),
                        ContentParameter("data_type_3", "Gaming (15%)"),
                        ContentParameter("data_type_4", "Others (5%)"),
                        ContentParameter("data_type_5", ""),
                        ContentParameter("url_for_recipient", "StaticURL")
                ))

        Assertions.assertThat(internalSmsMessageGenerator.generate("321654987"))
                .isEqualTo(message)

        verify(categorizationResultGenerator).generate("321654987", LocalDate.parse("2017-03-12"))
    }
}