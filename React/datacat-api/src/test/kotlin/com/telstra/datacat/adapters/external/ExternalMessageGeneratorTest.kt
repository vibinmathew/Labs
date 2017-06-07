package com.telstra.datacat.adapters.external

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.telstra.datacat.adapters.databases.repositories.DataCategories
import com.telstra.datacat.domain.CategorizationResult
import com.telstra.datacat.domain.CategoryResult
import com.telstra.datacat.domain.DailyUsageResult
import com.telstra.datacat.domain.services.CategorizationResultGenerator
import com.telstra.datacat.domain.services.DataIdentifierService
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.math.BigInteger
import java.time.Clock
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

class ExternalMessageGeneratorTest {

    val categorizationResultGenerator: CategorizationResultGenerator = mock()
    val dataIdentifierService: DataIdentifierService = mock()
    val clock = Clock.fixed(ZonedDateTime.of(2017, 3, 12, 15, 30, 0, 0, ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault())

    val externalMessageGenerator: ExternalMessageGenerator = ExternalMessageGenerator(
            "http://example.com/1",
            dataIdentifierService,
            categorizationResultGenerator,
            clock
    )

    @Test
    fun generate() {
        val cats = Arrays.asList(
                CategoryResult(DataCategories.Streaming, BigInteger("50")),
                CategoryResult(DataCategories.Social, BigInteger("25")),
                CategoryResult(DataCategories.Gaming, BigInteger("15")),
                CategoryResult(DataCategories.FileSharing, BigInteger("5"))
        )

        whenever(categorizationResultGenerator.generate("321654987", LocalDate.now(clock)))
                .thenReturn(CategorizationResult(cats, Collections.emptyList<DailyUsageResult>()))

        whenever(dataIdentifierService.generateToken("321654987"))
                .thenReturn("token123")

        assertThat(externalMessageGenerator.generate("321654987"))
                .isEqualTo(ExternalSmsRequest("321654987", "Telstra Data usage breakdown:\n" +
                        "Streaming Video & Audio (50%)\n" +
                        "More information: http://example.com/1/usage/token123"))

        verify(categorizationResultGenerator).generate("321654987", LocalDate.parse("2017-03-12"))
    }
}