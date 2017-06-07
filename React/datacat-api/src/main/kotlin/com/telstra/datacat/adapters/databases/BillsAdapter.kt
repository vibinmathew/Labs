package com.telstra.datacat.adapters.databases

import com.telstra.datacat.domain.Bill
import com.telstra.datacat.domain.gateways.BillsGateway
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class BillsAdapter(val billsRepository: BillsRepository) : BillsGateway {
    override fun lastBills(msisdn: String): List<Bill> {
        val billRows = billsRepository.lastBills(msisdn)
        return billRows.map { billRow -> Bill(billRow.date.toLocalDateTime().toLocalDate(), BigDecimal(billRow.bill)) }
                .sortedWith(compareBy { it.date })
    }
}