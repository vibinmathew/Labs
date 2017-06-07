import {connect} from "react-redux";
import Categorization from "../presenters/Categorization";
import {selectCategory} from "../modules/dataCategorization";

function mapStateToProps(state) {
    return {
        dataCategorization: state.dataCategorization,
        billing: state.billing
    }
}
function mapDispatchToProps(dispatch) {
    return {
        selectCategory: (category) => dispatch(selectCategory(category)),
    };
}

const ConnectedCategorization = connect(mapStateToProps, mapDispatchToProps)(Categorization);

export default ConnectedCategorization;
