import reducer, {requestBilling, receiveBilling, fetchBilling, receiveBillingError} from "./billing";


describe('Billing', () => {

    describe('reducer', () => {
        it('should return the initial billing state', () => {
            expect(reducer(undefined, {})).toEqual({});
        });

        it('should handle REQUEST_BILLING', () => {
            expect(reducer(undefined, requestBilling())).toEqual({
                isFetching: true,
                isError: false,
                isExpired: false
            });
        });

        it('should handle RECEIVED_BILLING', () => {
            const json = {
                usage: 7700000,
                allowance: 6000000,
                start: '2017-02-15',
                end: '2017-03-15',
                updatedAt: '2017-03-17T08:50Z',
                nextBillingCycleUrl: 'token123/1',
                previousBillingCycleUrl: 'token123/-1',
                baseBill: 100
            };

            expect(reducer({isFetching: true}, receiveBilling(json))).toEqual({
                isFetching: false,
                isError: false,
                isExpired: false,
                usage: 7700000,
                allowance: 6000000,
                start: '2017-02-15',
                end: '2017-03-15',
                updatedAt: new Date('2017-03-17T08:50Z'),
                nextBillingCycleUrl: 'token123/1',
                previousBillingCycleUrl: 'token123/-1',
                baseBill: 100
            });
        });

        it('should handle RECEIVED_BILLING_ERROR', () => {
            expect(reducer({}, {
                type: 'RECEIVED_BILLING_ERROR',
            })).toEqual({
                isFetching: false,
                isError: true,
            });
        });

        it('should handle RECEIVED_BILLING_EXPIRY', () => {
            expect(reducer({}, {
                type: 'RECEIVED_BILLING_EXPIRY',
            })).toEqual({
                isFetching: false,
                isExpired: true,
            });
        });

        it('should reset errors when fetching again', () => {
            expect(reducer({
                isFetching: false,
                isError: true
            }, {
                type: 'REQUEST_BILLING',
            })).toEqual({
                isFetching: true,
                isError: false,
                isExpired: false
            });
        });
    });

    describe('action creators', () => {
        let fetcher;
        let dispatch;

        beforeEach(() => {
            fetcher = jest.fn();
            dispatch = jest.fn();
        });

        describe('fetchBilling', () => {
            it('calls the given fetch action', () => {
                const fetchAction = fetchBilling('0265498721', fetcher);
                fetchAction(dispatch);
                expect(dispatch).toHaveBeenCalledWith(requestBilling());
                expect(fetcher).toHaveBeenCalledWith(dispatch, receiveBilling, receiveBillingError, '0265498721');
            });
        });

        describe('receiveBilling', () => {
            it('extracts relevant fields from json', () => {
                const action = receiveBilling({
                    usage: 1.1,
                    allowance: 900000,
                    start: '2017-02-15',
                    end: '2017-03-15',
                    updatedAt: '2017-03-17T08:50Z',
                    nextBillingCycleUrl: 'token123/1',
                    previousBillingCycleUrl: 'token123/-1',
                    baseBill: 100
                });

                expect(action.type).toEqual('RECEIVED_BILLING');
                expect(action.usage).toEqual(1.1);
                expect(action.allowance).toEqual(900000);
                expect(action.start).toEqual('2017-02-15');
                expect(action.end).toEqual('2017-03-15');
                expect(action.updatedAt).toEqual('2017-03-17T08:50Z');
                expect(action.nextBillingCycleUrl).toEqual('token123/1');
                expect(action.previousBillingCycleUrl).toEqual('token123/-1');
                expect(action.baseBill).toEqual(100);
            });
        });

        describe('receiveBillingError', () => {
            it('returns RECEIVED_BILLING_ERROR when receiving a default error', () => {
                const action = receiveBillingError(400);
                expect(action.type).toEqual('RECEIVED_BILLING_ERROR')
            });

            it('returns RECEIVED_BILLING_EXPIRY when receiving a resource not found error', () => {
                const action = receiveBillingError(404);
                expect(action.type).toEqual('RECEIVED_BILLING_EXPIRY')
            });
        });
    });
});
