import {connect} from "react-redux";
import DataPack from "../presenters/DataPack";
import {fetchLastBills} from "../modules/bills";
import {fetchBilling} from "../modules/billing";

function mapStateToProps(state) {
    return {
        bills: state.bills,
        billing: state.billing,
        navigation: state.navigation
    }
}
function mapDispatchToProps(dispatch) {
    return {
        fetchLastBills: (token) => dispatch(fetchLastBills(token)),
        fetchBilling: (resourcePath) => dispatch(fetchBilling(resourcePath)),
    };
}

const ConnectedDataPack = connect(mapStateToProps, mapDispatchToProps)(DataPack);

export default ConnectedDataPack;
