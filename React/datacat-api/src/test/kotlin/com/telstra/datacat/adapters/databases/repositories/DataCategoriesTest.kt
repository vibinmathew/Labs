package com.telstra.datacat.adapters.databases.repositories

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class DataCategoriesTest {

    @Test
    fun dataCategories_canBeJsonSerialized() {
        val subject = DataCategories.Social
        assertThat(ObjectMapper().writeValueAsString(subject)).isEqualTo("\"Social Media\"")
    }

    @Test
    fun dataCategories_returnsId() {
        assertThat(DataCategories.getById(4)).isEqualTo(DataCategories.FileSharing)
    }

    @Test
    fun dataCategories_returnsNullIfUnknownId() {
        assertThat(DataCategories.getById(22)).isNull()
    }
}