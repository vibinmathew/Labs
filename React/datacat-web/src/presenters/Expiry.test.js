import React from "react";
import {shallow, mount} from "enzyme";

import Expiry from "./Expiry"

describe('Expiry', () => {
    it('renders properly', () => {
        let wrapper = shallow(<Expiry />);
        expect(wrapper.find('.error-text').text()).toContain('Sorry! We only store 3 months of your data usage.');
        expect(wrapper.find('.message-centered svg')).toHaveLength(1);
    });
})

