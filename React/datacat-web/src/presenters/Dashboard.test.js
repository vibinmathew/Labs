import React from "react";
import {shallow, mount} from "enzyme";
import Dashboard from "./Dashboard";
import * as utils from "./utils";
import Button from './Button'

describe('Dashboard', () => {

    let saveCurrentPath = jest.fn();
    let fetchLastBills = jest.fn();
    utils.redirect = jest.fn();
    let props = {
        params: {
            token: '0412345678'
        },
        bills: {
        },
        navigation: {},
        saveCurrentPath: saveCurrentPath,
        fetchLastBills: fetchLastBills
    };

    beforeEach(() => {
        fetchLastBills.mockClear();
    });

    it('renders without crashing', () => {
        let wrapper = shallow(<Dashboard {...props}/>);
        expect(wrapper.find(".dashboard-help-text").text()).toEqual("What would you like to do?");
        expect(wrapper.find(".datausage .help-text").text()).toEqual("I want to understand how  I'm using my data");
        expect(wrapper.find(".datausage svg")).toHaveLength(1);

        let buttons = wrapper.find(".datausage").find(Button);
        expect(buttons).toHaveLength(1);
        expect(buttons.nodes[0].props.buttonText).toEqual("View my data usage");

        expect(buttons.nodes[0].props.clickAction).toEqual(wrapper.instance().redirectToDataUsage);
    });

    describe("dispatchers", () => {
        it('fetchLastBills', () => {
            shallow(<Dashboard {...props}/>);
            expect(fetchLastBills).toBeCalledWith('0412345678');
        });

        it('should not dispatch fetchLastBills call if lastBills already present', () => {

            let lastBills = [
                {date: '2017-02-15', bill: 150},
                {date: '2017-01-15', bill: 140},
                {date: '2016-12-15', bill: 160}
            ];

            shallow(<Dashboard {...props} bills={{lastBills:lastBills, recommendedDataPack:0}}/>);
            expect(fetchLastBills).not.toHaveBeenCalled();
        });
    });

    describe("DataPack button", () => {
        it("redirects to data pack page if recommendedDataPack is present", () => {
            let wrapper = shallow(<Dashboard {...props} bills={{
                lastBills: [{extraDataCharge: 10}],
                recommendedDataPack: {
                    price: 15,
                    amount: 2
                }
            }}/>);
            let button = wrapper.find(".datapack").find(Button);
            expect(button).toHaveLength(1);

            expect(wrapper.find(".datapack .help-text").text()).toEqual("I often exceed my data and I'd like  to reduce my future bills");
            expect(wrapper.find(".datapack svg")).toHaveLength(1);
            expect(button.props().buttonText).toEqual("Reduce my future bills");
            expect(button.props().clickAction).toEqual(wrapper.instance().redirectToDataPack);
        });
        it("should not render DataPack button if recommendedDataPack is not present", () => {
            let wrapper = shallow(<Dashboard {...props} bills={{
                lastBills: [{extraDataCharge: 10}],
                recommendedDataPack: {}
            }}/>);
            let button = wrapper.find(".datapack").find(Button);
            expect(button).toHaveLength(0);
        });
        it("should not render DataPack button if lastBills is undefined", () => {
            let wrapper = shallow(<Dashboard {...props} bills={{lastBills: undefined}}/>);
            let button = wrapper.find(".datapack").find(Button);
            expect(button).toHaveLength(0);
        });

        it("should render Manage my data button after fetching bills if recommendedDataPack is not present", () => {
            let wrapper = shallow(<Dashboard {...props} bills={{lastBills:[], recommendedDataPack:0}} />);
            let button = wrapper.find(".managemydata").find(Button);
            expect(button).toHaveLength(1);
            expect(button.props().buttonText).toEqual("Manage my data");
            expect(wrapper.find(".managemydata .help-text").text()).toEqual("I don't exceed often and I'd like  to know how I can manage my data");

            expect(button.props().clickAction).toEqual(wrapper.instance().redirectToManageYourUsage);
            wrapper.instance().redirectToManageYourUsage();
            expect(utils.redirect).toBeCalledWith('https://www.telstra.com.au/help/my-account/manage-your-usage');
        })
    });
});