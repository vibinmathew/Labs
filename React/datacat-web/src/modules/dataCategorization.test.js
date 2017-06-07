import reducer, {
    requestDataCategorization,
    receiveDataCategorization,
    fetchDataCategorization,
    receiveDataCategorizationError,
    selectCategory,
} from "./dataCategorization";


describe('Data Categorization', () => {
    describe('reducer', () => {
        it('should return the initial state', () => {
            expect(reducer(undefined, {})).toEqual({});
        });

        it('should handle REQUEST_DATA_CAT', () => {
            expect(reducer(undefined, requestDataCategorization())).toEqual({
                isFetching: true,
                isError: false,
                isExpired: false
            });
        });

        it('should handle RECEIVED_DATA_CAT', () => {
            const json = {
                overall: [{category: "categoryName"}],
                dailyUsage: 'Daily Usage array'
            };

            expect(reducer({isFetching: true}, receiveDataCategorization(json))).toEqual({
                isFetching: false,
                isError: false,
                isExpired: false,
                overall: [{category: "categoryName"}],
                dailyUsage: 'Daily Usage array',
                selectedCategory: "categoryName"
            });
        });
        
        it('should handle SELECT_CATEGORY', () => {
            expect(reducer({selectedCategory: 'Streaming'}, selectCategory('Gaming'))).toEqual({
                selectedCategory: 'Gaming'
            });
        });

        it('should handle RECEIVED_DATA_CAT_ERROR', () => {
            expect(reducer({}, {
                type: 'RECEIVED_DATA_CAT_ERROR',
            })).toEqual({
                isFetching: false,
                isError: true,
            });
        });

        it('should handle RECEIVED_DATA_CAT_EXPIRY', () => {
            expect(reducer({}, {
                type: 'RECEIVED_DATA_CAT_EXPIRY',
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
                type: 'REQUEST_DATA_CAT',
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

        describe('fetchDataCategorization', () => {
            it('calls the given fetch action', () => {
                const fetchAction = fetchDataCategorization('0265498721', fetcher);
                fetchAction(dispatch);
                expect(dispatch).toHaveBeenCalledWith(requestDataCategorization());
                expect(fetcher).toHaveBeenCalledWith(dispatch, receiveDataCategorization, receiveDataCategorizationError, '0265498721');
            });
        });

        describe('receiveDataCategorization', () => {
            it('extracts relevant fields from json', () => {
                const action = receiveDataCategorization({
                    overall: 'OverallObject',
                    dailyUsage: 'daily usage as array'
                });

                expect(action.type).toEqual('RECEIVED_DATA_CAT');
                expect(action.overall).toEqual('OverallObject');
                expect(action.dailyUsage).toEqual('daily usage as array');
            });
        });

        describe('receiveDataCategorization', () => {
            it('extracts relevant fields from json', () => {
                const action = selectCategory('Gaming');

                expect(action.type).toEqual('SELECT_CATEGORY');
                expect(action.selectedCategory).toEqual('Gaming');
            });
        });

        describe('receiveDataCategorizationError', () => {
            it('returns RECEIVED_DATA_CAT_ERROR when receiving a default error', () => {
                const action = receiveDataCategorizationError(400);
                expect(action.type).toEqual('RECEIVED_DATA_CAT_ERROR')
            });

            it('returns RECEIVED_DATA_CAT_EXPIRY when receiving a resource not found error', () => {
                const action = receiveDataCategorizationError(404);
                expect(action.type).toEqual('RECEIVED_DATA_CAT_EXPIRY')
            });
        });
    });
});
