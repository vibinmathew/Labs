import * as apiFetch from "./apiFetch";

export function fetchUserInfo(dispatch, receiveUserInfo, receiveUserInfoError, token, fetcher = apiFetch) {
    fetcher.fetchFromApi(dispatch, '/user/' + token, receiveUserInfo, receiveUserInfoError);
}
export function fetchDataCategorization(dispatch, receiveDataCategorization, receiveDataCategorizationError, resourcePath, fetcher = apiFetch) {
    fetcher.fetchFromApi(dispatch, '/categorization/' + resourcePath, receiveDataCategorization, receiveDataCategorizationError);
}
export function fetchBilling(dispatch, receiveBilling, receiveBillingError, resourcePath, fetcher = apiFetch) {
    fetcher.fetchFromApi(dispatch, '/billing/' + resourcePath, receiveBilling, receiveBillingError);
}
export function fetchLastBills(dispatch, receiveLastBills, receiveLastBillsError, token, fetcher = apiFetch) {
    fetcher.fetchFromApi(dispatch, '/lastbills/' + token, receiveLastBills, receiveLastBillsError);
}
export function sendSMS(dispatch, smsSent, smsSentError, msisdn, fetcher = apiFetch) {
    fetcher.fetchFromApi(dispatch, '/sms/' + msisdn, smsSent, smsSentError);
}

