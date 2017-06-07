import React, {Component, PropTypes} from "react";
import {convertToGb} from "./utils";
import "./Summary.css";

class Summary extends Component {

    render() {

        const {billing} = this.props;

        return (
            <div>
                <div className="usage-container">
                    {this.renderDataUsage(billing)}

                    { this.renderDaysLeft(billing) }

                </div>
            </div>
        );
    }

    renderDataUsage(billing) {
        return billing && billing.usage ? (
            <div className="data-used">
                <span
                    className={billing.usage > billing.allowance ? "usage text-red" : "usage"}>{convertToGb(billing.usage)}</span>
                <span><span className="allowance">{convertToGb(billing.allowance)}</span><sup>GB</sup></span>
            </div>
        ) : ''
    }

    renderDaysLeft(billing) {
        if (billing && billing.end) {
            let endTime = new Date(billing.end);
            let todayTime = new Date();

            let endDate = new Date(endTime.getFullYear(), endTime.getMonth(), endTime.getDate());
            let todayDate = new Date(todayTime.getFullYear(), todayTime.getMonth(), todayTime.getDate());

            let millisecondsLeft = endDate - todayDate;

            let daysLeft = Math.round(millisecondsLeft / (1000 * 3600 * 24));

            if (daysLeft < 0) {
                return <span className="previous-cycle">Previous Billing Cycle</span>
            } else {
                return <div className="days-left"> {daysLeft} days left</div>
            }

        }
    }
}

Summary.propTypes = {
    billing: PropTypes.object
};

export default Summary;
