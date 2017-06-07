package com.telstra.datacat.domain.services

interface TokenGenerator {
    fun generate(): String
}