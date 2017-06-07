package com.telstra.datacat.controllers

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.telstra.datacat.domain.DataIdentifier
import com.telstra.datacat.domain.services.DataIdentifierService
import io.restassured.module.mockmvc.RestAssuredMockMvc
import io.restassured.module.mockmvc.RestAssuredMockMvc.`when`
import org.hamcrest.Matchers.equalTo
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class UserControllerTest {

    val dataIdentifierService: DataIdentifierService = mock()

    @Before
    fun setup() {
        RestAssuredMockMvc.standaloneSetup(UserController(dataIdentifierService))
    }

    @Test
    fun userEndpoint() {
        whenever(dataIdentifierService.loadFromToken("token123")).thenReturn(DataIdentifier("token123", "0245678912", LocalDate.now()))

        `when`()
                .post("/api/user/token123")
                .then()
                .body("msisdn", equalTo("0245678912"))
    }
}
