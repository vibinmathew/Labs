import {connect} from "react-redux";
import UserInfo from "../presenters/UserInfo";


function mapStateToProps(state) {
    return {
        user: state.user,
        billing: state.billing
    }
}

const ConnectedUserInfo = connect(mapStateToProps)(UserInfo);

export default ConnectedUserInfo;
