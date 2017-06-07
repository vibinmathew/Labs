package com.telstra.datacat.domain.gateways

import com.telstra.datacat.domain.ExtraDataCharge

interface ExtraDataChargeGateway {
    fun lastExtraDataCharges(msisdn: String): List<ExtraDataCharge>
}