import React, {Component} from "react";
import Header from './Header'
import LastBills from './LastBills'
import Expiry from './Expiry'
import Error from './Error'
import Recommendation from './Recommendation'
import Spinner from "./Spinner"
import {redirect} from './utils'
import Button from './Button'

class DataPack extends Component {

    constructor(props) {
        super(props);
        const {params, bills, billing} = this.props;
        if (!bills.lastBills) {
            this.props.fetchLastBills(params.token);
        }
        if (!billing.baseBill) {
            this.props.fetchBilling(params.token);
        }
        this.renderContent = this.renderContent.bind(this);
    }

    render() {
        let backPath = "/dashboard/" + this.props.params.token;
        return (
            <div className="App">
                <Header title="Reduce my bill" backPath={backPath}/>
                {this.renderContent()}
            </div>
        );
    }

    renderContent() {
        const {bills, billing} = this.props;
        if (bills.isExpired || billing.isExpired) {
            return <Expiry/>
        }
        if (bills.isError || billing.isError) {
            return <Error message="We can't seem to load your data recommendation."/>
        }
        if (bills.isFetching || billing.isFetching) {
            return <Spinner spinnerText={["Hang tight!", "We are loading your data recommendations."]}/>;
        }
        if (bills.lastBills && billing.baseBill) {
            return (
                <div>
                    <LastBills lastBills={bills.lastBills}/>
                    <Recommendation bills={bills} billing={billing}/>
                    <Button clickAction={this.redirectToDataPacksMoreInfo} buttonText="Learn more about data packs" />
                </div>
            )
        }
    }

    redirectToDataPacksMoreInfo() {
        redirect("https://www.telstra.com.au/small-business/mobile-phones/mobile-applications-and-services/voice-and-data-packs");
    }
}

export default DataPack;
