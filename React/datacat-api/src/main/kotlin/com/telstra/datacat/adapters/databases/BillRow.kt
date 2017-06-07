package com.telstra.datacat.adapters.databases

import java.sql.Timestamp

data class BillRow(val msisdn: String,
                   val bill: String,
                   val date: Timestamp,
                   val baseBill: String,
                   val extraDataCharge: String,
                   val extraData: String)