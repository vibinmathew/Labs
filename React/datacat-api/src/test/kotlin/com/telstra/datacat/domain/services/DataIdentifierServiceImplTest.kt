package com.telstra.datacat.domain.services

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.telstra.datacat.domain.DataIdentifier
import com.telstra.datacat.domain.gateways.DataIdentifierGateway
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.time.Clock
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

class DataIdentifierServiceImplTest {

    val dataIdentifierGateway: DataIdentifierGateway = mock()
    val tokenGenerator: TokenGenerator = mock()
    val clock = Clock.fixed(ZonedDateTime.of(2017, 2, 23, 10, 15, 30, 0, ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault())

    val dataIdentifierService = DataIdentifierServiceImpl(dataIdentifierGateway, tokenGenerator, clock)

    @Test
    fun loadFromToken() {
        val dataIdentifier = DataIdentifier("token123", "0465123245", LocalDate.now())
        whenever(dataIdentifierGateway.load("token123"))
                .thenReturn(dataIdentifier)

        assertThat(dataIdentifierService.loadFromToken("token123")).isEqualTo(dataIdentifier)
    }

    @Test
    fun saveToken() {
        whenever(tokenGenerator.generate()).thenReturn("token123")

        assertThat(dataIdentifierService.generateToken("0465789845")).isEqualTo("token123")

        verify(tokenGenerator).generate()
        verify(dataIdentifierGateway).saveToken("token123", "0465789845", LocalDate.parse("2017-02-23"))
    }
}