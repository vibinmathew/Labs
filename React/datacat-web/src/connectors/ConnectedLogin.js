import {connect} from "react-redux";
import LoginForm from "../presenters/LoginForm";
import {sendSMS, resetSentStatus} from "../modules/login";

function mapStateToProps(state) {
    return {
        login: state.login,
    }
}
function mapDispatchToProps(dispatch) {
    return {
        sendSMS: (number) => dispatch(sendSMS(number)),
        resetSentStatus : () => dispatch(resetSentStatus())
    };
}

const ConnectedLogin = connect(mapStateToProps, mapDispatchToProps)(LoginForm);

export default ConnectedLogin;
