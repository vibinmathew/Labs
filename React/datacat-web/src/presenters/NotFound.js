import React, {Component} from "react";

class NotFound extends Component {

    constructor(props) {
        super(props);
        this.resetLoginForm = this.resetLoginForm.bind(this);
    }

    render() {
        return (
            <div className="message-container">
                <div className="message-centered">
                    <svg width="40" height="39" viewBox="0 0 40 39" xmlns="http://www.w3.org/2000/svg"><title>9AB7C127-F19D-498E-BD55-9ED6C1F002D4</title><g stroke="#333" strokeWidth="2.5" fill="#D8D8D8" fillRule="evenodd" strokeLinecap="round"><path d="M2.5 37.5L38 2M37.5 37.5L2 2"/></g></svg>
                    <div className="not-found-text error-text">We couldn’t find {this.props.msisdn} on our system</div>
                    <div className="back-link" onClick={this.resetLoginForm}>Try again?</div>
                </div>
            </div>

        )
    }

    resetLoginForm() {
        this.props.resetSentStatus();
    }
}

export default NotFound;
