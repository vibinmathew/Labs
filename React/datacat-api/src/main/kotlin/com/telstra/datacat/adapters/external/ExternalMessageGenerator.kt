package com.telstra.datacat.adapters.external

import com.telstra.datacat.domain.services.CategorizationResultGenerator
import com.telstra.datacat.domain.services.DataIdentifierService
import com.telstra.datacat.domain.services.MessageGenerator
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDate

@Profile("!internal")
@Component
class ExternalMessageGenerator(@Value("\${frontend.origin}") val frontendOrigin: String,
                               val dataIdentifierService: DataIdentifierService,
                               val categorizationResultGenerator: CategorizationResultGenerator,
                               val clock: Clock) : MessageGenerator<ExternalSmsRequest> {

    override fun generate(msisdn: String): ExternalSmsRequest {
        val list = categorizationResultGenerator.generate(msisdn, LocalDate.now(clock)).overall
        val token = dataIdentifierService.generateToken(msisdn)

        return ExternalSmsRequest(msisdn, "Telstra Data usage breakdown:\n" +
                "${list[0].category.text} (${list[0].amount}%)\n" +
                "More information: $frontendOrigin/usage/$token")
    }
}