package com.telstra.datacat.adapters.internal

import com.telstra.datacat.adapters.internal.InternalSmsRequest.Recipient
import com.telstra.datacat.domain.services.CategorizationResultGenerator
import com.telstra.datacat.domain.services.MessageGenerator
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDate


@Profile("internal")
@Component
class InternalSmsMessageGenerator(val categorizationResultGenerator: CategorizationResultGenerator,
                                  val clock: Clock,
                                  @Value("\${sms.gateway.notificationEventType}") val notificationEventType: String,
                                  @Value("\${sms.gateway.entityType}") val entityType: String,
                                  @Value("\${sms.gateway.staticUrl}") val staticUrl: String) : MessageGenerator<InternalSmsRequest> {

    /**
     * This generates entries expected by the SMS API template (currently expecting five data_type_x)
     */
    override fun generate(msisdn: String): InternalSmsRequest {
        val categorization = categorizationResultGenerator.generate(msisdn, LocalDate.now(clock))

        val contentParams: MutableList<InternalSmsRequest.ContentParameter> = listOf(
                InternalSmsRequest.ContentParameter("data_type_1"),
                InternalSmsRequest.ContentParameter("data_type_2"),
                InternalSmsRequest.ContentParameter("data_type_3"),
                InternalSmsRequest.ContentParameter("data_type_4"),
                InternalSmsRequest.ContentParameter("data_type_5")
        ).toMutableList()

        categorization.overall.mapIndexed { index, categoryResult ->
            contentParams[index].value = "${categoryResult.category.getCategoryText()} (${categoryResult.amount}%)"
        }
        contentParams.add(InternalSmsRequest.ContentParameter("url_for_recipient", staticUrl))

        return InternalSmsRequest(notificationEventType, entityType, Recipient(msisdn), contentParams)
    }
}
