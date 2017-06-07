package com.telstra.datacat.controllers

import com.telstra.datacat.domain.User
import com.telstra.datacat.domain.services.DataIdentifierService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(val dataIdentifierService: DataIdentifierService) {

    @RequestMapping("/api/user/{token}")
    fun user(@PathVariable token: String): User {
        return User(dataIdentifierService.loadFromToken(token).msisdn)
    }
}