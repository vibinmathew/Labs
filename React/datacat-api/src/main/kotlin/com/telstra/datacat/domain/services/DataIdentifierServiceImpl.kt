package com.telstra.datacat.domain.services

import com.telstra.datacat.domain.DataIdentifier
import com.telstra.datacat.domain.gateways.DataIdentifierGateway
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDate

@Component
class DataIdentifierServiceImpl(val dataIdentifierGateway: DataIdentifierGateway,
                                val tokenGenerator: TokenGenerator,
                                val clock: Clock) : DataIdentifierService {

    override fun generateToken(msisdn: String): String {
        val token = tokenGenerator.generate()
        dataIdentifierGateway.saveToken(token, msisdn, LocalDate.now(clock))
        return token
    }

    override fun loadFromToken(token: String): DataIdentifier {
        return dataIdentifierGateway.load(token)
    }

}