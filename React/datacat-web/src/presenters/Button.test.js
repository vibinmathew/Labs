import React from "react";
import {shallow, mount} from "enzyme";
import Button from "./Button"

describe('Login', () => {
    let clickAction = jest.fn();

    let props = {
        buttonText: "Button Text",
        clickAction: clickAction 
    };

    it('renders the button', () => {
        let wrapper = shallow(<Button {...props}/>);
        expect(wrapper.find(".button span").text()).toEqual("Button Text");
        expect(wrapper.find(".button svg")).toHaveLength(1);
        wrapper.find(".button").simulate('click');
        expect(clickAction).toHaveBeenCalled();
    });
});

