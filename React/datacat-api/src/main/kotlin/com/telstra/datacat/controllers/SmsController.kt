package com.telstra.datacat.controllers

import com.telstra.datacat.adapters.RabbitConfiguration.Companion.MESSAGING_QUEUE_NAME
import com.telstra.datacat.adapters.RabbitConfiguration.Companion.TOPIC
import com.telstra.datacat.domain.SmsMessage
import com.telstra.datacat.domain.gateways.MsisdnFilterGateway
import com.telstra.datacat.domain.gateways.UsageNotificationGateway
import com.telstra.datacat.domain.services.SmsService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_GATEWAY
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SmsController(val smsService: SmsService,
                    val usageNotificationGateway: UsageNotificationGateway,
                    val msisdnFilterGateway: MsisdnFilterGateway, val rabbitTemplate: RabbitTemplate) {

    val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @RequestMapping("/api/sms/{msisdn}")
    fun sendSms(@PathVariable msisdn: String): ResponseEntity<String> {
        if (!msisdn.matches("^614\\d{8}$".toRegex())) {
            return ResponseEntity.status(NOT_FOUND).build()
        }
        val httpStatus = if (smsService.send(msisdn)) OK else BAD_GATEWAY
        return ResponseEntity.status(httpStatus).body(httpStatus.toString())
    }

    @RequestMapping("/api/sms")
    fun checkDataUsage(): ResponseEntity<String> {
        val msisdns = usageNotificationGateway.msisdnToNotify()
        logger.debug("Candidate MSISDNs to notify: $msisdns")
        for (msisdn in msisdns) {
            if (msisdnFilterGateway.check(msisdn)) {
                rabbitTemplate.convertAndSend(TOPIC, MESSAGING_QUEUE_NAME, SmsMessage(msisdn))
            }
            else {
                logger.debug("MSISDN $msisdn is not in the whitelist")
            }
        }
        return ResponseEntity.status(HttpStatus.OK).build()
    }

}