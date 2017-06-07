package com.telstra.datacat.adapters.databases

import com.telstra.datacat.domain.Billing
import com.telstra.datacat.domain.gateways.BillingGateway
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.BigInteger
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

@Component
class BillingAdapter(val billingRepository: BillingRepository) : BillingGateway {
    override fun billing(msisdn: String, date: LocalDate, index: Int, token: String): Billing {


        val defaultBillingRow = BillingRow("0", "0", "0",
                Timestamp.from(LocalDateTime.now().minusMonths(1).toInstant(ZoneOffset.UTC)),
                Timestamp.from(Instant.now()),
                Timestamp.from(Instant.now()))

        val billingRow = billingRepository.fetchBilling(msisdn, date, index) ?:  defaultBillingRow

        val nextBillingCycle = billingRepository.fetchBilling(msisdn, date, index + 1)
        val prevBillingCycle = billingRepository.fetchBilling(msisdn, date, index - 1)

        val nextBillingUrl = if (nextBillingCycle == null) "" else "$token/${index + 1}"
        val prevBillingUrl = if (prevBillingCycle == null) "" else "$token/${index - 1}"

        val usage = BigInteger(billingRow.usage)
        val allowance = BigInteger(billingRow.allowance)
        val baseBill = BigDecimal(billingRow.baseBill)
        val start = billingRow.start.toLocalDateTime().toLocalDate()
        val end = billingRow.end.toLocalDateTime().toLocalDate().minusDays(1)
        val updatedAt = billingRow.updatedAt.toLocalDateTime().atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneOffset.UTC)

        return Billing(usage, allowance, baseBill, start, end, updatedAt, nextBillingUrl, prevBillingUrl)
    }
}