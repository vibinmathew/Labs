package com.telstra.datacat.domain

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate
import java.time.ZonedDateTime

data class Billing(
        @JsonSerialize() val usage: BigInteger,
        val allowance: BigInteger,
        val baseBill: BigDecimal,
        @JsonSerialize(using = ToStringSerializer::class) val start: LocalDate,
        @JsonSerialize(using = ToStringSerializer::class) val end: LocalDate,
        @JsonSerialize(using = ToStringSerializer::class) val updatedAt: ZonedDateTime,
        val nextBillingCycleUrl: String,
        val previousBillingCycleUrl: String
)