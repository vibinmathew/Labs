package com.telstra.datacat.adapters.external

import com.telstra.datacat.domain.gateways.SmsGateway
import com.telstra.datacat.domain.services.MessageGenerator
import com.telstra.datacat.domain.services.SmsService
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Profile("!internal")
@Component
class ExternalSmsService(val smsGateway: SmsGateway<ExternalSmsRequest>,
                         val messageGenerator: MessageGenerator<ExternalSmsRequest>) : SmsService {

    override fun send(msisdn: String): Boolean {
        return smsGateway.send(messageGenerator.generate(msisdn))
    }
}