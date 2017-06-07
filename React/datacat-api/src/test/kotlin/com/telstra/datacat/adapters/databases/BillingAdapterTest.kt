package com.telstra.datacat.adapters.databases

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.telstra.datacat.domain.Billing
import com.telstra.datacat.domain.exceptions.ResourceNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore
import org.junit.Test
import java.math.BigDecimal
import java.math.BigInteger
import java.sql.Timestamp
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset

class BillingAdapterTest {

    val repository: BillingRepository = mock()

    val adapter = BillingAdapter(repository)

    @Test
    fun billing_shouldReturnUserBilling() {
        val billingRow = BillingRow(
                "6000000",
                "5700000",
                "45.95",
                Timestamp.valueOf("2017-05-15 00:00:00"),
                Timestamp.valueOf("2017-06-15 00:00:00"),
                Timestamp.valueOf("2017-03-14 08:50:00")
        )
        whenever(repository.fetchBilling("0478895612", LocalDate.parse("2017-05-25"), 0)).thenReturn(billingRow)
        whenever(repository.fetchBilling("0478895612", LocalDate.parse("2017-05-25"), 1)).thenReturn(billingRow)
        whenever(repository.fetchBilling("0478895612", LocalDate.parse("2017-05-25"), -1)).thenReturn(billingRow)

        val billing = Billing(
                BigInteger("6000000"),
                BigInteger("5700000"),
                BigDecimal("45.95"),
                LocalDate.parse("2017-05-15"),
                LocalDate.parse("2017-06-14"),
                Timestamp.valueOf("2017-03-14 08:50:00").toLocalDateTime().atZone(ZoneId.systemDefault())
                        .withZoneSameInstant(ZoneOffset.UTC),
                "token123/1",
                "token123/-1"
        )

        assertThat(adapter.billing("0478895612", LocalDate.parse("2017-05-25"), 0, "token123")).isEqualTo(billing)

        verify(repository).fetchBilling("0478895612", LocalDate.parse("2017-05-25"), 0)
        verify(repository).fetchBilling("0478895612", LocalDate.parse("2017-05-25"), 1)
        verify(repository).fetchBilling("0478895612", LocalDate.parse("2017-05-25"), -1)
    }

    @Test
    fun billing_shouldReturnNextAndPreviousBilling() {
        val billingRow = BillingRow(
                "6000000",
                "5700000",
                "45.95",
                Timestamp.valueOf("2017-05-15 00:00:00"),
                Timestamp.valueOf("2017-06-15 00:00:00"),
                Timestamp.valueOf("2017-03-14 08:50:00")
        )
        whenever(repository.fetchBilling("0478895612", LocalDate.parse("2017-05-25"), 1)).thenReturn(billingRow)
        whenever(repository.fetchBilling("0478895612", LocalDate.parse("2017-05-25"), 0)).thenReturn(billingRow)
        whenever(repository.fetchBilling("0478895612", LocalDate.parse("2017-05-25"), 2)).thenReturn(billingRow)

        val billing = Billing(
                BigInteger("6000000"),
                BigInteger("5700000"),
                BigDecimal("45.95"),
                LocalDate.parse("2017-05-15"),
                LocalDate.parse("2017-06-14"),
                Timestamp.valueOf("2017-03-14 08:50:00").toLocalDateTime().atZone(ZoneId.systemDefault())
                        .withZoneSameInstant(ZoneOffset.UTC),
                "token123/2",
                "token123/0"
        )

        assertThat(adapter.billing("0478895612", LocalDate.parse("2017-05-25"), 1, "token123")).isEqualTo(billing)
    }

    @Test
    fun billing_shouldReturnEmptyNextIfNotExisting() {
        val billingRow = BillingRow(
                "6000000",
                "5700000",
                "45.95",
                Timestamp.valueOf("2017-05-15 00:00:00"),
                Timestamp.valueOf("2017-06-15 00:00:00"),
                Timestamp.valueOf("2017-03-14 08:50:00")
        )
        whenever(repository.fetchBilling("0478895612", LocalDate.parse("2017-05-25"), 0)).thenReturn(billingRow)
        whenever(repository.fetchBilling("0478895612", LocalDate.parse("2017-05-25"), -1)).thenReturn(billingRow)
        whenever(repository.fetchBilling("0478895612", LocalDate.parse("2017-05-25"), 1)).thenReturn(null)

        val billing = Billing(
                BigInteger("6000000"),
                BigInteger("5700000"),
                BigDecimal("45.95"),
                LocalDate.parse("2017-05-15"),
                LocalDate.parse("2017-06-14"),
                Timestamp.valueOf("2017-03-14 08:50:00").toLocalDateTime().atZone(ZoneId.systemDefault())
                        .withZoneSameInstant(ZoneOffset.UTC),
                "",
                "token123/-1"
        )

        assertThat(adapter.billing("0478895612", LocalDate.parse("2017-05-25"), 0, "token123")).isEqualTo(billing)
    }

    @Test(expected = ResourceNotFoundException::class)
    @Ignore("Disabled until real billing data is available")
    fun billing_shouldThrowResourceNotFoundExceptionWhenMsisdnNotExisting() {
        whenever(repository.fetchBilling("0478895612", LocalDate.parse("2017-05-25"), 0)).thenReturn(null)
        adapter.billing("0478895612", LocalDate.parse("2017-05-25"), 0, "token123")
    }
}