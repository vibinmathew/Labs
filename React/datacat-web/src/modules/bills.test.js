import reducer, {fetchLastBills, requestLastBills, receiveLastBills, receiveLastBillsError} from "./bills";


describe('Bills', () => {

    describe('action creators', () => {
        let fetcher;
        let dispatch;

        beforeEach(() => {
            fetcher = jest.fn();
            dispatch = jest.fn();
        });

        describe('fetchLastBills', () => {
            it('calls the given fetch action', () => {
                const fetchAction = fetchLastBills('token123', fetcher);
                fetchAction(dispatch);
                expect(dispatch).toHaveBeenCalledWith(requestLastBills());
                expect(fetcher).toHaveBeenCalledWith(dispatch, receiveLastBills, receiveLastBillsError, 'token123');
            });
        });

        describe('requestLastBills', () => {
            it('returns REQUEST_LAST_BILLS action', () => {
                const action = requestLastBills();
                expect(action.type).toEqual('REQUEST_LAST_BILLS')
            });
        });

        describe('receiveLastBills', () => {
            it('returns RECEIVED_LAST_BILLS action with $0 data pack if no excess', () => {
                const action = receiveLastBills([{date: '2017-02-15', bill: 150, baseBill: 100, extraDataCharge: 0}, {date: '2017-01-15', bill: 140, baseBill: 100, extraDataCharge: 0}]);
                expect(action.type).toEqual('RECEIVED_LAST_BILLS');
                expect(action.lastBills).toEqual([{date: '2017-02-15', bill: 150, baseBill: 100, extraDataCharge: 0}, {date: '2017-01-15', bill: 140, baseBill: 100, extraDataCharge: 0}])
                expect(action.recommendedDataPack).toEqual({});
            });
        });

        describe('receiveLastBills', () => {
            it('returns RECEIVED_LAST_BILLS action with $15 data pack if average excess between $10.01 - $30', () => {
                const action = receiveLastBills([{date: '2017-02-15', bill: 150, baseBill: 100, extraDataCharge: 10}, {date: '2017-01-15', bill: 140, baseBill: 100, extraDataCharge: 15}]);
                expect(action.type).toEqual('RECEIVED_LAST_BILLS');
                expect(action.lastBills).toEqual([{date: '2017-02-15', bill: 150, baseBill: 100, extraDataCharge: 10}, {date: '2017-01-15', bill: 140, baseBill: 100, extraDataCharge: 15}])
                expect(action.recommendedDataPack).toEqual({
                    price: 15,
                    amount: 2
                });
            });
        });

        describe('receiveLastBills', () => {
            it('returns RECEIVED_LAST_BILLS action with $35 data pack if average excess between $30.01 - $50', () => {
                const action = receiveLastBills([{date: '2017-02-15', bill: 150, baseBill: 100, extraDataCharge: 35}, {date: '2017-01-15', bill: 140, baseBill: 100, extraDataCharge: 45}]);
                expect(action.type).toEqual('RECEIVED_LAST_BILLS');
                expect(action.lastBills).toEqual([{date: '2017-02-15', bill: 150, baseBill: 100, extraDataCharge: 35}, {date: '2017-01-15', bill: 140, baseBill: 100, extraDataCharge: 45}])
                expect(action.recommendedDataPack).toEqual({
                    price: 35,
                    amount: 5
                });
            });
        });

        describe('receiveLastBills', () => {
            it('returns RECEIVED_LAST_BILLS action with $55 data pack if average excess above $50.01', () => {
                const action = receiveLastBills([{date: '2017-02-15', bill: 150, baseBill: 100, extraDataCharge: 60}, {date: '2017-01-15', bill: 140, baseBill: 100, extraDataCharge: 50}]);
                expect(action.type).toEqual('RECEIVED_LAST_BILLS');
                expect(action.lastBills).toEqual([{date: '2017-02-15', bill: 150, baseBill: 100, extraDataCharge: 60}, {date: '2017-01-15', bill: 140, baseBill: 100, extraDataCharge: 50}])
                expect(action.recommendedDataPack).toEqual({
                    price: 55,
                    amount: 8
                });
            });
        });

        describe('receiveLastBillsError', () => {
            it('returns RECEIVED_LAST_BILLS_ERROR action', () => {
                const action = receiveLastBillsError(500);
                expect(action.type).toEqual('RECEIVED_LAST_BILLS_ERROR')
            });

            it('returns RECEIVED_LAST_BILLS_EXPIRY action', () => {
                const action = receiveLastBillsError(404);
                expect(action.type).toEqual('RECEIVED_LAST_BILLS_EXPIRY')
            });
        });
    });

    describe('reducer', () => {
        it('should return the initial state', () => {
            expect(reducer(undefined, {})).toEqual({});
        });

        it('should handle REQUEST_LAST_BILLS', () => {
            expect(reducer({}, {type: 'REQUEST_LAST_BILLS'})).toEqual({
                isFetching: true,
                isError: false,
                isExpired: false
            });
        });

        it('should handle RECEIVED_LAST_BILLS', () => {
            expect(reducer({
                isFetching: true
            }, {
                type: 'RECEIVED_LAST_BILLS',
                lastBills: [
                    {date: '2017-02-15', bill: 150},
                    {date: '2017-01-15', bill: 140},
                    {date: '2016-12-15', bill: 160}
                ],
                recommendedDataPack : 15
            })).toEqual({
                isFetching: false,
                lastBills: [
                    {date: '2017-02-15', bill: 150},
                    {date: '2017-01-15', bill: 140},
                    {date: '2016-12-15', bill: 160}
                ],
                recommendedDataPack: 15
            });
        });

        it('should handle RECEIVED_LAST_BILLS_ERROR', () => {
            expect(reducer({}, {
                type: 'RECEIVED_LAST_BILLS_ERROR',
            })).toEqual({
                isFetching: false,
                isError: true,
            });
        });

        it('should handle RECEIVED_LAST_BILLS_EXPIRY', () => {
            expect(reducer({}, {
                type: 'RECEIVED_LAST_BILLS_EXPIRY',
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
                type: 'REQUEST_LAST_BILLS',
            })).toEqual({
                isFetching: true,
                isError: false,
                isExpired: false
            });
        });
    });
});
