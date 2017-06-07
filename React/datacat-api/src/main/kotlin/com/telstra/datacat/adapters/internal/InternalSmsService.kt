package com.telstra.datacat.adapters.internal

import com.telstra.datacat.domain.gateways.SmsGateway
import com.telstra.datacat.domain.services.MessageGenerator
import com.telstra.datacat.domain.services.SmsService
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Profile("internal")
@Component
class InternalSmsService(val smsGateway: SmsGateway<InternalSmsRequest>,
                         val messageGenerator: MessageGenerator<InternalSmsRequest>) : SmsService {

    override fun send(msisdn: String): Boolean {
        return smsGateway.send(messageGenerator.generate(msisdn))
    }
}