package com.telstra.datacat.adapters.databases

import java.sql.Timestamp

data class BillingRow(val usage: String, val allowance: String, val baseBill: String, val start: Timestamp, val end: Timestamp, val updatedAt: Timestamp)