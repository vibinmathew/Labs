package com.telstra.datacat.configurations

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock

@Configuration
open class SystemClockConfiguration {

    @Bean
    open fun clock(): Clock {
        return Clock.systemDefaultZone()
    }
}