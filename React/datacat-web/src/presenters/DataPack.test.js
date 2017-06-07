import React from "react";
import {shallow, mount} from "enzyme";
import DataPack from "./DataPack";
import Header from "./Header";
import LastBills from "./LastBills";
import Expiry from "./Expiry";
import Error from "./Error";
import Recommendation from "./Recommendation";
import Button from "./Button"
import Spinner from "./Spinner"
import * as utils from "./utils";

describe('DataPack', () => {

    let fetchLastBills = jest.fn();
    let fetchBilling = jest.fn();
    let saveCurrentPath = jest.fn();
    utils.route = jest.fn();

    let lastBills = [
        {date: '2017-02-15', bill: 150},
        {date: '2017-01-15', bill: 140},
        {date: '2016-12-15', bill: 160}
    ];

    let props = {
        fetchLastBills: fetchLastBills,
        fetchBilling: fetchBilling,
        params: {
            token: 'token123'
        },
        billing: {
            baseBill: 100
        },
        bills: {
            lastBills: lastBills,
            recommendedDataPack: {
                price: 15,
                amount: 2
            }
        },
        navigation: {},
        saveCurrentPath: saveCurrentPath
    };

    beforeEach(() => {
        fetchLastBills.mockClear();
    });

    it('renders without crashing', () => {
        let wrapper = shallow(<DataPack {...props}/>);
        expect(wrapper.find(Header)).toHaveLength(1);
        expect(wrapper.find(Header).props().title).toEqual("Reduce my bill");
        expect(wrapper.find(Header).props().backPath).toEqual("/dashboard/token123");
        expect(wrapper.find(LastBills)).toHaveLength(1);
        expect(wrapper.find(LastBills).props().lastBills).toEqual(lastBills);
        expect(wrapper.find(Recommendation)).toHaveLength(1);
        expect(wrapper.find(Recommendation).props().bills.lastBills).toEqual(lastBills);
        expect(wrapper.find(Recommendation).props().bills.recommendedDataPack).toEqual({
            price: 15,
            amount: 2
        });
        expect(wrapper.find(Recommendation).props().billing.baseBill).toEqual(100);
        let button = wrapper.find(Button);
        expect(button).toHaveLength(1);

        expect(button.props().buttonText).toEqual("Learn more about data packs");
        expect(button.props().clickAction).toEqual(wrapper.instance().redirectToDataPacksMoreInfo);
    });

    it('renders before being able to fetch', () => {
        let wrapper = shallow(<DataPack {...props} bills={{}}/>);
        expect(wrapper.find(Header)).toHaveLength(1);
        expect(wrapper.find(Header).props().title).toEqual("Reduce my bill");
        expect(wrapper.find(LastBills)).toHaveLength(0);
        expect(wrapper.find(Recommendation)).toHaveLength(0);
    });

    it('should not dispatch fetchLastBills call if lastBills already present', () => {
        shallow(<DataPack {...props}/>);
        expect(fetchLastBills).not.toHaveBeenCalled();
    });

    it('should dispatch fetchLastBills action if bills is not set', () => {
        shallow(<DataPack {...props} bills={{}}/>);
        expect(fetchLastBills).toBeCalledWith('token123');
    });

    it('should dispatch fetchBilling action if billing is not set', () => {
        shallow(<DataPack {...props} billing={{}}/>);
        expect(fetchBilling).toBeCalledWith('token123');
    });

    describe("Expiry errors", () => {
        it('renders expiry screen when fetching last bills has expired error', () => {
            let wrapper = shallow(<DataPack {...props} bills={{isExpired: true}}/>);
            expect(wrapper.find(Expiry)).toHaveLength(1);
        });
        it('renders expiry screen when fetching billing has expired error', () => {
            let wrapper = shallow(<DataPack {...props} billing={{isExpired: true}}/>);
            expect(wrapper.find(Expiry)).toHaveLength(1);
        });
    });

    describe("Default errors", () => {
        it('renders error screen when fetching last bills has default error', () => {
            let wrapper = shallow(<DataPack {...props} bills={{isError: true}}/>);
            expect(wrapper.find(Error)).toHaveLength(1);
        });
        it('renders error screen when fetching billing has default error', () => {
            let wrapper = shallow(<DataPack {...props} billing={{isError: true}}/>);
            expect(wrapper.find(Error)).toHaveLength(1);
        });
    });

    describe("When fetching data", () => {
        it('renders loading screen', () => {
            let wrapper = shallow(<DataPack {...props} billing={{isFetching: true}}/>);
            expect(wrapper.find(Spinner)).toHaveLength(1);
            expect(wrapper.find(Spinner).props().spinnerText).toEqual(["Hang tight!", "We are loading your data recommendations."]);
        });
    });
});

