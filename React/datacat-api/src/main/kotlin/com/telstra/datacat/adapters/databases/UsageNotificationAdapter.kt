package com.telstra.datacat.adapters.databases

import com.telstra.datacat.domain.gateways.UsageNotificationGateway
import org.springframework.stereotype.Component
import java.util.*

@Component
class UsageNotificationAdapter(val repository: UsageNotificationRepository) : UsageNotificationGateway {
    override fun msisdnToNotify(): List<String> {
        val notifications = repository.fetchUsageNotifications()
        return notifications.mapTo(ArrayList()) {
            it.msisdn.replace("'", "").replace(Regex("^(4|04|00614|\\+614)"), "614")
        }
    }
}