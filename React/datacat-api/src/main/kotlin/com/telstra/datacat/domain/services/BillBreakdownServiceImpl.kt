package com.telstra.datacat.domain.services

import com.telstra.datacat.domain.BillBreakdown
import com.telstra.datacat.domain.gateways.BaseBillGateway
import com.telstra.datacat.domain.gateways.BillsGateway
import com.telstra.datacat.domain.gateways.ExtraDataChargeGateway
import com.telstra.datacat.domain.gateways.ExtraDataGateway
import org.springframework.stereotype.Component
import java.util.*

@Component
class BillBreakdownServiceImpl(val billsGateway: BillsGateway, val baseBillGateway: BaseBillGateway, val extraDataChargeGateway: ExtraDataChargeGateway, val extraDataGateway: ExtraDataGateway) : BillBreakdownService {
    override fun lastBillBreakdowns(msisdn: String): List<BillBreakdown> {

        val bills = billsGateway.lastBills(msisdn)
        val baseBills = baseBillGateway.lastBaseBills(msisdn)
        val extraDataCharges = extraDataChargeGateway.lastExtraDataCharges(msisdn)
        val extraDataList = extraDataGateway.lastExtraDataList(msisdn)

        val billsBreakdownList = ArrayList<BillBreakdown>()
        for((index, bill) in bills.withIndex()) {

            val baseBill = baseBills.get(index)
            val billsBreakdown = BillBreakdown(bill.date, bill.bill, baseBill.baseBill)

            for (extraDataCharge in extraDataCharges){
                if(extraDataCharge.date.monthValue == bill.date.monthValue){
                    billsBreakdown.extraDataCharge = extraDataCharge.extraDataCharge
                    break
                }
            }

            for (extraData in extraDataList){
                if(extraData.date.monthValue == bill.date.monthValue){
                    billsBreakdown.extraData = extraData.extraData
                    break
                }
            }
            billsBreakdownList.add(billsBreakdown)
        }
        return billsBreakdownList
    }
}