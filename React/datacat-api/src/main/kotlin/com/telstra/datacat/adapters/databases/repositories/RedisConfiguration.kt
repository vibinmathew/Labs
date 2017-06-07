package com.telstra.datacat.adapters.databases.repositories

import com.telstra.datacat.adapters.databases.DataIdentifierRow
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate

@Configuration
class RedisConfiguration {
    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory) : RedisTemplate<String, DataIdentifierRow> {
        val redisTemplate = RedisTemplate<String, DataIdentifierRow>()
        redisTemplate.connectionFactory = redisConnectionFactory
        return redisTemplate
    }

    @Bean
    fun msisdnFilterTemplate(redisConnectionFactory: RedisConnectionFactory) : RedisTemplate<String, Boolean> {
        val redisTemplate = RedisTemplate<String, Boolean>()
        redisTemplate.connectionFactory = redisConnectionFactory
        return redisTemplate
    }
}