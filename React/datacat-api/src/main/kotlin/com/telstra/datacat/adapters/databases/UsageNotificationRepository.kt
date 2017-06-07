package com.telstra.datacat.adapters.databases

interface UsageNotificationRepository {
    fun fetchUsageNotifications(): List<UsageNotificationRow>
}