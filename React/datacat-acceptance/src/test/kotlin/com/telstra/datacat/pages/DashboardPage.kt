package com.telstra.datacat.pages

import org.fluentlenium.core.FluentPage
import org.fluentlenium.core.annotation.PageUrl
import org.fluentlenium.core.domain.FluentWebElement
import org.fluentlenium.core.filter.FilterConstructor.containingText

@PageUrl("/dashboard/{msisdn}")
class DashboardPage : FluentPage() {

    override fun isAt() {
        dataUsageButton().displayed()
        dataPackButton().displayed()
    }

    fun dataUsageButton(): FluentWebElement {
        return el("div.button", containingText("View my data usage"))
    }

    fun dataPackButton(): FluentWebElement {
        return el("div.button", containingText("Reduce my future bills"))
    }

}
