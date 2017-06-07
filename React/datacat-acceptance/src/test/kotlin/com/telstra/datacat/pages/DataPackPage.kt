package com.telstra.datacat.pages

import org.fluentlenium.core.FluentPage
import org.fluentlenium.core.annotation.PageUrl
import org.fluentlenium.core.filter.FilterConstructor.containingText

@PageUrl("/datapack/{msisdn}")
class DataPackPage : FluentPage() {

    override fun isAt() {
        title().displayed()
        backButton().displayed()
        moreInfoButton().displayed()
    }

    private fun title() = el(".title", containingText("Reduce my bill"))
    private fun moreInfoButton() = el(".button", containingText("Learn more about data packs"))

    fun backButton() = el(".back .back-link-text", containingText("Home"))

    fun lastBills() = el("div.last-bills")

    fun recommendations() = el("div.recommendations")

    fun billCalculation() = el(".bill-calculation-txt")

    fun  billCalcuationDetails() = el(".bill-calculation-details")

}
