import React from "react";
import {shallow, mount} from "enzyme";
import Summary from "./Summary";


describe('Summary', () => {

    it('renders data used', () => {
        let wrapper = shallow(<Summary billing={{usage: 5500000, allowance: 9000000}}/>);
        expect(wrapper.find('div.data-used span.usage').text()).toContain('5.2');
        expect(wrapper.find('div.data-used span.allowance').text()).toContain('8.5');
    });

    it('renders billing cycle remaining days (including today but excluding cycle end date)', () => {
        let endOfCycle = new Date();
        endOfCycle.setDate(endOfCycle.getDate() + 5);
        let end = endOfCycle.getFullYear() + '-' + (endOfCycle.getMonth() + 1) + '-' + endOfCycle.getDate();
        let wrapper = shallow(<Summary billing={{usage: 5500000, allowance: 9000000, end: end}}/>);
        expect(wrapper.find('.days-left').text()).toContain("5 days left");
    });


    it('it renders previous billing cycle and does not render billing cycle remaining days', () => {
        let endOfCycle = new Date();
        endOfCycle.setDate(endOfCycle.getDate() - 5);
        let end = endOfCycle.getFullYear() + '-' + (endOfCycle.getMonth() + 1) + '-' + endOfCycle.getDate();
        let wrapper = shallow(<Summary billing={{usage: 5500000, allowance: 9000000, end: end}}/>);
        expect(wrapper.find('.days-left')).toHaveLength(0);
        expect(wrapper.find('.previous-cycle').text()).toContain("Previous Billing Cycle");
    });

    it('renders red data usage when usage is greater than allowance', () => {
        let wrapper = shallow(<Summary billing={{usage: 9000000, allowance: 5500000}}/>);
        expect(wrapper.find('div.data-used span.usage').props().className).toContain('text-red');
    });

    it('renders black data usage when usage is less than allowance', () => {
        let wrapper = shallow(<Summary billing={{usage: 5500000, allowance: 9000000}}/>);
        expect(wrapper.find('div.data-used span.usage').props().className).not.toContain('text-red');
    });
});

