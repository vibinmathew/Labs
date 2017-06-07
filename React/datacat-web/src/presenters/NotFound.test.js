import React from "react";
import {shallow, mount} from "enzyme";
import * as utils from "./utils"
import NotFound from "./NotFound"

describe('NotFound', () => {

    let resetSentStatus = jest.fn();

    let props = {
        resetSentStatus: resetSentStatus,
    };

    it('renders not found screen ', () => {
        utils.redirect = jest.fn();
        
        let wrapper = shallow(<NotFound msisdn="0412345678" />);

        expect(wrapper.find('.not-found-text').text()).toContain('We couldn’t find 0412345678 on our system');
    });

    it('clicking on the go back link should dispatch the reset action and reset the state', () => {

        let wrapper = shallow(<NotFound msisdn="0412345678" {...props} />);

        wrapper.find('.back-link').simulate('click');

        expect(resetSentStatus).toHaveBeenCalled();
    });
});

