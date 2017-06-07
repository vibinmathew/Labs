package com.telstra.datacat.adapters.databases

import com.telstra.datacat.domain.BaseBill
import com.telstra.datacat.domain.gateways.BaseBillGateway
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class BaseBillAdapter(val repository: BaseBillRepository) : BaseBillGateway {
    override fun lastBaseBills(msisdn: String): List<BaseBill> {
        val baseBillRows = repository.lastBaseBills(msisdn)
        return baseBillRows.map { billRow -> BaseBill(billRow.date.toLocalDateTime().toLocalDate(), BigDecimal(billRow.baseBill)) }
                .sortedWith(compareBy { it.date })
    }

}