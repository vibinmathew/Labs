import * as client from "../adapters/client";

const REQUEST_DATA_CAT = 'REQUEST_DATA_CAT';
const RECEIVED_DATA_CAT = 'RECEIVED_DATA_CAT';
const RECEIVED_DATA_CAT_ERROR = 'RECEIVED_DATA_CAT_ERROR';
const RECEIVED_DATA_CAT_EXPIRY = 'RECEIVED_DATA_CAT_EXPIRY';
const SELECT_CATEGORY = 'SELECT_CATEGORY';

export default function reducer(state = {}, action) {
    switch (action.type) {
        case REQUEST_DATA_CAT:
            return Object.assign({}, state, {
                isFetching: true,
                isError: false,
                isExpired: false,
            });
        case RECEIVED_DATA_CAT:
            return Object.assign({}, state, {
                isFetching: false,
                isError: false,
                isExpired: false,
                overall: action.overall,
                dailyUsage: action.dailyUsage,
                selectedCategory: action.overall[0].category
            });
        case RECEIVED_DATA_CAT_ERROR:
            return Object.assign({}, state, {
                isFetching: false,
                isError: true,
            });
        case RECEIVED_DATA_CAT_EXPIRY:
            return Object.assign({}, state, {
                isFetching: false,
                isExpired: true,
            });
        case SELECT_CATEGORY:
            return Object.assign({}, state, {
                selectedCategory: action.selectedCategory,
            });
        default:
            return state
    }
}

export function requestDataCategorization() {
    return {
        type: REQUEST_DATA_CAT
    }
}

export function receiveDataCategorization(json) {
    return {
        type: RECEIVED_DATA_CAT,
        overall: json.overall,
        dailyUsage: json.dailyUsage
    }
}

export function receiveDataCategorizationError(httpStatus) {
    return {
        type: httpStatus === 404 ? RECEIVED_DATA_CAT_EXPIRY : RECEIVED_DATA_CAT_ERROR,
    }
}

export function selectCategory(selectedCategory) {
    return {
        type: SELECT_CATEGORY,
        selectedCategory: selectedCategory
    }
}

export function fetchDataCategorization(resourcePath, fetcher = client.fetchDataCategorization) {
    return (dispatch) => {
        dispatch(requestDataCategorization());
        fetcher(dispatch, receiveDataCategorization, receiveDataCategorizationError, resourcePath);
    }
}
