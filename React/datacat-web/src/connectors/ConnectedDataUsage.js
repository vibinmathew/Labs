import {connect} from "react-redux";
import DataUsage from "../presenters/DataUsage";
import {fetchUserInfo} from "../modules/user";
import {fetchBilling} from "../modules/billing";
import {fetchDataCategorization} from "../modules/dataCategorization";
import {fetchLastBills} from "../modules/bills";

function mapStateToProps(state) {
    return {
        user: state.user,
        billing: state.billing,
        dataCategorization: state.dataCategorization,
        bills: state.bills,
        navigation: state.navigation
    }
}
function mapDispatchToProps(dispatch) {
    return {
        fetchUserInfo: (token) => dispatch(fetchUserInfo(token)),
        fetchBilling: (resourcePath) => dispatch(fetchBilling(resourcePath)),
        fetchDataCategorization: (resourcePath) => dispatch(fetchDataCategorization(resourcePath)),
        fetchLastBills: (token) => dispatch(fetchLastBills(token)),
    };
}
const ConnectedDataUsage = connect(mapStateToProps, mapDispatchToProps)(DataUsage);

export default ConnectedDataUsage;
