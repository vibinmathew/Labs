package com.telstra.datacat.adapters.databases

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed
import java.io.Serializable
import java.time.LocalDate

@RedisHash("dataIdentifiers")
data class DataIdentifierRow(@Id @Indexed val token: String,
                             val msisdn: String,
                             val date: LocalDate) : Serializable {

    companion object {
        private const val serialVersionUID = -3979903098103059405L
    }

}