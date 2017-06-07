import React from "react";
import ReactDOM from "react-dom";
import {Provider} from "react-redux";
import {Router, Route} from "react-router";
import store, {history} from "./store";
import ConnectedDataUsage from "./connectors/ConnectedDataUsage";
import ConnectedLogin from "./connectors/ConnectedLogin";
import ConnectedDataPack from "./connectors/ConnectedDataPack";
import "./index.css";
import ConnectedDashboard from "./connectors/ConnectedDashboard";

ReactDOM.render(
    <Provider store={store}>
        <Router history={history}>
            <Route path="/usage/:token" component={ConnectedDataUsage}/>
            <Route path="/usage/:token/:index" component={ConnectedDataUsage}/>
            <Route path="/datapack/:token" component={ConnectedDataPack}/>
            <Route path="/" component={ConnectedLogin}/>
            <Route path="/dashboard/:token" component={ConnectedDashboard}/>
        </Router>
    </Provider>,
  document.getElementById('root')
);
