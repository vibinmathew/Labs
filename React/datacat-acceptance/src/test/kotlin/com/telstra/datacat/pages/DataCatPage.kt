package com.telstra.datacat.pages

import org.fluentlenium.core.FluentPage
import org.fluentlenium.core.annotation.PageUrl
import org.fluentlenium.core.domain.FluentWebElement
import org.fluentlenium.core.filter.FilterConstructor.containingText

@PageUrl("/usage/{msisdn}")
class DataCatPage : FluentPage() {

    override fun isAt() {
        title().displayed()
        summarySection().displayed()
    }

    private fun title() = el(".title", containingText("My Telstra data usage"))

    private fun summarySection() = el(".usage-container")

    fun userInfo(): FluentWebElement {
        return el(".userinfo div", containingText("This data is an approximation"))
    }

    fun lastUpdated(): FluentWebElement {
        return el(".userinfo div", containingText("Last updated"))
    }

    fun dataUsed(): FluentWebElement {
        return el("div.data-used")
    }

    fun daysLeft(): FluentWebElement {
        return el(".days-left")
    }

    fun previousBillingCycle(): FluentWebElement {
        return el(".previous-cycle")
    }

    fun billingCycle(): FluentWebElement {
        return el(".billing-cycle")
    }

    fun graphLegend(): FluentWebElement {
        return el(".graph-legend")
    }

    fun dailyBarChart(): FluentWebElement {
        return el("div#container")
    }

    fun categories(): List<FluentWebElement> {
        return find("ul.categories li")
    }

    fun category(index: Int): FluentWebElement {
        return categories().get(index)
    }

    fun nextButton(): FluentWebElement {
        return el("#next-billing-cycle")
    }

    fun previousButton(): FluentWebElement {
        return el("#prev-billing-cycle")
    }

    fun datapackButton(): FluentWebElement {
        return el("div.button", containingText("Reduce my future bills"))
    }

    fun backButton() = el(".back .back-link-text", containingText("Home"))
}
