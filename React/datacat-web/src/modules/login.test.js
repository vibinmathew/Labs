import reducer, {requestSendSMS, receiveSendSMS, sendSMS, receiveSendSMSError, resetSentStatus} from "./login";


describe('Login', () => {
    describe('reducer', () => {
        it('should return the initial account state', () => {
            expect(reducer(undefined, {})).toEqual({});
        });

        it('should handle REQUEST_SEND_SMS', () => {
            expect(reducer(undefined, requestSendSMS())).toEqual({
                isFetching: true,
                isError: false,
                isNotFound: false,
            });
        });

        it('should handle RECEIVED_SEND_SMS', () => {
            expect(reducer({isFetching: true}, receiveSendSMS())).toEqual({
                isFetching: false,
                sent: true
            });
        });

        it('should handle RECEIVED_SEND_SMS_NOT_FOUND', () => {
            expect(reducer({}, {
                type: 'RECEIVED_SEND_SMS_NOT_FOUND',
            })).toEqual({
                isFetching: false,
                isNotFound: true,
            });
        });

        it('should handle RECEIVED_SEND_SMS_ERROR', () => {
            expect(reducer({}, {
                type: 'RECEIVED_SEND_SMS_ERROR',
            })).toEqual({
                isFetching: false,
                isError: true,
            });
        });

        it('should reset errors when fetching again', () => {
            expect(reducer({
                isFetching: false,
                isError: true
            }, {
                type: 'REQUEST_SEND_SMS',
            })).toEqual({
                isFetching: true,
                isError: false,
                isNotFound: false
            });
        });

        it('should reset sent status when RESET_SENT_STATUS is received', () => {
            expect(reducer({
                sent: true,
            }, {
                type: 'RESET_SENT_STATUS',
            })).toEqual({
                sent: false,
                isNotFound: false
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

        describe('sendSMS', () => {
            it('calls the given fetch action', () => {
                const fetchAction = sendSMS('0265498721', fetcher);
                fetchAction(dispatch);
                expect(dispatch).toHaveBeenCalledWith(requestSendSMS());
                expect(fetcher).toHaveBeenCalledWith(dispatch, receiveSendSMS, receiveSendSMSError, '0265498721');
            });
        });

        describe('receiveSendSMSError', () => {
            it('returns RECEIVED_SEND_SMS_ERROR when receiving a default error', () => {
                const action = receiveSendSMSError(400);
                expect(action.type).toEqual('RECEIVED_SEND_SMS_ERROR')
            });

            it('returns RECEIVED_SEND_SMS_NOT_FOUND when receiving a resource not found error', () => {
                const action = receiveSendSMSError(404);
                expect(action.type).toEqual('RECEIVED_SEND_SMS_NOT_FOUND')
            });
        });

        describe('sendSMS', () => {
            it("returns RESET_SENT_STATUS when resetSentStatus is called", () => {
                const action = resetSentStatus();
                expect(action.type).toEqual('RESET_SENT_STATUS')
            });
        });
    });
});
