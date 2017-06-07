package com.telstra.datacat.controllers.mock

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

@Profile("!internal")
@Component
class ExternalMockUsageNotification(val fakeJdbcTemplate: NamedParameterJdbcTemplate,
                                    @Value("\${data-usage.notifications.table-name}") val usageNotifTable: String) : MockUsageNotification {
    override fun create() {
        fakeJdbcTemplate.execute("CREATE TABLE IF NOT EXISTS $usageNotifTable( msisdn VARCHAR(25), mica_acct_no VARCHAR(25), notification_type VARCHAR(50), delivery_pref VARCHAR(100), percent REAL, mdum_sent_date TIMESTAMP(0), gploaded_time TIMESTAMP(0) DEFAULT NOW());",
                { ps ->
                    ps.execute()
                })
    }

    override fun fill(paramsNotify: MutableMap<String, Any>) {
        fakeJdbcTemplate.update("INSERT INTO $usageNotifTable (msisdn, percent, mdum_sent_date) VALUES (:msisdn, :usage, :start_time)", paramsNotify)
    }

    override fun clear(parameters: MutableMap<String, Any>): Int {
        return fakeJdbcTemplate.update("DELETE FROM $usageNotifTable WHERE msisdn=:msisdn", parameters)
    }

    override fun drop() {
        fakeJdbcTemplate.execute("DROP TABLE IF EXISTS $usageNotifTable",
                { ps ->
                    ps.execute()
                })
    }
}