import React, {Component} from "react";
import Button from './Button'
import {route, redirect} from "./utils"
import "./Dashboard.css"

class Dashboard extends Component {

    constructor(props) {
        super(props);
        this.redirectToDataUsage = this.redirectToDataUsage.bind(this);
        this.redirectToDataPack = this.redirectToDataPack.bind(this);
        this.renderDatapack = this.renderDatapack.bind(this);

        if (!this.props.bills.lastBills) {
            this.props.fetchLastBills(this.props.params.token);
        }
    }

    render() {
        return (
            <div className="App">
                <div className="header-section">

                    <svg
                        className="gradient"
                        xmlns="http://www.w3.org/2000/svg"
                        id="svg8"
                        version="1.1"
                        viewBox="0 0 99.218747 37.835415"
                        >
                        <defs
                            id="defs2">
                            <polygon
                                id="path-1"
                                points="36.68418,0.2066602 0.0234,0.2066602 0.0234,33.764977 36.68418,33.764977 " />
                        </defs>

                        <g
                            transform="translate(0,-259.1646)"
                            id="layer1">
                            <g
                                transform="matrix(0.26458333,0,0,0.26458333,-0.00537303,255.2893)"
                                id="g3710">
                                <g
                                    id="Data-Cat---V7-Branded">
                                    <g
                                        id="Dashboard"
                                        transform="translate(0,-79)">
                                        <polygon id="Path-3" className="st0" points="0,172 375,236 375,219.6 "/>
                                    </g>
                                </g>
                                <g
                                    id="Data-Cat---V7-Branded_2_">
                                    <g
                                        id="Dashboard_2_"
                                        transform="translate(0,-79)">
                                        <linearGradient
                                            id="Path-3_1_"
                                            gradientUnits="userSpaceOnUse"
                                            x1="0"
                                            y1="149.3"
                                            x2="375"
                                            y2="149.3">
                                            <stop stopColor="#C69DC9" offset="0" id="stop3685"></stop>
                                            <stop stopColor="#5BC5F2" offset="1" id="stop3687"></stop>
                                        </linearGradient>
                                        <polygon id="Path-3_2_" className="st1" points="375,79 375,219.6 0,172 0,79 "/>
                                    </g>
                                </g>
                            </g>
                            <image
                                width="10.417971"
                                height="11.90625"
                                preserveAspectRatio="none"
                                xlinkHref="data:image/png;base64,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
"
                                id="image3753"
                                x="5.2916665"
                                y="263.13336" />
                        </g>
                    </svg>
                </div>
                <div className="main-content">
                    <div className="dashboard-help-text">What would you like to do?</div>
                    <div className="datausage">
                        <svg xmlns="http://www.w3.org/2000/svg" width="40" height="44" viewBox="0 0 40 44">
                            <g fill="none" fillRule="evenodd">
                                <path fill="#FFF" d="M207-115l-375-64v588h375z"/>
                                <path fill="#00AAF3" fillRule="nonzero" d="M8.324 13.297H3.351A3.368 3.368 0 0 0 0 16.65v23.243a3.368 3.368 0 0 0 3.351 3.351h4.973a3.368 3.368 0 0 0 3.351-3.351V16.649a3.368 3.368 0 0 0-3.35-3.352zm0 26.595H3.351V16.649h4.973v23.243zM22.486 0h-4.972a3.368 3.368 0 0 0-3.352 3.351v36.541a3.368 3.368 0 0 0 3.352 3.351h4.972a3.368 3.368 0 0 0 3.352-3.351V3.352A3.368 3.368 0 0 0 22.486 0zm0 39.892h-4.972V3.352h4.972v36.54zM36.65 25.73h-4.973a3.368 3.368 0 0 0-3.352 3.351v10.811a3.368 3.368 0 0 0 3.352 3.351h4.973A3.368 3.368 0 0 0 40 39.893V29.08a3.368 3.368 0 0 0-3.351-3.351zm0 14.162h-4.973v-10.81h4.973v10.81z"/>
                            </g>
                        </svg>
                        <div className="help-text">I want to understand how <br /> I'm using my data</div>
                        <Button displayName="data-usage-btn" clickAction={this.redirectToDataUsage} buttonText="View my data usage" />
                    </div>
                    {this.renderDatapack()}
                </div>
            </div>
        );
    }
    redirectToDataUsage() {
        route('/usage/' + this.props.params.token)
    }
    redirectToDataPack() {
        route('/datapack/' + this.props.params.token)
    }
    renderDatapack() {
        if (this.props.bills.lastBills) {
            if (this.props.bills.recommendedDataPack.price > 0){
                return(
                <div className="datapack">
                    <svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" viewBox="0 0 40 40">
                        <g fill="none" fillRule="evenodd">
                            <path fill="#FFF" d="M207-324l-375-64v588h375z"/>
                            <path fill="#00AAF3" fillRule="nonzero" d="M26.633 24.539h4.589c.798 0 1.496-.698 1.496-1.497 0-.798-.698-1.496-1.496-1.496h-4.589c-.798 0-1.496.698-1.496 1.496 0 .799.698 1.497 1.496 1.497zm0-7.083h4.589c.798 0 1.496-.698 1.496-1.496s-.698-1.496-1.496-1.496h-4.589c-.798 0-1.496.698-1.496 1.496s.698 1.496 1.496 1.496zm4.689 11.172h-4.689c-.798 0-1.496.699-1.496 1.497 0 .798.698 1.496 1.496 1.496h4.689c.798 0 1.496-.698 1.496-1.496s-.599-1.497-1.496-1.497zm3.69-21.945h-1.795V4.888A4.896 4.896 0 0 0 28.329 0H4.888A4.896 4.896 0 0 0 0 4.888V27.93a4.896 4.896 0 0 0 4.888 4.888h1.795v1.795a4.896 4.896 0 0 0 4.888 4.888h23.541A4.896 4.896 0 0 0 40 34.613V11.571c-.1-2.693-2.294-4.888-4.988-4.888zM6.684 11.571v18.155H4.888c-.898 0-1.696-.798-1.696-1.696V4.888c0-.898.798-1.696 1.696-1.696h23.54c.899 0 1.697.798 1.697 1.696v1.795H11.47c-2.693 0-4.788 2.195-4.788 4.888zm30.025 23.042c0 .898-.798 1.696-1.696 1.696h-23.54c-.898 0-1.696-.798-1.696-1.696V11.571c0-.898.798-1.696 1.695-1.696h23.541c.898 0 1.696.798 1.696 1.696v23.042zM20.15 22.643c-.4-.399-.898-.698-1.397-.997-.499-.3-.997-.599-1.396-.798-.4-.2-.798-.499-1.098-.698-.299-.2-.399-.5-.399-.798v-.3c0-.399.1-.698.4-.897.299-.2.698-.3 1.097-.3h.2c.498 0 .897.1 1.096.3.3.199.4.399.4.698 0 .399.099.598.299.798.2.2.498.3.798.3.299 0 .598-.1.798-.3.2-.2.399-.499.399-.898s-.1-.798-.2-1.097c-.1-.3-.3-.599-.598-.898-.3-.3-.5-.499-.898-.698-.3-.2-.698-.3-1.097-.4v-.897c0-.399-.1-.698-.4-.898-.199-.2-.498-.299-.798-.299-.299 0-.498.1-.798.3-.199.199-.399.498-.399.897v.798c-.399.1-.698.2-.997.4-.3.199-.599.398-.898.697-.3.3-.499.599-.598.998-.2.399-.2.798-.2 1.197v.399c0 .598.1 1.197.4 1.596.299.399.598.798 1.096 1.197.4.3.898.698 1.397.898.499.299.997.498 1.397.798.399.3.798.598 1.097.897.299.3.399.699.399 1.098 0 .499-.2.897-.499 1.197-.3.299-.698.399-1.197.399h-.2c-.498 0-.897-.1-1.196-.4-.3-.298-.5-.697-.5-1.296 0-.399-.099-.698-.398-.898-.2-.2-.499-.299-.798-.299-.3 0-.599.1-.798.3-.2.199-.4.498-.4.897 0 1.097.3 1.895.799 2.594.598.698 1.297 1.097 2.094 1.396v.598c0 .4.1.699.4.898.199.2.498.3.798.3.299 0 .498-.1.798-.3.2-.2.399-.498.399-.898v-.598a3.781 3.781 0 0 0 2.094-1.297c.599-.598.799-1.496.799-2.493 0-.699-.1-1.297-.4-1.796-.1-.499-.498-.997-.897-1.396z"/>
                        </g>
                    </svg>
                    <div className="help-text">I often exceed my data and I'd like <br /> to reduce my future bills</div>
                    <Button clickAction={this.redirectToDataPack} buttonText="Reduce my future bills" />
                </div>)
            } else {
                return(
                    <div className="managemydata">
                        <svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" viewBox="0 0 40 40">
                            <g fill="none" fillRule="evenodd">
                                <path fill="#FFF" d="M207-324l-375-64v588h375z"/>
                                <path fill="#00AAF3" fillRule="nonzero" d="M26.633 24.539h4.589c.798 0 1.496-.698 1.496-1.497 0-.798-.698-1.496-1.496-1.496h-4.589c-.798 0-1.496.698-1.496 1.496 0 .799.698 1.497 1.496 1.497zm0-7.083h4.589c.798 0 1.496-.698 1.496-1.496s-.698-1.496-1.496-1.496h-4.589c-.798 0-1.496.698-1.496 1.496s.698 1.496 1.496 1.496zm4.689 11.172h-4.689c-.798 0-1.496.699-1.496 1.497 0 .798.698 1.496 1.496 1.496h4.689c.798 0 1.496-.698 1.496-1.496s-.599-1.497-1.496-1.497zm3.69-21.945h-1.795V4.888A4.896 4.896 0 0 0 28.329 0H4.888A4.896 4.896 0 0 0 0 4.888V27.93a4.896 4.896 0 0 0 4.888 4.888h1.795v1.795a4.896 4.896 0 0 0 4.888 4.888h23.541A4.896 4.896 0 0 0 40 34.613V11.571c-.1-2.693-2.294-4.888-4.988-4.888zM6.684 11.571v18.155H4.888c-.898 0-1.696-.798-1.696-1.696V4.888c0-.898.798-1.696 1.696-1.696h23.54c.899 0 1.697.798 1.697 1.696v1.795H11.47c-2.693 0-4.788 2.195-4.788 4.888zm30.025 23.042c0 .898-.798 1.696-1.696 1.696h-23.54c-.898 0-1.696-.798-1.696-1.696V11.571c0-.898.798-1.696 1.695-1.696h23.541c.898 0 1.696.798 1.696 1.696v23.042zM20.15 22.643c-.4-.399-.898-.698-1.397-.997-.499-.3-.997-.599-1.396-.798-.4-.2-.798-.499-1.098-.698-.299-.2-.399-.5-.399-.798v-.3c0-.399.1-.698.4-.897.299-.2.698-.3 1.097-.3h.2c.498 0 .897.1 1.096.3.3.199.4.399.4.698 0 .399.099.598.299.798.2.2.498.3.798.3.299 0 .598-.1.798-.3.2-.2.399-.499.399-.898s-.1-.798-.2-1.097c-.1-.3-.3-.599-.598-.898-.3-.3-.5-.499-.898-.698-.3-.2-.698-.3-1.097-.4v-.897c0-.399-.1-.698-.4-.898-.199-.2-.498-.299-.798-.299-.299 0-.498.1-.798.3-.199.199-.399.498-.399.897v.798c-.399.1-.698.2-.997.4-.3.199-.599.398-.898.697-.3.3-.499.599-.598.998-.2.399-.2.798-.2 1.197v.399c0 .598.1 1.197.4 1.596.299.399.598.798 1.096 1.197.4.3.898.698 1.397.898.499.299.997.498 1.397.798.399.3.798.598 1.097.897.299.3.399.699.399 1.098 0 .499-.2.897-.499 1.197-.3.299-.698.399-1.197.399h-.2c-.498 0-.897-.1-1.196-.4-.3-.298-.5-.697-.5-1.296 0-.399-.099-.698-.398-.898-.2-.2-.499-.299-.798-.299-.3 0-.599.1-.798.3-.2.199-.4.498-.4.897 0 1.097.3 1.895.799 2.594.598.698 1.297 1.097 2.094 1.396v.598c0 .4.1.699.4.898.199.2.498.3.798.3.299 0 .498-.1.798-.3.2-.2.399-.498.399-.898v-.598a3.781 3.781 0 0 0 2.094-1.297c.599-.598.799-1.496.799-2.493 0-.699-.1-1.297-.4-1.796-.1-.499-.498-.997-.897-1.396z"/>
                            </g>
                        </svg>
                        <div className="help-text">I don't exceed often and I'd like <br /> to know how I can manage my data</div>
                        <Button clickAction={this.redirectToManageYourUsage} buttonText="Manage my data" />
                    </div>)
            }
        }
    }
    redirectToManageYourUsage() {
        redirect("https://www.telstra.com.au/help/my-account/manage-your-usage");
    }
}

export default Dashboard;