package com.telstra.datacat.domain.services


import org.assertj.core.api.Assertions.assertThat
import org.junit.Test


class Base64TokenGeneratorTest {

    val tokenGenerator = Base64TokenGenerator()

    @Test
    fun generate() {
        assertThat(tokenGenerator.generate()).isNotEmpty()
    }
}