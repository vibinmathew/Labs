package com.telstra.datacat.adapters.databases

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.*

class UsageNotificationAdapterTest {

    val repository: UsageNotificationRepository = mock()
    val usageNotificationAdapter: UsageNotificationAdapter = UsageNotificationAdapter(repository)

    @Test
    fun msisdnToNotify_shouldReturnExceededDataUsers() {
        whenever(repository.fetchUsageNotifications()).thenReturn(Arrays.asList(
                UsageNotificationRow("'0404040404'"),
                UsageNotificationRow("0405050505")
        ))

        val msisdns = usageNotificationAdapter.msisdnToNotify()
        assertThat(msisdns).isEqualTo(Arrays.asList("61404040404", "61405050505"))
    }
}