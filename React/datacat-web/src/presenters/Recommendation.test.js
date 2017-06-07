import React from "react";
import {shallow, mount} from "enzyme";
import Recommendation from "./Recommendation";
import {BarChart} from "recharts";

describe('Recommendation', () => {

    let props = {
        bills: {
            lastBills: [
                {date: '2017-02-15', bill: 150, baseBill: 100, extraDataCharge: 50, extraData: 5000000},
                {date: '2017-01-15', bill: 150, baseBill: 100, extraDataCharge: 40, extraData: 4500000},
                {date: '2016-12-15', bill: 150.1, baseBill: 100, extraDataCharge: 60, extraData: 6000000}
            ],
            recommendedDataPack: {
                price: 15,
                amount: 2
            }
        },
        billing: {
            baseBill: 100
        }
    };

    it('renders without crashing', () => {
        let wrapper = shallow(<Recommendation {...props}/>);
        let recommendationText = wrapper.find("div.recommendations").text();
        let billCalculationSection = wrapper.find(".bill-calculation-txt");

        expect(recommendationText).toContain("That's an average of $150/mth due to 4.9GB of Extra Data");
        expect(recommendationText).toContain("Only pay $115/mth with a 2GB Data Pack.");
        expect(wrapper.find(BarChart)).toHaveLength(1);
        expect(billCalculationSection).toHaveLength(1);
        expect(billCalculationSection.text()).toContain("How was this calculated?");
        expect(wrapper.find('.bill-calculation-details')).toHaveLength(0);
        billCalculationSection.simulate('click');
        let billCalculationDetailsSection = wrapper.find('.bill-calculation-details');
        expect(billCalculationDetailsSection).toHaveLength(1);
        expect(billCalculationDetailsSection.text()).toContain("1GB = $10");
        expect(billCalculationDetailsSection.text()).toContain("4.9GB = $50");
        expect(wrapper.find('.graph-legend').text()).toContain("4.9GB Extra Data");
        expect(wrapper.find('.graph-legend').text()).toContain("2GB Extra Data");
        expect(wrapper.find('.graph-legend').text()).toContain("Base Plan");

        billCalculationSection.simulate('click');
        expect(wrapper.find('.bill-calculation-details')).toHaveLength(0);
    });
});

