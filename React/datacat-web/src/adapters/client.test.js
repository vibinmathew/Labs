import {fetchUserInfo, fetchDataCategorization, fetchBilling, sendSMS, fetchLastBills} from "./client";


describe('fetchUserInfo', () => {
    it('should fetch user info from the API', () => {
        let fetcher = {
            fetchFromApi: jest.fn()
        };

        fetchUserInfo('dispatch', 'receiveUserInfo', 'receiveUserInfoError', '0265498732', fetcher);

        expect(fetcher.fetchFromApi).toHaveBeenCalledWith('dispatch', '/user/0265498732', 'receiveUserInfo', 'receiveUserInfoError');

    });
});

describe('fetchDataCategorization', () => {
    it('should fetch categorization from the API', () => {
        let fetcher = {
            fetchFromApi: jest.fn()
        };

        fetchDataCategorization('dispatch', 'receiveDataCategorization', 'receiveDataCategorizationError', '0265498732', fetcher);

        expect(fetcher.fetchFromApi).toHaveBeenCalledWith('dispatch', '/categorization/0265498732', 'receiveDataCategorization', 'receiveDataCategorizationError');

    });
});


describe('fetchBilling', () => {
    it('should fetch billing from the API', () => {
        let fetcher = {
            fetchFromApi: jest.fn()
        };

        fetchBilling('dispatch', 'receiveBilling', 'receiveBillingError', '0265498732', fetcher);

        expect(fetcher.fetchFromApi).toHaveBeenCalledWith('dispatch', '/billing/0265498732', 'receiveBilling', 'receiveBillingError');

    });
})

describe('fetchLastBills', () => {
    it('should fetch LastBills from the API', () => {
        let fetcher = {
            fetchFromApi: jest.fn()
        };

        fetchLastBills('dispatch', 'receiveLastBills', 'receiveLastBillsError', 'token123', fetcher);

        expect(fetcher.fetchFromApi).toHaveBeenCalledWith('dispatch', '/lastbills/token123', 'receiveLastBills', 'receiveLastBillsError');

    });
})

describe('sendSMS', () => {
    it('should call the SMS API', () => {
        let fetcher = {
            fetchFromApi: jest.fn()
        };

        sendSMS('dispatch', 'smsSent', 'smsSentError', '0265498732', fetcher);

        expect(fetcher.fetchFromApi).toHaveBeenCalledWith('dispatch', '/sms/0265498732', 'smsSent', 'smsSentError');

    });
});
