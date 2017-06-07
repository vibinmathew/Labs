package com.telstra.datacat.adapters.databases

import com.telstra.datacat.domain.ExtraDataCharge
import com.telstra.datacat.domain.gateways.ExtraDataChargeGateway
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class ExtraDataChargeAdapter(val repository: ExtraDataChargeRepository) : ExtraDataChargeGateway {
    override fun lastExtraDataCharges(msisdn: String): List<ExtraDataCharge> {
        val billRows = repository.lastExtraDataCharges(msisdn)
        return billRows.map { billRow -> ExtraDataCharge(billRow.date.toLocalDateTime().toLocalDate(), BigDecimal(billRow.extraDataCharge)) }
              .sortedWith(compareBy { it.date })
    }
}