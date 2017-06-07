package com.telstra.datacat.domain

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import java.math.BigDecimal
import java.time.LocalDate

data class Bill(
        @JsonSerialize(using = ToStringSerializer::class) val date: LocalDate,
        val bill: BigDecimal)