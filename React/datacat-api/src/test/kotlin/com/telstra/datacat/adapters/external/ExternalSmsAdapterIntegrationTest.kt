package com.telstra.datacat.adapters.external

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlMatching
import com.github.tomakehurst.wiremock.common.SingleRootFileSource
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import com.github.tomakehurst.wiremock.junit.WireMockClassRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles(profiles = arrayOf("NO_PROFILE"))
@TestPropertySource(properties=arrayOf(
        "sms.gateway.url=http://localhost:8089/v1/sms/messages",
        "security.oauth2.client.access-token-uri=http://localhost:8089/v1/oauth/token",
        "security.oauth2.client.client-id=111222333",
        "security.oauth2.client.client-secret=77665544332211",
        "frontend.origin=http://example.com"
))
class ExternalSmsAdapterIntegrationTest {

    @Rule @JvmField
    var wireMockRule = WireMockClassRule(options()
            .port(8089)
            .recordRequestHeadersForMatching(Arrays.asList("Authorization"))
            .usingFilesUnderClasspath("wiremock")
    )

    @Before
    fun baseSetup() {
        if (false) {
            enableRecording()
        }
    }

    private fun enableRecording() {
        val mappings = SingleRootFileSource("src/test/resources/wiremock/mappings")
        val files = SingleRootFileSource("src/test/resources/wiremock/__files")
        wireMockRule.enableRecordMappings(mappings, files)

        stubFor(post(urlMatching("/v1/.*"))
                .willReturn(aResponse().proxiedFrom("https://api.telstra.com")
        ))
    }

    @Autowired
    var smsAdapter : ExternalSmsAdapter? = null

    @Test
    fun sendSms() {
        val send = smsAdapter?.send(ExternalSmsRequest("0429876448", "Hello !!"))
        assertThat(send).isTrue()
    }

}