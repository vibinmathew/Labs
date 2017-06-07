import React from "react";
import {shallow, mount} from "enzyme";
import Spinner from "./Spinner"

describe('Spinner', () => {
    it('renders loading screen', () => {
        let wrapper = shallow(<Spinner spinnerText={["Some Spinner Text", "Some More Text"]}/>);
        expect(wrapper.find('.loading-text').text()).toContain('Some Spinner Text');
        expect(wrapper.find('.loading-text').text()).toContain('Some More Text');
        expect(wrapper.find('.message-centered svg')).toHaveLength(1);
    });
})

