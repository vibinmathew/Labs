import React from "react";
import {shallow, mount} from "enzyme";
import LastBills from "./LastBills";

describe('LastBills', () => {

    let props = {
        lastBills: [
            {date: '2017-02-15', bill: 150, baseBill: 100, extraDataCharge: 50},
            {date: '2017-01-15', bill: 140, baseBill: 100, extraDataCharge: 0},
            {date: '2016-12-15', bill: 160, baseBill: 100, extraDataCharge: 60}
        ]
    };

    fit('renders without crashing', () => {
        let wrapper = shallow(<LastBills {...props}/>);
        expect(wrapper.find("div.last-bills-header").text()).toContain("Here is what you've spent over the last 3 months:");
        let bills = wrapper.find(".last-bills-list .bill-element");

        expect(bills.at(0).find(".bill-month-name").text()).toEqual("Feb");
        expect(bills.at(0).find(".bill-value").text()).toEqual("$150");
        expect(bills.at(0).find(".bill-info").text()).toEqual("$100 Base Plan + $50 Extra Data");
        expect(bills.at(1).find(".bill-month-name").text()).toEqual("Jan");
        expect(bills.at(1).find(".bill-value").text()).toEqual("$140");
        expect(bills.at(1).find(".bill-info").text()).toEqual("$100 Base Plan");
        expect(bills.at(2).find(".bill-month-name").text()).toEqual("Dec");
        expect(bills.at(2).find(".bill-value").text()).toEqual("$160");
        expect(bills.at(2).find(".bill-info").text()).toEqual("$100 Base Plan + $60 Extra Data");
    });
});

