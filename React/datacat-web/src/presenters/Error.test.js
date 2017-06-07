import React from "react";
import {shallow, mount} from "enzyme";
import Error from "./Error"
import * as utils from "./utils"
import Button from "./Button"

describe('Error', () => {

    it('renders error screen when an error exists in the props', () => {
        utils.redirect = jest.fn();
        
        let wrapper = shallow(<Error message="some message"/>);

        expect(wrapper.find('.error-text').text()).toContain("Oops! some message");
        expect(wrapper.find('.message-centered svg')).toHaveLength(1);

        expect(wrapper.find(Button).props().buttonText).toEqual('Chat with a Customer Service Agent');
        expect(wrapper.find(Button).props().clickAction).toEqual(wrapper.instance().redirectToChat);
        wrapper.instance().redirectToChat();
        expect(utils.redirect).toBeCalledWith('https://www.telstra.com.au/chatnow/landing');
    });
});

