package com.telstra.datacat.domain.services

interface MessageGenerator<T> {
    fun generate(msisdn: String): T
}