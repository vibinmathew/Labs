package com.telstra.datacat.adapters.databases.repositories

import com.telstra.datacat.adapters.databases.DataIdentifierRepository
import com.telstra.datacat.adapters.databases.DataIdentifierRow
import com.telstra.datacat.adapters.databases.MsisdnFilterRepository
import com.telstra.datacat.domain.exceptions.ResourceNotFoundException
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit


@Component
class RedisRepository(private val template: RedisTemplate<String, DataIdentifierRow>,
                      private val msisdnFilterTemplate: RedisTemplate<String, Boolean>) : DataIdentifierRepository {
    override fun saveDataIdentifier(dataIdentifierRow: DataIdentifierRow) {
        template.opsForValue().set(dataIdentifierRow.token, dataIdentifierRow)
        template.expire(dataIdentifierRow.token, 90, TimeUnit.DAYS)
    }

    override fun findByToken(token: String): DataIdentifierRow {
        try {
            return template.opsForValue().get(token)
        } catch(e: IllegalStateException) {
            throw ResourceNotFoundException()
        }
    }
}