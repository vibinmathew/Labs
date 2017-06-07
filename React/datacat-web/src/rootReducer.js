import {combineReducers} from "redux";
import {routerReducer} from "react-router-redux";
import user from "./modules/user";
import dataCategorization from "./modules/dataCategorization";
import billing from "./modules/billing";
import login from "./modules/login"
import bills from "./modules/bills"

const rootReducer = combineReducers({
    user,
    dataCategorization,
    billing,
    login,
    bills,
    routing: routerReducer,
});

export default rootReducer