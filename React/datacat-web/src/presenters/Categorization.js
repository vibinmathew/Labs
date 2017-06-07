import React, {Component, PropTypes} from "react";
import CategoryList from "./CategoryList"
import CategorizationGraph from "./CategorizationGraph"
import "./Categorization.css";
import {getShortMonthName, route} from "./utils";

class Categorization extends Component {

    render() {
        const {billing, dataCategorization} = this.props;
        return (
            <div>
                <div className="navigate-billing-cycle">
                    {this.renderPreviousBillingCycle(billing.previousBillingCycleUrl)}
                    <span className="billing-cycle">{this.renderBillingCycle(billing)}</span>
                    {this.renderNextBillingCycle(billing.nextBillingCycleUrl)}<br/><br/>
                </div>
                <CategorizationGraph {...this.props}/>
                <div className="graph-legend">
                    <span className="legend-selected-colour">&nbsp;</span>
                    <span>{dataCategorization.selectedCategory}</span>
                    <span className="legend-total-colour">&nbsp;</span>
                    <span>Total Usage</span>
                </div>
                <CategoryList {...this.props}/>
            </div>
        );
    }

    renderBillingCycle(billing) {

        if (billing && billing.start) {
            let startDate = new Date(billing.start);
            let endDate = new Date(billing.end);
            let billingCycle = (startDate.getDate() + ' ' + getShortMonthName(startDate) + ' - '
            + endDate.getDate() + ' ' + getShortMonthName(endDate) + ', ' + endDate.getFullYear());

            return billingCycle;
        }
        else {
            return "";
        }
    }

    renderNextBillingCycle(nextBillingCycleUrl) {
        let cls = nextBillingCycleUrl==="" ? "next-button-disabled" : "next-button";
        let routeIfAvailable = (url) => {
            if (url) {
                route('/usage/' + url);
            }
        }
        return <span id="next-billing-cycle" className={cls} onClick={() => routeIfAvailable(nextBillingCycleUrl)}>
            <svg width="24" height="24" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><title>F8D8122D-2645-490A-A903-B4766D1F9359</title><path d="M24 12c0 6.6-5.4 12-12 12S0 18.6 0 12 5.4 0 12 0s12 5.4 12 12zm-8.28-.42l-5.34-5.34a.58.58 0 0 0-.84 0 .58.58 0 0 0 0 .84L14.4 12l-4.92 4.92a.58.58 0 0 0 0 .84c.12.12.3.18.42.18s.3-.06.42-.18l5.34-5.34c.3-.24.3-.6.06-.84z"/></svg>
        </span>
    }

    renderPreviousBillingCycle(prevBillingCycleUrl) {
        let cls = prevBillingCycleUrl==="" ? "prev-button-disabled" : "prev-button";
        let routeIfAvailable = (url) => {
            if (url) {
                route('/usage/' + url);
            }
        }
        return <span id="prev-billing-cycle" className={cls} onClick={() => routeIfAvailable(prevBillingCycleUrl)}>
            <svg width="24" height="24" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><title>29AFDDC1-9834-4331-8409-7C0BDE8067F4</title><path d="M0 12c0 6.6 5.4 12 12 12s12-5.4 12-12S18.6 0 12 0 0 5.4 0 12zm8.28-.42l5.34-5.34c.24-.24.6-.24.84 0s.24.6 0 .84L9.6 12l4.92 4.92c.24.24.24.6 0 .84-.12.12-.3.18-.42.18s-.3-.06-.42-.18l-5.34-5.34c-.3-.24-.3-.6-.06-.84z"/></svg>
        </span>
    }
}
Categorization.propTypes = {
    billing: PropTypes.object,
    dataCategorization: PropTypes.object
};
export default Categorization;
