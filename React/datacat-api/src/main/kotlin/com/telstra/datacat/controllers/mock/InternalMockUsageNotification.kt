package com.telstra.datacat.controllers.mock

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Profile("internal")
@Component
class InternalMockUsageNotification() : MockUsageNotification {
    override fun create() {
    }

    override fun fill(paramsNotify: MutableMap<String, Any>) {
    }

    override fun clear(parameters: MutableMap<String, Any>): Int {
        return 0
    }

    override fun drop() {
    }
}