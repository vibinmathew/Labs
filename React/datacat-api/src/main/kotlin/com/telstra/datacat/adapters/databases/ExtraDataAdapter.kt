package com.telstra.datacat.adapters.databases

import com.telstra.datacat.domain.ExtraData
import com.telstra.datacat.domain.gateways.ExtraDataGateway
import org.springframework.stereotype.Component
import java.math.BigInteger

@Component
class ExtraDataAdapter(val repository: ExtraDataRepository) : ExtraDataGateway {
    override fun lastExtraDataList(msisdn: String): List<ExtraData> {
        val billRows = repository.lastExtraDataList(msisdn)
        return billRows.map { billRow -> ExtraData(billRow.date.toLocalDateTime().toLocalDate(), BigInteger(billRow.extraData)) }
                .sortedWith(compareBy { it.date })
    }
}