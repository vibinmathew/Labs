import React from "react";
import {shallow, mount} from "enzyme";
import DataUsage from "./DataUsage";
import ConnectedCategorization from "../connectors/ConnectedCategorization";
import ConnectedSummary from "../connectors/ConnectedSummary"
import ConnectedUserInfo from "../connectors/ConnectedUserInfo"
import Header from "./Header"
import Spinner from "./Spinner"
import Error from "./Error"
import Expiry from "./Expiry"
import Button from "./Button"
import * as utils from "./utils"

describe('DataUsage', () => {
    let fetchBilling = jest.fn();
    let fetchUserInfo = jest.fn();
    let fetchDataCategorization = jest.fn();
    let fetchLastBills = jest.fn();
    utils.redirect = jest.fn();

    let props = {
        fetchBilling: fetchBilling,
        fetchUserInfo: fetchUserInfo,
        fetchDataCategorization: fetchDataCategorization,
        fetchLastBills: fetchLastBills,
        params: {
            token: '0412345678'
        },
        billing: {},
        user: {},
        dataCategorization: {},
        error: {
            status: ''
        },
        bills: {
        },
        navigation: {}
    };

    beforeEach(() => {
        fetchDataCategorization.mockClear();
        fetchBilling.mockClear();
        fetchUserInfo.mockClear();
        fetchLastBills.mockClear();
    });

    it('renders without crashing', () => {
        let wrapper = shallow(<DataUsage {...props}/>);
        let header = wrapper.find(Header);
        expect(header).toHaveLength(1);
        expect(header.props().title).toEqual("My Telstra data usage");
        expect(header.props().backPath).toEqual("/dashboard/0412345678");

        let userInfo = wrapper.find(ConnectedUserInfo);
        expect(userInfo).toHaveLength(1);

        let cat = wrapper.find(ConnectedCategorization);
        expect(cat).toHaveLength(1);

        expect(wrapper.find(ConnectedSummary)).toHaveLength(1);
    });

    describe("When fetching data", () => {
        it('renders loading screen', () => {
            let wrapper = shallow(<DataUsage {...props} billing={{isFetching: true}}/>);
            expect(wrapper.find(Spinner)).toHaveLength(1);
            expect(wrapper.find(Spinner).props().spinnerText).toEqual(["Hang tight!", "We are loading your data usage."]);
        });
    });

    describe("Expiry errors", () => {
        it('renders expiry screen when billing has expired error', () => {
            let wrapper = shallow(<DataUsage {...props} billing={{isExpired: true}}/>);
            expect(wrapper.find(Expiry)).toHaveLength(1);
        });

        it('renders expiry screen when dataCategorization has expired error', () => {
            let wrapper = shallow(<DataUsage {...props} dataCategorization={{isExpired: true}}/>);
            expect(wrapper.find(Expiry)).toHaveLength(1);
        });

        it('renders expiry screen when user has expired error', () => {
            let wrapper = shallow(<DataUsage {...props} billing={{isExpired: true}}/>);
            expect(wrapper.find(Expiry)).toHaveLength(1);
        });
    });

    describe("Default errors", () => {
        it('renders error screen when billing has default error', () => {
            let wrapper = shallow(<DataUsage {...props} billing={{isError: true}}/>);
            expect(wrapper.find(Error)).toHaveLength(1);
        });

        it('renders error screen when dataCategorization has default error', () => {
            let wrapper = shallow(<DataUsage {...props} dataCategorization={{isError: true}}/>);
            expect(wrapper.find(Error)).toHaveLength(1);
        });

        it('renders error screen when user has default error', () => {
            let wrapper = shallow(<DataUsage {...props} user={{isError: true}}/>);
            expect(wrapper.find(Error)).toHaveLength(1);
        });
    });

    describe("dispatchers", () => {
        it('dispatches fetchBilling', () => {
            shallow(<DataUsage {...props}/>);
            expect(fetchBilling).toBeCalledWith('0412345678');
        });

        it('dispatches fetchUserInfo', () => {
            shallow(<DataUsage {...props}/>);
            expect(fetchUserInfo).toBeCalledWith('0412345678');
        });

        it('dispatches fetchDataCategorization', () => {
            shallow(<DataUsage {...props}/>);
            expect(fetchDataCategorization).toBeCalledWith('0412345678');
        });

        it('dispatches fetchLastBills', () => {
            shallow(<DataUsage {...props}/>);
            expect(fetchLastBills).toBeCalledWith('0412345678');
        });

        it('should not dispatch fetchLastBills call if lastBills already present', () => {

            let lastBills = [
                {date: '2017-02-15', bill: 150},
                {date: '2017-01-15', bill: 140},
                {date: '2016-12-15', bill: 160}
            ];

            shallow(<DataUsage {...props} bills={{lastBills:lastBills, recommendedDataPack:0}}/>);
            expect(fetchLastBills).not.toHaveBeenCalled();
        });

        it('dispatches api calls when the index has changed', () => {
            let wrapper = shallow(<DataUsage {...props} />);
            wrapper.setProps({
                params: {
                    token: '0412345678',
                    index: '1'
                }
            });

            expect(fetchDataCategorization).toBeCalledWith('0412345678/1');
            expect(fetchBilling).toBeCalledWith('0412345678/1');
            expect(fetchUserInfo).toBeCalledWith('0412345678');
        });

        it('should not make api calls when the index has not changed', () => {
            let wrapper = shallow(<DataUsage {...props} params={{
                token: '0412345678',
                index: '1'
            }}/>);
            wrapper.setProps({
                params: {
                    token: '0412345678',
                    index: '1'
                }
            });

            expect(fetchDataCategorization).toBeCalledWith('0412345678/1');
            expect(fetchBilling).toBeCalledWith('0412345678/1');
            expect(fetchUserInfo).toBeCalledWith('0412345678');
            expect(fetchDataCategorization).toHaveBeenCalledTimes(1);
            expect(fetchBilling).toHaveBeenCalledTimes(1);
            expect(fetchUserInfo).toHaveBeenCalledTimes(1);
        });
    });

    describe("DataPack button", () => {
        it("should render live chat button after fetching bills if extra data charge is not present", () => {
            let wrapper = shallow(<DataUsage {...props} bills={{lastBills:[], recommendedDataPack:0}} />);
            let button = wrapper.find(Button);
            expect(button).toHaveLength(1);
            expect(button.props().buttonText).toEqual("Telstra 24x7 Live Chat");
            expect(wrapper.find(Button).props().clickAction).toEqual(wrapper.instance().redirectToChat);
            expect(wrapper.find('.live-chat-container').text()).toContain('Still have questions?Chat with a Customer Service Agent about your service');
            wrapper.instance().redirectToChat();
            expect(utils.redirect).toBeCalledWith('https://www.telstra.com.au/chatnow/landing?pageId=https%3a%2f%2flivechat.telstra.com%2fTB%3aBusiness%3aDefault');
        })

        it("redirects to data pack page if extra data charge is present", () => {
            let wrapper = shallow(<DataUsage {...props} bills={{
                lastBills: [{extraDataCharge: 10}],
                recommendedDataPack: {
                    price: 15,
                    amount: 2
                }
            }}/>);
            let button = wrapper.find(Button);
            expect(button).toHaveLength(1);

            expect(button.props().buttonText).toEqual("Reduce my future bills");
            expect(button.props().clickAction).toEqual(wrapper.instance().redirectToDataPack);
        })
        it("should not render any button if lastBills is undefined", () => {
            let wrapper = shallow(<DataUsage {...props} bills={{lastBills: undefined}}/>);
            let button = wrapper.find(Button);
            expect(button).toHaveLength(0);
        })
    });

    describe("redirectToDataPack", () => {
        it("redirects to data pack page", () => {
            utils.route = jest.fn();

            let wrapper = shallow(<DataUsage {...props}/>);
            wrapper.instance().redirectToDataPack();

            expect(utils.route).toBeCalledWith('/datapack/0412345678');
        })
    })
});

