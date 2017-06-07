import reducer, {requestUserInfo, receiveUserInfo, fetchUserInfo, receiveUserInfoError} from "./user";


describe('User info', () => {
    describe('reducer', () => {
        it('should return the initial account state', () => {
            expect(reducer(undefined, {})).toEqual({});
        });

        it('should handle REQUEST_USER_INFO', () => {
            expect(reducer(undefined, requestUserInfo())).toEqual({
                isFetching: true,
                isError: false,
                isExpired: false,
            });
        });

        it('should handle RECEIVED_USER_INFO', () => {
            const json = {
                msisdn: '0487654321'
            };

            expect(reducer({isFetching: true}, receiveUserInfo(json))).toEqual({
                isFetching: false,
                msisdn: '0487654321'
            });
        });

        it('should handle RECEIVED_USER_INFO_ERROR', () => {
            expect(reducer({}, {
                type: 'RECEIVED_USER_INFO_ERROR',
            })).toEqual({
                isFetching: false,
                isError: true,
            });
        });

        it('should handle RECEIVED_USER_INFO_EXPIRY', () => {
            expect(reducer({}, {
                type: 'RECEIVED_USER_INFO_EXPIRY',
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
                type: 'REQUEST_USER_INFO',
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

        describe('fetchUserInfo', () => {
            it('calls the given fetch action', () => {
                const fetchAction = fetchUserInfo('0265498721', fetcher);
                fetchAction(dispatch);
                expect(dispatch).toHaveBeenCalledWith(requestUserInfo());
                expect(fetcher).toHaveBeenCalledWith(dispatch, receiveUserInfo, receiveUserInfoError, '0265498721');
            });
        });

        describe('receiveUserInfo', () => {
            it('extracts relevant fields from json', () => {
                const action = receiveUserInfo({
                    msisdn: '0465412387'
                });

                expect(action.type).toEqual('RECEIVED_USER_INFO');
                expect(action.msisdn).toEqual('0465412387');
            });
        });

        describe('receiveUserInfoError', () => {
            it('returns RECEIVED_USER_INFO_ERROR when receiving a default error', () => {
                const action = receiveUserInfoError(400);
                expect(action.type).toEqual('RECEIVED_USER_INFO_ERROR')
            });

            it('returns RECEIVED_USER_INFO_EXPIRY when receiving a resource not found error', () => {
                const action = receiveUserInfoError(404);
                expect(action.type).toEqual('RECEIVED_USER_INFO_EXPIRY')
            });
        });
    });
});
