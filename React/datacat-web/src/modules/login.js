import * as client from "../adapters/client";

const REQUEST_SEND_SMS = 'REQUEST_SEND_SMS';
const RECEIVED_SEND_SMS = 'RECEIVED_SEND_SMS';
const RECEIVED_SEND_SMS_ERROR = 'RECEIVED_SEND_SMS_ERROR';
const RECEIVED_SEND_SMS_NOT_FOUND = 'RECEIVED_SEND_SMS_NOT_FOUND';
const RESET_SENT_STATUS = 'RESET_SENT_STATUS';

export default function reducer(state = {}, action) {
    switch (action.type) {
        case REQUEST_SEND_SMS:
            return Object.assign({}, state, {
                isFetching: true,
                isError: false,
                isNotFound: false,
            });
        case RECEIVED_SEND_SMS:
            return Object.assign({}, state, {
                isFetching: false,
                sent: true,
            });
        case RECEIVED_SEND_SMS_ERROR:
            return Object.assign({}, state, {
                isFetching: false,
                isError: true
            });
        case RECEIVED_SEND_SMS_NOT_FOUND:
            return Object.assign({}, state, {
                isFetching: false,
                isNotFound: true
            });
        case RESET_SENT_STATUS:
            return Object.assign({}, state, {
                sent: false,
                isNotFound: false
            });
        default:
            return state
    }
}

export function requestSendSMS() {
    return {
        type: REQUEST_SEND_SMS
    }
}

export function receiveSendSMS() {
    return {
        type: RECEIVED_SEND_SMS,
    }
}
export function resetSentStatus() {
    return {
        type: RESET_SENT_STATUS,
    }
}

export function receiveSendSMSError(httpStatus) {
    return {
        type: httpStatus === 404 ? RECEIVED_SEND_SMS_NOT_FOUND : RECEIVED_SEND_SMS_ERROR,
    }
}

export function sendSMS(number, fetcher = client.sendSMS) {
    return (dispatch) => {
        dispatch(requestSendSMS());
        fetcher(dispatch, receiveSendSMS, receiveSendSMSError, number);
    }
}

