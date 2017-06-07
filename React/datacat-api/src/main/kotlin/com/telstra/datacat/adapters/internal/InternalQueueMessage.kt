package com.telstra.datacat.adapters.internal

import java.io.Serializable

data class InternalQueueMessage(val internalSmsRequest: InternalSmsRequest, val retryCounter: Int = 0) : Serializable