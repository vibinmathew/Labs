package com.telstra.datacat.domain

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate


data class BillBreakdown(@JsonSerialize(using = ToStringSerializer::class) val date: LocalDate,
                         val bill: BigDecimal,
                         var baseBill: BigDecimal,
                         var extraDataCharge: BigDecimal = BigDecimal.ZERO,
                         var extraData: BigInteger = BigInteger.ZERO)