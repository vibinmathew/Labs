package com.telstra.datacat.domain.services

interface SmsService {
    fun send(msisdn: String) : Boolean
}