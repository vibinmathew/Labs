package com.telstra.datacat.domain

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import java.math.BigInteger
import java.time.LocalDate

data class ExtraData(
        @JsonSerialize(using = ToStringSerializer::class) val date: LocalDate,
        val extraData: BigInteger)