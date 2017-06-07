package com.telstra.datacat.domain.gateways

import com.telstra.datacat.domain.ExtraData

interface ExtraDataGateway {
    fun lastExtraDataList(msisdn: String): List<ExtraData>
}