package com.telstra.datacat.adapters.external

import java.io.Serializable

data class ExternalQueueMessage(val request: ExternalSmsRequest, val retryCounter: Int = 0) : Serializable {
    companion object {
        private const val serialVersionUID = 8720537707709859994L
    }
}