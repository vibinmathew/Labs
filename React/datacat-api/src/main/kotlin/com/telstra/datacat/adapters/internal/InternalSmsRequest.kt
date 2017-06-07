package com.telstra.datacat.adapters.internal

import java.io.Serializable


data class InternalSmsRequest(val notificationEventType: String, val entityType: String,
                              val recipient: Recipient, val contentParameters: List<ContentParameter>) : Serializable {

    data class Recipient(val msisdn: String) : Serializable

    data class ContentParameter(val name: String, var value: String = "") : Serializable
}

