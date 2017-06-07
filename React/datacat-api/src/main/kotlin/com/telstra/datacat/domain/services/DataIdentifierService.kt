package com.telstra.datacat.domain.services

import com.telstra.datacat.domain.DataIdentifier

interface DataIdentifierService {
    fun loadFromToken(token: String): DataIdentifier
    fun generateToken(msisdn: String): String
}