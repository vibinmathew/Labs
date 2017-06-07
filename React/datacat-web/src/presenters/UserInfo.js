import React, {Component, PropTypes} from "react";
import "./UserInfo.css";

class UserInfo extends Component {

    render() {
        const {user, billing} = this.props;
        return (
            <div className="userinfo">
                <div>{this.renderUserInfo(user)}</div>
                <div>{this.renderUpdatedAt(billing)}</div>
            </div>
        );
    }

    renderUserInfo(user) {
        if (user && user.msisdn) {
            return `This data is an approximation of actual usage for ${user.msisdn}.`;
        }
    }

    renderUpdatedAt(billing) {
        if (billing && billing.updatedAt && !isNaN(billing.updatedAt.getDate())) {
            let date = new Date(billing.updatedAt);
            return "Last updated at " + date.getHours() % 12 + ":" + ("0" + date.getMinutes()).slice(-2) + ( date.getHours() >= 12 ? " PM" : " AM" ) + " on " + date.getDate() + "/" + (date.getMonth() + 1) + "/" + date.getFullYear();
        }
    }
}

UserInfo.propTypes = {
    user: PropTypes.object,
    billing: PropTypes.object
};

export default UserInfo;
