import React, {Component} from "react";
import "./DataUsage.css";
import ConnectedCategorization from "../connectors/ConnectedCategorization"
import ConnectedSummary from "../connectors/ConnectedSummary"
import ConnectedUserInfo from "../connectors/ConnectedUserInfo"
import Header from './Header'
import Spinner from './Spinner'
import Error from './Error'
import Expiry from './Expiry'
import Button from './Button'
import {route, redirect} from "./utils"

class DataUsage extends Component {

    constructor(props) {
        super(props);
        const {params} = this.props;

        let index = params.index ? "/" + params.index : '';

        this.props.fetchBilling(params.token + index);
        this.props.fetchUserInfo(params.token);
        this.props.fetchDataCategorization(params.token + index);
        if (!this.props.bills.lastBills) {
            this.props.fetchLastBills(params.token);
        }
        this.renderContent = this.renderContent.bind(this);
        this.redirectToDataPack = this.redirectToDataPack.bind(this);
        this.renderFooter = this.renderFooter.bind(this);
        this.renderHeader = this.renderHeader.bind(this);
    }

    componentWillReceiveProps(nextProps) {
        if (this.props.params.index !== nextProps.params.index) {
            this.props.fetchBilling(nextProps.params.token + "/" + nextProps.params.index);
            this.props.fetchUserInfo(nextProps.params.token);
            this.props.fetchDataCategorization(nextProps.params.token + "/" + nextProps.params.index);
        }
    }

    render() {
        return (
            <div className="App">
                {this.renderHeader()}
                {this.renderContent()}
            </div>
        );
    }

    renderHeader() {
        let backPath = "/dashboard/" + this.props.params.token;
        return <Header title="My Telstra data usage" backPath={backPath}/>
    }

    renderContent() {
        const {user, billing, dataCategorization} = this.props;
        if (user.isExpired || billing.isExpired || dataCategorization.isExpired) {
            return <Expiry />;
        } else if (user.isError || billing.isError || dataCategorization.isError) {
            return <Error message="We can't seem to load your data usage."/>
        } else if (user.isFetching || billing.isFetching || dataCategorization.isFetching) {
            return <Spinner spinnerText={["Hang tight!", "We are loading your data usage."]}/>
        } else {
            return this.renderData()
        }
    }

    redirectToDataPack() {
        route('/datapack/' + this.props.params.token)
    }

    renderData() {
        return <div>
            <ConnectedSummary />
            <ConnectedUserInfo />
            <ConnectedCategorization />
            {this.renderFooter()}
        </div>
    }

    renderFooter() {
        if (this.props.bills.lastBills) {
            if (this.props.bills.recommendedDataPack.price > 0){
                return <Button clickAction={this.redirectToDataPack} buttonText="Reduce my future bills"/>
            } else {
                return <div className="live-chat-container">
                            <div className="live-chat-message">
                                <div>Still have questions?</div>
                                <div>Chat with a Customer Service Agent about your service</div>
                            </div>

                            <Button clickAction={this.redirectToChat} buttonText="Telstra 24x7 Live Chat"/>
                        </div>
            }
        }
    }

    redirectToChat() {
        redirect("https://www.telstra.com.au/chatnow/landing?pageId=https%3a%2f%2flivechat.telstra.com%2fTB%3aBusiness%3aDefault");
    }
}

export default DataUsage;
