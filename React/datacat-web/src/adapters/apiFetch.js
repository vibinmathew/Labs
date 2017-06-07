import fetch from "isomorphic-fetch";

export default function apiLocation() {
    return '/api';
}

export function fetchFromApi(dispatch, path, actionCreator, errorActionCreator) {
    return fetch(apiLocation() + path)
        .then(response => {
            if (Math.floor(response.status / 100) !== 2){
                dispatch(errorActionCreator(response.status));
                return false;
            }
            return response.json()
        })
        .then(json => {
            if (json) {
                dispatch(actionCreator(json));
            }
        })
        .catch(() => {
            dispatch(errorActionCreator());
        })
}