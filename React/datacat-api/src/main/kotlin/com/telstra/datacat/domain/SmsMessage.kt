package com.telstra.datacat.domain

import java.io.Serializable

data class SmsMessage(val msisdn: String, val retryCounter: Int = 0) : Serializable {

    companion object {
        private const val serialVersionUID = -394356436579405L
    }
}