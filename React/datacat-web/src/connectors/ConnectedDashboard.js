import {connect} from "react-redux";
import Dashboard from "../presenters/Dashboard";
import {fetchLastBills} from "../modules/bills";

function mapStateToProps(state) {
    return {
        bills: state.bills,
    }
}
function mapDispatchToProps(dispatch) {
    return {
        fetchLastBills: (token) => dispatch(fetchLastBills(token)),
    };
}

const ConnectedDashboard = connect(mapStateToProps, mapDispatchToProps)(Dashboard);

export default ConnectedDashboard;
