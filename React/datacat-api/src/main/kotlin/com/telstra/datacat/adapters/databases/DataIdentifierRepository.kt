package com.telstra.datacat.adapters.databases

interface DataIdentifierRepository {
    fun saveDataIdentifier(dataIdentifierRow: DataIdentifierRow)
    fun findByToken(token: String): DataIdentifierRow
}