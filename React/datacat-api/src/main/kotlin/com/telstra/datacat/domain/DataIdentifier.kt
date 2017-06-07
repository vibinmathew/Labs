package com.telstra.datacat.domain

import java.time.LocalDate

data class DataIdentifier(val token: String, val msisdn: String, val date: LocalDate)
