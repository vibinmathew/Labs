package com.telstra.datacat.controllers.mock


import com.telstra.datacat.adapters.databases.DataIdentifierRow
import com.telstra.datacat.adapters.databases.repositories.MsisdnFilterRowMapper
import com.telstra.datacat.adapters.databases.repositories.RedisRepository
import com.telstra.datacat.domain.services.TokenGenerator
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.Math.pow
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import java.security.SecureRandom
import java.sql.Date
import java.time.Clock
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@RestController
class PopulateRtiDb(val fakeJdbcTemplate: NamedParameterJdbcTemplate,
                    val clock: Clock,
                    val redisRepository: RedisRepository,
                    val tokenGenerator: TokenGenerator,
                    val mockUsageNotification: MockUsageNotification) {

    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMM")
    val random = SecureRandom()
    val baseBill = BigDecimal("95.95")

    @RequestMapping("/mock/{msisdn}/{usage}")
    fun generateData(@PathVariable msisdn: String, @PathVariable usage: BigDecimal): Map<Any, Any> {

        // Filling SMS triggering threshold table
        val paramsNotify: MutableMap<String, Any> = HashMap()
        paramsNotify.put("msisdn", msisdn)
        paramsNotify.put("usage", usage)
        val startTime = LocalDateTime.now(clock).minusHours(random.nextInt(23).toLong())
        paramsNotify.put("start_time", java.sql.Timestamp.valueOf(startTime))

        mockUsageNotification.create()
        mockUsageNotification.fill(paramsNotify)

        val readableFactors: MutableMap<Any, Any> = HashMap()
        val toDay = LocalDate.now(clock)
        val fromDay = toDay.minusMonths(1)
        val billingStart = toDay.minusDays(random.nextInt(28).toLong())
        val billingEnd = billingStart.plusMonths(1)

        readableFactors.put("current", generateCycleData(msisdn, usage, fromDay, toDay, billingStart, billingEnd))
        readableFactors.put("previous", generateCycleData(msisdn, usage, fromDay.minusMonths(1), toDay.minusMonths(1), billingStart.minusMonths(1), billingEnd.minusMonths(1)))
        readableFactors.put("oldest", generateCycleData(msisdn, usage, fromDay.minusMonths(2), toDay.minusMonths(2), billingStart.minusMonths(2), billingEnd.minusMonths(2)))

        // Whitelist the MSISDN
        createMsisdnFilterTable()

        // create / fill bills table
        createBillsTable()
        val bill1date = toDay.minusMonths(1)
        val bill2date = toDay.minusMonths(2)
        val bill3date = toDay.minusMonths(3)
        val extraCharge = arrayOf(0, 0, 0)
        val extraData = arrayOf(BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO)
        if (usage > BigDecimal("100")) {
            extraCharge[0] = 30
            extraCharge[1] = 0
            extraCharge[2] = 40
            extraData[0] = BigInteger.valueOf(3 * 1024 * 1024)
            extraData[1] = BigInteger.valueOf(0 * 1024 * 1024)
            extraData[2] = BigInteger.valueOf(4 * 1024 * 1024)
        }
        addBill(msisdn, bill1date, 155, baseBill, extraCharge[0], extraData[0])
        addBill(msisdn, bill2date, 144, baseBill, extraCharge[1], extraData[1])
        addBill(msisdn, bill3date, 162, baseBill, extraCharge[2], extraData[2])
        readableFactors.put("bills", arrayOf(
                mapOf("date" to bill1date.toString(), "bill" to 150, "base_bill" to baseBill, "extra_data_charge" to extraCharge[0], "extra_data" to extraData[0]),
                mapOf("date" to bill2date.toString(), "bill" to 140, "base_bill" to baseBill, "extra_data_charge" to extraCharge[1], "extra_data" to extraData[1]),
                mapOf("date" to bill3date.toString(), "bill" to 160, "base_bill" to baseBill, "extra_data_charge" to extraCharge[2], "extra_data" to extraData[2])
        ))

        return readableFactors
    }

    @RequestMapping("/mock/{msisdn}/clear")
    fun clearData(@PathVariable msisdn: String): MutableMap<Any, Any> {
        val toDay = LocalDate.now(clock)
        val readableFactors: MutableMap<Any, Any> = HashMap()

        createBillingTable()
        mockUsageNotification.create()
        createBillsTable()

        val parameters = Collections.singletonMap<String, Any>("msisdn", msisdn)
        readableFactors.put("current", clearMonthlyData(toDay.format(formatter), parameters))
        readableFactors.put("previous", clearMonthlyData(toDay.minusMonths(1).format(formatter), parameters))
        readableFactors.put("older", clearMonthlyData(toDay.minusMonths(2).format(formatter), parameters))
        readableFactors.put("oldest", clearMonthlyData(toDay.minusMonths(3).format(formatter), parameters))
        readableFactors.put("ondb", mockUsageNotification.clear(parameters))
        readableFactors.put("mdum", fakeJdbcTemplate.update("DELETE FROM mdum WHERE msisdn=:msisdn", parameters))
        readableFactors.put("bills", fakeJdbcTemplate.update("DELETE FROM bills WHERE msisdn=:msisdn", parameters))
        return readableFactors
    }

    @RequestMapping("/filter/{msisdn}/{toSend}")
    fun addMsisdnToFilterList(@PathVariable msisdn: String, @PathVariable toSend: Boolean) : MutableMap<Any, Any> {
        createMsisdnFilterTable()
        val parameters: Map<String, Any> = mapOf("msisdn" to msisdn, "to_send" to toSend)
        val readableFactors: MutableMap<Any, Any> = HashMap()
        //readableFactors.put("msisdn_filter", fakeJdbcTemplate.update("INSERT INTO msisdn_filter(msisdn, to_send) VALUES (:msisdn, :to_send)", parameters))
        /*fakeJdbcTemplate.update("INSERT INTO msisdn_filter as m(msisdn, to_send)"+
                "VALUES (:msisdn, :to_send)"+
                "ON CONFLICT (msisdn) DO UPDATE SET to_send = EXCLUDED.to_send where m.msisdn=EXCLUDED.msisdn", parameters)*/
        try {
            fakeJdbcTemplate.update("INSERT INTO msisdn_filter VALUES (:msisdn, :to_send)", parameters)
        } catch (ex: Exception) {
            fakeJdbcTemplate.update("UPDATE msisdn_filter SET to_send = :to_send WHERE msisdn = :msisdn", parameters)
        }

        val msisdnFilterRowMapper = MsisdnFilterRowMapper()

        readableFactors.put("msisdn_filter", fakeJdbcTemplate.query("select * from msisdn_filter", msisdnFilterRowMapper))
        return readableFactors;
    }

    @RequestMapping("/mock/drop")
    fun dropAll() {
        dropTable("mdum")
        mockUsageNotification.drop()
        dropTable("bills")
        val toDay = LocalDate.now(clock)
        dropTable("month_by_day_${toDay.format(formatter)}")
        dropTable("month_by_day_${toDay.minusMonths(1).format(formatter)}")
        dropTable("month_by_day_${toDay.minusMonths(2).format(formatter)}")
        dropTable("month_by_day_${toDay.minusMonths(3).format(formatter)}")
    }

    /**
     * Fill tables related to monthly usage data + associated billing table
     */
    private fun generateCycleData(msisdn: String, usage: BigDecimal, fromDay: LocalDate, toDay: LocalDate, billing_start: LocalDate, billing_end: LocalDate): MutableMap<Any, Any> {
        val fromTableSuffix = fromDay.format(formatter)
        val toTableSuffix = toDay.format(formatter)

        // Create tables if missing
        createMonthlyData(toTableSuffix)
        createMonthlyData(fromTableSuffix)

        // Fill mock data
        val catFactor = Array(14, { pow(random.nextInt(14).toDouble(), 2.2) })
        var totalUsage = BigDecimal.ZERO
        var day = toDay.minusDays(1)
        while (!day.isBefore(fromDay)) {
            val tableSuffix = day.format(formatter)

            for (catId in 0..13) {
                val up = random.nextInt(50) * catFactor[catId]
                val down = random.nextInt(200) * catFactor[catId]

                val parameters: MutableMap<String, Any> = HashMap()
                parameters.put("msisdn", msisdn)
                parameters.put("date", Date.valueOf(day))
                parameters.put("cat_id", catId * 100 + 99)
                parameters.put("bytes_up", up)
                parameters.put("bytes_down", down)

                fakeJdbcTemplate.update("INSERT INTO month_by_day_$tableSuffix (imsi, msisdn, day, category_id, bytes_up, bytes_down)" +
                        " VALUES (:msisdn, :msisdn, :date, :cat_id, :bytes_up, :bytes_down)", parameters)

                totalUsage = totalUsage.add(BigDecimal(up.toString())).add(BigDecimal(down.toString()))
            }
            day = day.minusDays(1)
        }

        // Create billing table
        createBillingTable()

        // Fill billing table
        usage.setScale(2, RoundingMode.HALF_EVEN)
        totalUsage.setScale(2, RoundingMode.HALF_EVEN)
        val ratio = usage.divide(BigDecimal(100))
        val allowance = totalUsage.divide(ratio, RoundingMode.HALF_EVEN)

        val paramsAllowance: MutableMap<String, Any> = HashMap()
        paramsAllowance.put("msisdn", msisdn)
        paramsAllowance.put("allowance", allowance)
        paramsAllowance.put("baseBill", baseBill)
        paramsAllowance.put("usage", totalUsage)
        paramsAllowance.put("billing_start", Date.valueOf(billing_start))
        paramsAllowance.put("billing_end", Date.valueOf(billing_end))
        paramsAllowance.put("updated_at", Date.valueOf(LocalDate.now().minusDays(2)))
        fakeJdbcTemplate.update("INSERT INTO mdum(msisdn, allowance, base_bill, usage, billing_start, billing_end, updated_at) VALUES (:msisdn, :allowance, :baseBill, :usage, :billing_start, :billing_end, :updated_at)", paramsAllowance)

        // generate token related to the generated data
        val token = tokenGenerator.generate()
        redisRepository.saveDataIdentifier(DataIdentifierRow(token, msisdn, billing_end.minusDays(10)))

        // Build summary of generated data
        val total = (0..catFactor.size - 1).sumByDouble { catFactor[it] }
        val readableFactors: MutableMap<Any, Any> = HashMap()

        val data: MutableMap<Any, Any> = HashMap()
        data.put("usage", totalUsage.toBigInteger())
        data.put("allowance", allowance.toBigInteger())
        data.put("baseBill", baseBill)
        data.put("ratio", ratio)
        readableFactors.put("data", data)

        val cycle: MutableMap<Any, Any> = HashMap()
        cycle.put("start", billing_start.toString())
        cycle.put("end", billing_end.toString())
        readableFactors.put("cycle", cycle)

        val catFactors: MutableMap<Any, Any> = HashMap()
        for (i in 0..catFactor.size - 1) {
            catFactors.put(i, ((catFactor[i] * 100) / total).toInt())
        }
        readableFactors.put("catRatio", catFactors)
        readableFactors.put("token", token)

        return readableFactors
    }

    private fun addBill(msisdn: String, toDay: LocalDate, bill: Int, baseBill: BigDecimal, extraDataCharge: Int, extraData: BigInteger) {
        val paramsBills: MutableMap<String, Any> = HashMap()
        paramsBills.put("msisdn", msisdn)
        paramsBills.put("bill", bill)
        paramsBills.put("date", Date.valueOf(toDay))
        paramsBills.put("base_bill", baseBill)
        paramsBills.put("extra_data_charge", extraDataCharge)
        paramsBills.put("extra_data", extraData.longValueExact())
        fakeJdbcTemplate.update("INSERT INTO bills(msisdn, bill, date, base_bill, extra_data_charge, extra_data) " +
                "VALUES (:msisdn, :bill, :date, :base_bill, :extra_data_charge, :extra_data)", paramsBills)
    }

    private fun clearMonthlyData(toTableSuffix: String, parameters: MutableMap<String, Any>): Int {
        createMonthlyData(toTableSuffix)
        return fakeJdbcTemplate.update("DELETE FROM month_by_day_$toTableSuffix WHERE msisdn=:msisdn", parameters)
    }

    private fun createMonthlyData(toTableSuffix: String?) {
        fakeJdbcTemplate.execute("CREATE TABLE IF NOT EXISTS month_by_day_$toTableSuffix (" +
                "imsi character varying, msisdn character varying, day date, category_id integer, bytes_up bigint, bytes_down bigint)",
                { ps ->
                    ps.execute()
                })
    }

    private fun createBillingTable() {
        fakeJdbcTemplate.execute("CREATE TABLE IF NOT EXISTS mdum(msisdn character varying, allowance bigint, base_bill numeric(6,2), usage bigint, billing_start timestamp, billing_end timestamp, updated_at TIMESTAMP DEFAULT now())",
                { ps ->
                    ps.execute()
                })
    }

    private fun createMsisdnFilterTable() {
        fakeJdbcTemplate.execute("CREATE TABLE IF NOT EXISTS msisdn_filter(msisdn character varying PRIMARY KEY, to_send boolean)",
                { ps ->
                    ps.execute()
                })
    }

    private fun createBillsTable() {
        fakeJdbcTemplate.execute("CREATE TABLE IF NOT EXISTS bills(msisdn character varying, bill numeric(6,2), date timestamp, base_bill numeric(6,2), extra_data_charge numeric(6,2), extra_data bigint)",
                { ps ->
                    ps.execute()
                })
    }

    private fun dropTable(toTableSuffix: String?) {
        fakeJdbcTemplate.execute("DROP TABLE IF EXISTS $toTableSuffix",
                { ps ->
                    ps.execute()
                })
    }
}