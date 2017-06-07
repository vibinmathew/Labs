import React, {Component} from "react";
import "./Button.css"

class Button extends Component {

    render() {
        return (
            <div className="button" onClick={this.props.clickAction}>
                <span>{this.props.buttonText}</span>
                <svg width="7" height="12" viewBox="0 0 7 12" xmlns="http://www.w3.org/2000/svg"><title>Next</title><g fill="none" fillRule="evenodd"><path fill="#26336E" d="M-316-15H27v44h-343z"/><path d="M.548 12a.424.424 0 0 1-.183-.043.291.291 0 0 1-.137-.13c-.121-.086-.182-.187-.182-.302 0-.115.06-.216.182-.302L5.661 6 .183.777C.06.691 0 .59 0 .475 0 .36.06.259.183.173.273.058.38 0 .503 0c.12 0 .227.058.319.173l5.843 5.525c.122.086.183.187.183.302 0 .115-.061.216-.183.302L.913 11.827a.714.714 0 0 1-.183.13.424.424 0 0 1-.182.043z" fill="#F4F4F4"/></g></svg>
            </div>
        );
    }

}

export default Button;
