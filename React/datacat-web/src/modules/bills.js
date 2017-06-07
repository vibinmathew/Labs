import * as client from "../adapters/client";

const REQUEST_LAST_BILLS = 'REQUEST_LAST_BILLS';
const RECEIVED_LAST_BILLS = 'RECEIVED_LAST_BILLS';
const RECEIVED_LAST_BILLS_ERROR = 'RECEIVED_LAST_BILLS_ERROR';
const RECEIVED_LAST_BILLS_EXPIRY = 'RECEIVED_LAST_BILLS_EXPIRY';

export default function reducer(state = {}, action) {
    switch (action.type) {
        case REQUEST_LAST_BILLS:
            return Object.assign({}, state, {
                isFetching: true,
                isError: false,
                isExpired: false,
            });
        case RECEIVED_LAST_BILLS:
            return Object.assign({}, state, {
                isFetching: false,
                lastBills: action.lastBills,
                recommendedDataPack: action.recommendedDataPack
            });
        case RECEIVED_LAST_BILLS_ERROR:
            return Object.assign({}, state, {
                isFetching: false,
                isError: true
            });
        case RECEIVED_LAST_BILLS_EXPIRY:
            return Object.assign({}, state, {
                isFetching: false,
                isExpired: true
            });
        default:
            return state
    }
}

export function requestLastBills() {
    return {
        type: REQUEST_LAST_BILLS
    }
}

export function receiveLastBills(json) {
    let total = json.reduce((prev, next) => ({extraDataCharge : prev.extraDataCharge + next.extraDataCharge}));
    let averageExtraDataCharge = total.extraDataCharge / json.length;

    let recommendedDataPack = {};
    if (averageExtraDataCharge > 50) {
        recommendedDataPack = {
            price: 55,
            amount: 8
        };
    } else if (averageExtraDataCharge > 30) {
        recommendedDataPack = {
            price: 35,
            amount: 5
        };
    } else if (averageExtraDataCharge > 10) {
        recommendedDataPack = {
            price: 15,
            amount: 2
        };
    }

    return {
        type: RECEIVED_LAST_BILLS,
        lastBills: json,
        recommendedDataPack: recommendedDataPack
    }
}

export function receiveLastBillsError(httpStatus) {
    return {
        type: httpStatus === 404 ? RECEIVED_LAST_BILLS_EXPIRY : RECEIVED_LAST_BILLS_ERROR
    }
}

export function fetchLastBills(token, fetcher = client.fetchLastBills) {
    return (dispatch) => {
        dispatch(requestLastBills());
        fetcher(dispatch, receiveLastBills, receiveLastBillsError, token);
    }
}

