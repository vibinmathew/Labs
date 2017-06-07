import React, {Component} from "react";
import Header from './Header'
import Button from './Button'
import "./LoginForm.css"
import NotFound from "./NotFound"
import Spinner from "./Spinner"

class LoginForm extends Component {
    constructor(props) {
        super(props);
        this.state = {value: '', isValid: false, isButtonClicked: false};

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.resetLoginForm = this.resetLoginForm.bind(this);
    }

    handleChange(event) {
        this.setState({value: event.target.value});
        this.setState({isValid: /^614\d{8}$/.test(this.formatNumber(event.target.value)) });
    }

    handleSubmit() {
        this.setState({isButtonClicked: true});
        if(this.state.isValid) {
            this.props.sendSMS(this.formatNumber(this.state.value));
        }
    }

    formatNumber(number) {
        return number.replace(/\s|\(|\)/g, '').replace(/^(4|04|00614|\+614)/, "614");
    }

    render() {
        return (
            <div className="login">
                <Header title="My Telstra data usage"/>
                {this.renderMainSection()}
            </div>
        );
    }

    renderMainSection() {
        const {login} = this.props;
        const {value} = this.state;

        if (login.isFetching) {
            return <Spinner spinnerText={["Hang tight!", "We are looking for your number in our system"]}/>;
        } else if (login.isNotFound) {
            return <NotFound msisdn={value} resetSentStatus={this.props.resetSentStatus}/>;
        } else if (login.sent) {
            return this.renderInfoMessage();
        }
        return this.renderForm();
    }


    renderForm() {
        return <div className="login-container">
            <div className="top-space">
                <div className="welcome-text">You've been selected to preview a new tool from Telstra</div>
            </div>
            <div className="middle-space">
                <div className="input-box">
                    <div className="invalid-number-message">{!this.state.isValid && this.state.isButtonClicked ? 'Please enter a valid mobile number and try again' : '\u00A0'}</div>
                    <input className={!this.state.isValid && this.state.isButtonClicked ? 'invalid-number' : ''} type="tel" value={this.state.value} onChange={this.handleChange}/>
                    {this.state.isValid ? <div className="tick">
                        <svg width="14" height="11" viewBox="0 0 14 11" xmlns="http://www.w3.org/2000/svg"><title>OK</title><defs><path d="M0 0h342v44H0z"/><mask x="0" y="0" width="342" height="44" fill="#fff"></mask></defs><g fill="none" fillRule="evenodd"><use mask="url(#b)" transform="translate(-313 -17)" stroke="#A6A6A6" strokeWidth="2" fill="#FFF"/><path d="M13.88.124c-.164-.165-.371-.165-.536 0L4.09 9.419.66 5.99c-.165-.165-.372-.165-.537 0-.165.165-.165.372 0 .537L3.8 10.204c.082.083.165.124.248.124.082 0 .206-.041.247-.124L13.84.661c.207-.165.207-.372.042-.537z" fill="#00A6A4" fillRule="nonzero"/></g></svg>
                    </div> : null}
                </div>
                <Button buttonText="Send me an SMS" clickAction={this.handleSubmit}/>
            </div>
            <div className="bottom-space"></div>
        </div>
    }

    resetLoginForm() {
        this.props.resetSentStatus();
        this.setState({value: '', isValid: false, isButtonClicked: false});
    }

    renderInfoMessage() {
        return (
        <div className="message-container">
            <div className="message-centered">
                <svg width="52" height="38" viewBox="0 0 52 38" xmlns="http://www.w3.org/2000/svg"><title>Done</title><path d="M51.095.456c-.609-.608-1.369-.608-1.977 0L15.055 34.671 2.433 22.05c-.608-.609-1.368-.609-1.977 0-.608.608-.608 1.368 0 1.977L13.99 37.56c.304.305.608.457.913.457.304 0 .76-.152.912-.457L50.943 2.433c.76-.608.76-1.369.152-1.977z" fillRule="nonzero" fill="#333"/></svg>
                <div className="info-text">Your SMS is on the way</div>
                <div className="back-link" onClick={this.resetLoginForm}>Didn't recieve an SMS?</div>
            </div>
        </div>
        )
    }
}
export default LoginForm;
