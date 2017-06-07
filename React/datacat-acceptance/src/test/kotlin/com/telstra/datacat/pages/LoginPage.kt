package com.telstra.datacat.pages

import org.fluentlenium.core.FluentPage
import org.fluentlenium.core.annotation.PageUrl
import org.fluentlenium.core.domain.FluentWebElement
import org.fluentlenium.core.filter.FilterConstructor.containingText

@PageUrl("/usage/{msisdn}")
class LoginPage : FluentPage() {

    override fun isAt() {
        title().displayed()
    }

    private fun title() = el(".title", containingText("My Telstra data usage"))

    fun titleText() = window().title()


    fun welcomeText() = el(".welcome-text").text()

    fun fillNumber(number: String) {
        el("input").fill().withText(number)
    }

    fun sendButton(): FluentWebElement {
        return el(".button", containingText("Send me an SMS"))
    }

    fun infoText() = el(".info-text").text()

    fun notFoundText() = el(".not-found-text").text()

    fun infoVisible() = el("#root").find(".info-text").present()

    fun inputVisible() = el("#root").find("input").present()

    fun buttonVisible() = el("#root").find(".button").present()

    fun welcomeVisible() = el("#root").find(".welcome-text").present()

    fun tickPresent() = el("#root").find(".tick").present()

    fun waitForTickPresent() = el(".tick").present()

    fun backToLoginLink(): FluentWebElement {
        return el(".back-link")
    }

    fun input(): FluentWebElement {
        return el("input")
    }

}
