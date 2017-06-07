import * as client from "../adapters/client";

const REQUEST_USER_INFO = 'REQUEST_USER_INFO';
const RECEIVED_USER_INFO = 'RECEIVED_USER_INFO';
const RECEIVED_USER_INFO_ERROR = 'RECEIVED_USER_INFO_ERROR';
const RECEIVED_USER_INFO_EXPIRY = 'RECEIVED_USER_INFO_EXPIRY';

export default function reducer(state = {}, action) {
    switch (action.type) {
        case REQUEST_USER_INFO:
            return Object.assign({}, state, {
                isFetching: true,
                isError: false,
                isExpired: false,
            });
        case RECEIVED_USER_INFO:
            return Object.assign({}, state, {
                isFetching: false,
                msisdn: action.msisdn
            });
        case RECEIVED_USER_INFO_ERROR:
            return Object.assign({}, state, {
                isFetching: false,
                isError: true,
            });
        case RECEIVED_USER_INFO_EXPIRY:
            return Object.assign({}, state, {
                isFetching: false,
                isExpired: true,
            });
        default:
            return state
    }
}

export function requestUserInfo() {
    return {
        type: REQUEST_USER_INFO
    }
}

export function receiveUserInfo(json) {
    return {
        type: RECEIVED_USER_INFO,
        msisdn: json.msisdn
    }
}

export function receiveUserInfoError(httpStatus) {
    return {
        type: httpStatus === 404 ? RECEIVED_USER_INFO_EXPIRY : RECEIVED_USER_INFO_ERROR,
    }
}

export function fetchUserInfo(token, fetcher = client.fetchUserInfo) {
    return (dispatch) => {
        dispatch(requestUserInfo());
        fetcher(dispatch, receiveUserInfo, receiveUserInfoError, token);
    }
}
