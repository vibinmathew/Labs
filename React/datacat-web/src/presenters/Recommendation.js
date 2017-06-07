import React, {Component} from "react";
import {ResponsiveContainer, BarChart, Bar, XAxis, Line} from "recharts";
import {convertToGb, keepOneDecimal} from "./utils";
import "./Recommendation.css";

class Recommendation extends Component {

    constructor(props) {
        super(props);
        this.state = {showBillCalculationDetails: false};
        this.renderBillCalculationDetails = this.renderBillCalculationDetails.bind(this);
    }

    render() {
        const {bills, billing} = this.props;

        let total = bills.lastBills.reduce((prev, next) => ({
            bill: prev.bill + next.bill,
            extraData: prev.extraData + next.extraData,
            extraDataCharge: prev.extraDataCharge + next.extraDataCharge
        }));

        let averageBill = keepOneDecimal(total.bill / bills.lastBills.length);
        let averageExtraData = convertToGb(total.extraData / bills.lastBills.length);
        let recommendedBill = billing.baseBill + bills.recommendedDataPack.price;
        let averageExtraDataCharge = keepOneDecimal(total.extraDataCharge / bills.lastBills.length);

        let data = [
            {
                base: billing.baseBill,
                extra: averageBill - billing.baseBill,
                total: '$' + averageBill
            },
            {
                base: billing.baseBill,
                pack: bills.recommendedDataPack.price,
                total: '$' + recommendedBill
            }
        ];

        return (
            <div className="recommendations-wrapper">
                <div className="recommendations">
                    That's an <span className="average-text">average of ${averageBill}/mth</span> due
                    to {averageExtraData}GB of Extra Data.
                    <span className="saving-text"> Only pay ${recommendedBill}/mth</span> with
                    a {bills.recommendedDataPack.amount}GB Data Pack.
                </div>
                <div>
                    <div className="bill-calculation-txt" onClick={() => this.toggleBillCalculation()}>How was this
                        calculated?&nbsp;&nbsp;{this.renderToggleImage()}
                    </div>
                    {this.renderBillCalculationDetails(averageExtraData, averageExtraDataCharge)}
                </div>
                <div className="outer-container">
                    <div id="container">
                        <ResponsiveContainer>
                            <BarChart data={data} barSize={50}>
                                <XAxis padding={{left: 80, right: 75}} tickLine={false} dataKey="total" height={110}/>
                                <Bar dataKey="extra" stackId="a" fill="#e83319" isAnimationActive={false}/>
                                <Bar dataKey="pack" stackId="a" fill="#2d86ca" isAnimationActive={false}/>
                                <Bar dataKey="base" stackId="a" fill="#e4e4e4" isAnimationActive={false}/>
                                <Line className="x-axis" type="monotone"/>
                            </BarChart>
                        </ResponsiveContainer>
                    </div>
                </div>
                <div className="graph-legend">
                    <span className="extra-data">&nbsp;</span>
                    <span>{averageExtraData}GB Extra Data</span>
                    <span className="extra-data-pack">&nbsp;</span>
                    <span>{bills.recommendedDataPack.amount}GB Extra Data</span>
                    <span className="base-plan">&nbsp;</span>
                    <span>Base Plan</span>
                </div>
            </div>
        )
    }

    toggleBillCalculation() {
        this.setState({showBillCalculationDetails: !this.state.showBillCalculationDetails});
    }

    renderBillCalculationDetails(averageExtraData, averageExtraDataCharge) {
        if (this.state.showBillCalculationDetails) {
            return (
                <div className="bill-calculation-details">
                    <div className="bill-calculation-details-text">
                        When you exceeded your monthly data allowance, 1GB blocks of Extra Data were automatically
                        applied to your plan for $10 each:
                    </div>
                    <div className="bill-calculation-details-bold-text">
                        1GB = $10
                    </div>
                    <div className="bill-calculation-details-text">
                        Since you exceeded by an average of 5GB each month, you spent $50 in Extra Data charges each
                        month:
                    </div>
                    <div className="bill-calculation-details-bold-text">
                        {averageExtraData}GB = ${averageExtraDataCharge}
                    </div>
                </div>
            )
        }
    }

    renderToggleImage() {
        if (this.state.showBillCalculationDetails) {
            return (
                <svg xmlns="http://www.w3.org/2000/svg" width="8" height="5" viewBox="0 0 8 5">
                    <path fill="#333" fillRule="evenodd" d="M8 4.207L4.145 0 .29 4.207 1.017 5l3.128-3.414L7.273 5z"/>
                </svg>
            )
        } else {
            return (
                <svg xmlns="http://www.w3.org/2000/svg" width="8" height="5" viewBox="0 0 8 5">
                    <path fill="#333" fillRule="evenodd" d="M.29.793L4.145 5 8 .793 7.273 0 4.145 3.414 1.017 0z"/>
                </svg>
            )
        }
    }
}

export default Recommendation;
