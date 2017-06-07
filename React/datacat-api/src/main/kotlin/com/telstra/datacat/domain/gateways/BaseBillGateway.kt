package com.telstra.datacat.domain.gateways

import com.telstra.datacat.domain.BaseBill

interface BaseBillGateway {
    fun lastBaseBills(msisdn: String): List<BaseBill>
}