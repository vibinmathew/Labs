import React, {Component} from "react";
import "./Header.css"
import {route} from "./utils"

class Header extends Component {

    constructor(props) {
        super(props);
        this.onBackClick = this.onBackClick.bind(this);
        this.renderBack = this.renderBack.bind(this);
    }

    render() {
        return (
            <div className="App-header">
                {this.props.backPath? this.renderBack() : this.renderLogo()}
                <span className="title">{this.props.title}</span>
            </div>
        );
    }
    
    renderBack() {
        return <div className="back" onClick={this.onBackClick}>
                    <svg xmlns="http://www.w3.org/2000/svg" width="12" height="20" viewBox="0 0 12 20"><g fill="none" fillRule="evenodd"><path fill="#FFF" d="M-16-14h375v48H-16z"/><path fill="#26336E" fillRule="nonzero" d="M11 2.429c.571-.572.571-1.429 0-2-.572-.572-1.429-.572-2 0L.429 9c-.572.571-.572 1.429 0 2l8.5 8.571a1.41 1.41 0 0 0 1 .429 1.41 1.41 0 0 0 1-.429c.571-.571.571-1.428 0-2L3.429 10 11 2.429z"/></g></svg>
                    <span className="back-link-text">Home</span>
                </div>;
    }
    
    renderLogo() {
        return <svg width="26" height="31" viewBox="0 0 26 31" xmlns="http://www.w3.org/2000/svg"><title>Telstra Logo</title><defs><path id="a" d="M.016.143v23.271h24.772V.144H.016z"/></defs><g fill="none" fillRule="evenodd"><path fill="#FFF" d="M-16-10h375v48H-16z"/><g transform="translate(0 7.628)"><path d="M17.477 6.134l-1.155 7.017c-.239 1.262-1.043 1.604-1.757 1.604h-5.35l2.254-12.852C9.218.838 6.92.143 5.09.143 3.347.143 1.933.63.993 1.797.33 2.627 0 3.65 0 4.867c0 3.654 2.78 8.725 7.538 12.86 4.242 3.656 8.908 5.703 12.304 5.703 1.694 0 3.061-.536 3.958-1.607.707-.828.988-1.903.988-3.12 0-3.55-2.8-8.528-7.31-12.57" fill="#0099F9" mask="url(#b)"/></g><path d="M7.87.167c-.849 0-1.555.59-1.743 1.515l-.753 4.236h6.729L9.214 22.384h5.351c.714 0 1.518-.342 1.757-1.605l2.447-14.861h4.84c.852 0 1.556-.586 1.745-1.51l.755-4.24H7.871" fill="#001E82"/></g></svg>
    }

    onBackClick() {
        route(this.props.backPath);
    }
}

export default Header;
