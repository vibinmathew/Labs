import {createStore, applyMiddleware} from "redux";
import {syncHistoryWithStore, routerMiddleware} from "react-router-redux";
import {browserHistory} from "react-router";
import thunkMiddleware from "redux-thunk";
import createLogger from "redux-logger";
import rootReducer from "./rootReducer";

const loggerMiddleware = createLogger();

const store = createStore(
    rootReducer,
    applyMiddleware(
        routerMiddleware(browserHistory),
        thunkMiddleware,
        loggerMiddleware,
    )
);

export const history = syncHistoryWithStore(browserHistory, store);

export default store;