package com.telstra.datacat.domain.services

import org.apache.commons.codec.binary.Base64
import org.springframework.stereotype.Component
import java.nio.ByteBuffer
import java.util.*

@Component
class Base64TokenGenerator: TokenGenerator {
    override fun generate(): String {
        val uuid = UUID.randomUUID()
        val bb = ByteBuffer.wrap(ByteArray(16))
        bb.putLong(uuid.mostSignificantBits)
        bb.putLong(uuid.leastSignificantBits)
        val base64 = Base64(0, null, true)
        val token = base64.encodeAsString(bb.array())
        return token
    }
}