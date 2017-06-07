import React, {Component} from "react";
import {redirect} from "./utils"
import "./Error.css";
import Button from "./Button"

class Error extends Component {

    redirectToChat() {
        redirect("https://www.telstra.com.au/chatnow/landing");
    }
    
    render() {
        const {message} = this.props;
        return (
            <div className="message-container">
                <div className="message-centered">
                    <svg width="48" height="52" viewBox="0 0 48 52" xmlns="http://www.w3.org/2000/svg"><title>Bars</title><path d="M9.989 15.957H4.022C1.816 15.957 0 17.773 0 19.978V47.87c0 2.206 1.816 4.022 4.022 4.022h5.967c2.205 0 4.022-1.816 4.022-4.022V19.978c0-2.205-1.817-4.021-4.022-4.021zm0 31.913H4.022V19.978h5.967V47.87zM26.984 0h-5.968c-2.205 0-4.021 1.816-4.021 4.022V47.87c0 2.206 1.816 4.022 4.021 4.022h5.968c2.205 0 4.021-1.816 4.021-4.022V4.022C31.005 1.816 29.19 0 26.984 0zm0 47.87h-5.968V4.022h5.968V47.87zm16.994-16.994h-5.967c-2.206 0-4.022 1.816-4.022 4.022V47.87c0 2.206 1.816 4.022 4.022 4.022h5.967c2.206 0 4.022-1.816 4.022-4.022V34.898c0-2.206-1.816-4.022-4.022-4.022zm0 16.994h-5.967V34.897h5.967V47.87z" fillRule="nonzero" fill="#DADADA"/></svg>
                    <div className="error-text">Oops!<br/> {message}</div>
                    <div className="try-again-text">Please check again soon.</div>
                </div>
                <Button buttonText="Chat with a Customer Service Agent" clickAction={this.redirectToChat}/>
            </div>
        )
    }

}

export default Error;
