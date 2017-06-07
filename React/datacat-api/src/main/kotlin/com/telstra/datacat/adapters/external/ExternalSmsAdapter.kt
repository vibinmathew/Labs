package com.telstra.datacat.adapters.external

import com.telstra.datacat.domain.gateways.SmsGateway
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.security.oauth2.client.OAuth2RestTemplate
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClientException

@Profile("!internal")
@Component
class ExternalSmsAdapter(val externalRestTemplate: OAuth2RestTemplate,
                         @Value("\${sms.gateway.url}") val smsUrl : String) : SmsGateway<ExternalSmsRequest> {

    val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    override fun send(request: ExternalSmsRequest): Boolean {
        try {
            externalRestTemplate.postForObject(smsUrl, request, ExternalSmsResponse::class.java)
        } catch(e: RestClientException) {
            logger.error("Failed to send SMS: $e")
            return false
        }
        return true
    }
}