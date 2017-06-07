package com.telstra.datacat.adapters

import com.telstra.datacat.adapters.databases.MsisdnFilterRepository
import com.telstra.datacat.domain.gateways.MsisdnFilterGateway
import org.springframework.stereotype.Component

@Component
class MsisdnFilterAdapter(val repository: MsisdnFilterRepository) : MsisdnFilterGateway {
    override fun check(msisdn: String): Boolean {
        return repository.check(msisdn)
    }

    override fun add(msisdn: String, toSend: Boolean) {
        repository.addMsisdnToFilterlist(msisdn, toSend)
    }
}