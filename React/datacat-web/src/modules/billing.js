import * as client from "../adapters/client";

const REQUEST_BILLING = 'REQUEST_BILLING';
const RECEIVED_BILLING = 'RECEIVED_BILLING';
const RECEIVED_BILLING_EXPIRY = 'RECEIVED_BILLING_EXPIRY';
const RECEIVED_BILLING_ERROR = 'RECEIVED_BILLING_ERROR';

export default function reducer(state = {}, action) {
    switch (action.type) {
        case REQUEST_BILLING:
            return Object.assign({}, state, {
                isFetching: true,
                isError: false,
                isExpired: false
            });
        case RECEIVED_BILLING:
            return Object.assign({}, state, {
                isFetching: false,
                isError: false,
                isExpired: false,
                usage: action.usage,
                allowance: action.allowance,
                start: action.start,
                end: action.end,
                updatedAt: new Date(action.updatedAt),
                nextBillingCycleUrl: action.nextBillingCycleUrl,
                previousBillingCycleUrl: action.previousBillingCycleUrl,
                baseBill: action.baseBill
            });
        case RECEIVED_BILLING_EXPIRY:
            return Object.assign({}, state, {
                isFetching: false,
                isExpired: true,
            });
        case RECEIVED_BILLING_ERROR:
            return Object.assign({}, state, {
                isFetching: false,
                isError: true,
            });
        default:
            return state
    }
}

export function requestBilling() {
    return {
        type: REQUEST_BILLING
    }
}

export function receiveBilling(json) {
    return {
        type: RECEIVED_BILLING,
        usage: json.usage,
        allowance: json.allowance,
        start: json.start,
        end: json.end,
        updatedAt: json.updatedAt,
        nextBillingCycleUrl: json.nextBillingCycleUrl,
        previousBillingCycleUrl: json.previousBillingCycleUrl,
        baseBill: json.baseBill
    }
}

export function receiveBillingError(httpStatus) {
    return {
        type: httpStatus === 404 ? RECEIVED_BILLING_EXPIRY : RECEIVED_BILLING_ERROR,
    }
}

export function fetchBilling(resourcePath, fetcher = client.fetchBilling) {
    return (dispatch) => {
        dispatch(requestBilling());
        fetcher(dispatch, receiveBilling, receiveBillingError, resourcePath);
    }
}
