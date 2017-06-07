package com.telstra.datacat.adapters.internal

import com.telstra.datacat.domain.gateways.SmsGateway
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate

@Profile("internal")
@Component
class InternalSmsAdapter(val internalRestTemplate: RestTemplate,
                         @Value("\${sms.gateway.url}") val smsUrl: String) : SmsGateway<InternalSmsRequest> {

    val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    override fun send(request: InternalSmsRequest): Boolean {
        logger.debug("Sending SMS: $request")

        try {
            val response = internalRestTemplate.postForEntity(smsUrl, request, String::class.java)
            logger.debug("SMS Request sent $request => $response")
            if (!response.statusCode.is2xxSuccessful) {
                return false
            }
        } catch(e: RestClientException) {
            logger.error("Failed to send SMS: $e")
            return false
        }
        return true
    }

}