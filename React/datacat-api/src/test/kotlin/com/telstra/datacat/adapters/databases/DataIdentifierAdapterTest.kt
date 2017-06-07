package com.telstra.datacat.adapters.databases

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.telstra.datacat.domain.DataIdentifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.time.LocalDate

class DataIdentifierAdapterTest {

    val repository: DataIdentifierRepository = mock()

    val dataIdentifierAdapter = DataIdentifierAdapter(repository)

    @Test
    fun load_shouldReturnDataFromToken() {
        val date = LocalDate.parse("2017-03-23")
        whenever(repository.findByToken("token123")).thenReturn(DataIdentifierRow("token123", "068745744", date))

        assertThat(dataIdentifierAdapter.load("token123"))
                .isEqualTo(DataIdentifier("token123", "068745744", date))
    }

    @Test
    fun save_shouldPersistTheToken() {
        assertThat(dataIdentifierAdapter.saveToken("token123", "068745744", LocalDate.parse("2017-03-23")))

        verify(repository).saveDataIdentifier(DataIdentifierRow("token123", "068745744", LocalDate.parse("2017-03-23")))
    }
}