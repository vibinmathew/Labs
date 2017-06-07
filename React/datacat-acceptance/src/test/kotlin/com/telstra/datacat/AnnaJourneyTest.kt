package com.telstra.datacat

import com.telstra.datacat.pages.DashboardPage
import com.telstra.datacat.pages.DataCatPage
import com.telstra.datacat.pages.DataPackPage
import com.telstra.datacat.pages.LoginPage
import io.restassured.RestAssured
import io.restassured.RestAssured.`when`
import io.restassured.builder.RequestSpecBuilder
import org.assertj.core.api.Assertions.assertThat
import org.fluentlenium.adapter.junit.FluentTest
import org.fluentlenium.configuration.FluentConfiguration
import org.fluentlenium.core.annotation.Page
import org.fluentlenium.core.hook.wait.Wait
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import java.util.concurrent.TimeUnit

@Wait(timeout = 10, timeUnit = TimeUnit.SECONDS)
@FluentConfiguration(webDriver = "chrome", capabilities = "{\"chromeOptions\": { \"args\": [\"--no-sandbox\", \"--disable-gpu\"] } }")
class AnnaJourneyTest : FluentTest() {

    @Page
    private lateinit var dataCatPage: DataCatPage

    @Page
    private lateinit var loginPage: LoginPage

    @Page
    private lateinit var datapack: DataPackPage

    @Page
    private lateinit var dashboard: DashboardPage

    @Before
    fun setUp() {
        baseUrl = System.getenv("BASE_URL") ?: "http://localhost:3000"
    }

    @Test
    fun journey() {
        val token = mockData("current")

        goTo("/usage/$token")
        dataCatPage.isAt()
        val userInfo = dataCatPage.userInfo()
        userInfo.displayed()
        assertThat(userInfo.text()).contains("61429876448")

        val lastUpdated = dataCatPage.lastUpdated()
        lastUpdated.displayed()
        assertThat(lastUpdated.text()).containsPattern("Last\\ updated\\ at\\ \\d{1,2}:\\d{2}\\ PM|AM on\\ \\d{1,2}\\/\\d{1,2}\\/\\d{4}$")

        val dataUsed = dataCatPage.dataUsed()
        dataUsed.displayed()
        assertThat(dataUsed.text()).matches("^\\d+\\.*\\d*\\d+\\.*\\d*GB$")

        val daysLeft = dataCatPage.daysLeft()
        daysLeft.displayed()
        assertThat(daysLeft.text()).matches("^\\d+ day[s]? left$")

        val billingCycle = dataCatPage.billingCycle()
        billingCycle.displayed()
        val currentBillingCycle = billingCycle.text()
        assertThat(currentBillingCycle).matches("^\\d+ (Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec) - \\d+ (Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec), \\d{4}$")

        assertThat(dataCatPage.categories().size).isEqualTo(5)
        assertThat(dataCatPage.category(4).text()).contains("Others")
        for (elem in dataCatPage.categories()) {
            assertThat(elem.find("svg").size).isEqualTo(1)
            assertThat(elem.find(".category-container").text()).isNotEmpty()
            assertThat(elem.find(".percentage-container").text()).matches("^\\d+%$")
        }

        val dailyBarChart = dataCatPage.dailyBarChart()
        assertThat(dailyBarChart.find("svg").size).isEqualTo(1)

        assertThat(dataCatPage.category(0).attribute("class")).contains("selected")

        dataCatPage.category(1).click()
        assertThat(dataCatPage.category(0).attribute("class")).doesNotContain("selected")
        assertThat(dataCatPage.category(1).attribute("class")).contains("selected")

        assertThat(dataCatPage.graphLegend().text()).contains("Total Usage")
        assertThat(dataCatPage.graphLegend().text()).contains(dataCatPage.category(1).find(".category-container").text())

        dataCatPage.nextButton().displayed()
        dataCatPage.previousButton().displayed()
        val oldUrl = url()
        dataCatPage.nextButton().click()
        assertThat(url()).isEqualTo(oldUrl)
        dataCatPage.previousButton().click()
        assertThat(url()).isNotEqualTo(oldUrl)

        val previousBillingCycle = dataCatPage.billingCycle().text()
        assertThat(previousBillingCycle).isNotEqualTo(currentBillingCycle)

        dataCatPage.datapackButton().displayed()
        dataCatPage.datapackButton().click()
        datapack.isAt()

        datapack.lastBills().displayed()
        datapack.lastBills().text().contains("Here is what you've spent over the last 3 months")
        datapack.recommendations().displayed()
        datapack.recommendations().text().contains("That's an average of $")

        datapack.billCalculation().displayed()
        datapack.billCalculation().click()
        datapack.billCalcuationDetails().displayed()

        datapack.backButton().click()

        dashboard.isAt()
    }

    @Test
    fun journeyInThePast() {
        val token = mockData("previous")

        goTo("/usage/$token")
        dataCatPage.isAt()
        val userInfo = dataCatPage.userInfo()
        userInfo.displayed()
        assertThat(userInfo.text()).contains("61429876448")

        val lastUpdated = dataCatPage.lastUpdated()
        lastUpdated.displayed()
        assertThat(lastUpdated.text()).containsPattern("Last\\ updated\\ at\\ \\d{1,2}:\\d{2}\\ PM|AM on\\ \\d{1,2}\\/\\d{1,2}\\/\\d{4}$")

        val previousBillingCycle = dataCatPage.previousBillingCycle()
        previousBillingCycle.displayed()
        assertThat(previousBillingCycle.text()).isEqualTo("Previous Billing Cycle")

        val dataUsed = dataCatPage.dataUsed()
        dataUsed.displayed()
        assertThat(dataUsed.text()).matches("^\\d+\\.*\\d*\\d+\\.*\\d*GB$")
    }

    @Test
    fun loginJourney() {
        mockData("current")
        goTo("/")
        loginPage.isAt()
        assertThat(loginPage.titleText()).isEqualTo("My Telstra data usage")
        assertThat(loginPage.welcomeText()).isEqualTo("You've been selected to preview a new tool from Telstra")
        loginPage.fillNumber("6149")
        assertThat(loginPage.tickPresent()).isFalse()
        loginPage.fillNumber("0429876448")
        assertThat(loginPage.waitForTickPresent()).isTrue()
        assertThat(loginPage.infoVisible()).isFalse()
        loginPage.sendButton().click()
        assertThat(loginPage.infoText()).isEqualTo("Your SMS is on the way")
        assertThat(loginPage.welcomeVisible()).isFalse()
        assertThat(loginPage.inputVisible()).isFalse()
        assertThat(loginPage.buttonVisible()).isFalse()
        loginPage.backToLoginLink().click()
        loginPage.isAt()
        assertThat(loginPage.titleText()).isEqualTo("My Telstra data usage")
        assertThat(loginPage.welcomeText()).isEqualTo("You've been selected to preview a new tool from Telstra")
    }

    @Test
    fun dashboardJourney() {
        val token = mockData("current")
        goTo("/dashboard/$token")
        dashboard.isAt()
        dashboard.dataUsageButton().click()
        dataCatPage.isAt()
        dataCatPage.backButton().click()
        dashboard.isAt()
        dashboard.dataPackButton().click()
        datapack.isAt()
        datapack.backButton().click()
        dashboard.isAt()
    }

    @Test
    @Ignore("Disabled until real billing data is available")
    fun loginInvalidNumber() {
        goTo("/")
        loginPage.isAt()
        loginPage.fillNumber("0400000000")
        loginPage.sendButton().click()
        assertThat(loginPage.notFoundText()).isEqualTo("We couldn’t find 0400000000 on our system")
        loginPage.backToLoginLink().click()
        loginPage.isAt()
        assertThat(loginPage.titleText()).isEqualTo("My Telstra data usage")
        assertThat(loginPage.welcomeText()).isEqualTo("You've been selected to preview a new tool from Telstra")
        assertThat(loginPage.input().element.getAttribute("value")).isEqualTo("0400000000")
    }

    fun mockData(cycle: String): String {
        val apiUrl = System.getenv("API_BASE_URL") ?: "http://localhost:8080"

        RestAssured.requestSpecification = RequestSpecBuilder().setBaseUri(apiUrl).build()

        `when`()
                .post("/mock/61429876448/clear")
                .then()
                .statusCode(200)

        val token = `when`()
                .post("/mock/61429876448/103")
                .then()
                .statusCode(200).extract().path<String>("$cycle.token")

        return token
    }
}
