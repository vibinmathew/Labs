package com.telstra.datacat.controllers.mock

interface MockUsageNotification {
    fun create()
    fun fill(paramsNotify: MutableMap<String, Any>)
    fun clear(parameters: MutableMap<String, Any>): Int
    fun drop()
}