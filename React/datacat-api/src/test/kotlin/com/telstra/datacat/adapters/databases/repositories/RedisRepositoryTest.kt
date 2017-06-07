package com.telstra.datacat.adapters.databases.repositories

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.telstra.datacat.adapters.databases.DataIdentifierRow
import com.telstra.datacat.domain.exceptions.ResourceNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import java.time.LocalDate
import java.util.concurrent.TimeUnit


class RedisRepositoryTest {

    val redisTemplate: RedisTemplate<String, DataIdentifierRow> = mock()
    val valueOps: ValueOperations<String, DataIdentifierRow> = mock()

    val msisdnFilterTemplate: RedisTemplate<String, Boolean> = mock()
    val valueOpsMsisdnFilter: ValueOperations<String, Boolean> = mock()

    val redisRepository = RedisRepository(redisTemplate, msisdnFilterTemplate)

    @Before
    fun setup() {
        whenever(redisTemplate.opsForValue()).thenReturn(valueOps)
        whenever(msisdnFilterTemplate.opsForValue()).thenReturn(valueOpsMsisdnFilter)
    }

    @After
    fun afterEachTest() {
        Mockito.reset(redisTemplate)
        Mockito.reset(valueOps)
    }

    @Test
    fun save_persists() {
        val row = DataIdentifierRow("abc", "04122556677", LocalDate.now())

        redisRepository.saveDataIdentifier(row)

        verify(redisTemplate).opsForValue()
        verify(valueOps).set(row.token, row)
        verify(redisTemplate).expire(row.token, 90, TimeUnit.DAYS)
    }

    @Test
    fun findByToken_returnsDataId() {
        val row = DataIdentifierRow("abc", "04122556677", LocalDate.now())

        whenever(valueOps.get(row.token)).thenReturn(row)

        assertThat(redisRepository.findByToken(row.token)).isEqualTo(row)

        verify(redisTemplate).opsForValue()
        verify(valueOps).get(row.token)
    }

    @Test
    fun findByToken_whenTrowingException() {
        val row = DataIdentifierRow("abc", "04122556677", LocalDate.now())

        whenever(valueOps.get(row.token)).thenThrow(IllegalStateException())

        try {
            redisRepository.findByToken(row.token)
            fail("Should have thrown an exception")
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(ResourceNotFoundException::class.java)
        }
    }


}