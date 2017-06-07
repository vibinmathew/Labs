package com.telstra.datacat.configurations

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.client.OAuth2RestTemplate
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client


@Configuration
@EnableOAuth2Client
open class SmsClientConfiguration {

    @Bean
    open fun externalRestTemplate(resourceDetails: ClientCredentialsResourceDetails): OAuth2RestTemplate {
        return OAuth2RestTemplate(resourceDetails)
    }
}