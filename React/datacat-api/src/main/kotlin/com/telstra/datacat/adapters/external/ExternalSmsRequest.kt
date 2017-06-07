package com.telstra.datacat.adapters.external

import java.io.Serializable

data class ExternalSmsRequest(val to: String, val body: String) : Serializable