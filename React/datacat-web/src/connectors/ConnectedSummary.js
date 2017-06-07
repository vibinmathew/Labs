import {connect} from "react-redux";
import Summary from "../presenters/Summary";

function mapStateToProps(state) {
    return {
        billing: state.billing
    }
}

const ConnectedSummary = connect(mapStateToProps)(Summary);

export default ConnectedSummary;
