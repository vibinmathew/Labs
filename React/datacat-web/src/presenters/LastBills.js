import React, {Component} from "react";
import {getShortMonthName} from "./utils";
import "./LastBills.css"

class LastBills extends Component {

    render() {
        const {lastBills} = this.props;

        return (
            <div className="last-bills">
                <div className="last-bills-header">
                    Here is what you've spent over the last 3 months:
                </div>
                <div className="last-bills-list">
                    {this.renderBills(lastBills)}
                </div>
            </div>
        )
    }

    renderBills(lastBills) {
        if (lastBills) {
            return lastBills.map((bill) =>
                <div className="bill-element" key={bill.date}>
                    <div className="bill-month-name">{getShortMonthName(new Date(bill.date))}</div>
                    <div className="bill-value"><sup>$</sup>{bill.bill}</div>
                    <div className="bill-info">
                        <div>${bill.baseBill} Base Plan</div>
                        <div>{this.renderExtraDataCharge(bill.extraDataCharge)}</div>
                    </div>
                </div>
            );
        }
    }

    renderExtraDataCharge(extraDataCharge) {
        return (extraDataCharge && extraDataCharge > 0) ? ' + $' + extraDataCharge + ' Extra Data' : ''
    }
}

export default LastBills;
