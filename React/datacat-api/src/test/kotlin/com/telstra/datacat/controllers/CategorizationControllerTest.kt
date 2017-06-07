package com.telstra.datacat.controllers

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.telstra.datacat.adapters.databases.repositories.DataCategories.Antivirus
import com.telstra.datacat.adapters.databases.repositories.DataCategories.Gaming
import com.telstra.datacat.adapters.databases.repositories.DataCategories.Social
import com.telstra.datacat.adapters.databases.repositories.DataCategories.Streaming
import com.telstra.datacat.adapters.databases.repositories.DataCategories.SystemUpdates
import com.telstra.datacat.domain.CategorizationResult
import com.telstra.datacat.domain.CategoryResult
import com.telstra.datacat.domain.DailyUsageResult
import com.telstra.datacat.domain.DataIdentifier
import com.telstra.datacat.domain.services.CategorizationResultGenerator
import com.telstra.datacat.domain.services.DataIdentifierService
import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.hamcrest.Matchers.equalTo
import org.junit.Before
import org.junit.Test
import java.math.BigInteger
import java.time.LocalDate
import java.util.Arrays.asList

class CategorizationControllerTest {

    val categorizationResultGenerator: CategorizationResultGenerator = mock()
    val dataIdentifierService: DataIdentifierService = mock()

    @Before
    fun setup() {
        RestAssuredMockMvc.standaloneSetup(CategorizationController(categorizationResultGenerator, dataIdentifierService))
    }

    @Test
    fun categorizationEndpoint() {
        val list = asList(
                CategoryResult(Gaming, BigInteger("30")),
                CategoryResult(Social, BigInteger("20")),
                CategoryResult(Antivirus, BigInteger("7")),
                CategoryResult(SystemUpdates, BigInteger("1"))
        )

        val dayUsageList = asList(
                DailyUsageResult("2017-02-18", asList(CategoryResult(Streaming, BigInteger("30")))),
                DailyUsageResult("2017-02-17", asList(CategoryResult(Social, BigInteger("40")))),
                DailyUsageResult("2017-02-16", asList(CategoryResult(SystemUpdates, BigInteger("50"))))
        )

        val date = LocalDate.now()
        whenever(categorizationResultGenerator.generate("0245678912", date))
                .thenReturn(CategorizationResult(list, dayUsageList))
        whenever(dataIdentifierService.loadFromToken("token123")).thenReturn(DataIdentifier("token123", "0245678912", date))

        RestAssuredMockMvc.`when`()
                .get("/api/categorization/token123")
                .then()
                .body("overall[0].category", equalTo("Gaming"))
                .body("overall[0].amount", equalTo(30))
                .body("overall[1].category", equalTo("Social Media"))
                .body("overall[1].amount", equalTo(20))
                .body("overall[2].category", equalTo("Antivirus"))
                .body("overall[2].amount", equalTo(7))
                .body("overall[3].category", equalTo("System & Application Updates"))
                .body("overall[3].amount", equalTo(1))
                .body("dailyUsage[0].date", equalTo("2017-02-18"))
                .body("dailyUsage[0].usage[0].category", equalTo(Streaming.getCategoryText()))
                .body("dailyUsage[0].usage[0].amount", equalTo(30))
                .body("dailyUsage[1].date", equalTo("2017-02-17"))
                .body("dailyUsage[1].usage[0].category", equalTo(Social.getCategoryText()))
                .body("dailyUsage[1].usage[0].amount", equalTo(40))
                .body("dailyUsage[2].date", equalTo("2017-02-16"))
                .body("dailyUsage[2].usage[0].category", equalTo(SystemUpdates.getCategoryText()))
                .body("dailyUsage[2].usage[0].amount", equalTo(50))

    }

    @Test
    fun categorizationEndpoint_shouldHandleIndexInPast() {
        val list = asList(
                CategoryResult(Gaming, BigInteger("30")),
                CategoryResult(Social, BigInteger("20")),
                CategoryResult(Antivirus, BigInteger("7")),
                CategoryResult(SystemUpdates, BigInteger("1"))
        )

        val dayUsageList = asList(
                DailyUsageResult("2017-02-18", asList(CategoryResult(Streaming, BigInteger("30")))),
                DailyUsageResult("2017-02-17", asList(CategoryResult(Social, BigInteger("40")))),
                DailyUsageResult("2017-02-16", asList(CategoryResult(SystemUpdates, BigInteger("50"))))
        )

        val date = LocalDate.now()
        whenever(categorizationResultGenerator.generate("0245678912", date, 1))
                .thenReturn(CategorizationResult(list, dayUsageList))
        whenever(dataIdentifierService.loadFromToken("token123")).thenReturn(DataIdentifier("token123", "0245678912", date))

        RestAssuredMockMvc.`when`()
                .get("/api/categorization/token123/1")
                .then()
                .statusCode(200)
    }
}
