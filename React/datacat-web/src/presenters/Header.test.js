import React from "react";
import {shallow, mount} from "enzyme";
import Header from "./Header"
import * as utils from "./utils"

describe('Header', () => {
    
    it('renders title', () => {
        let wrapper = shallow(<Header title="This is a title"/>);
        expect(wrapper.find('.title').text()).toContain('This is a title');
        expect(wrapper.find('svg')).toHaveLength(1);
        expect(wrapper.find('.back')).toHaveLength(0);
    });
    
    it('renders back button if back path is available', () => {
        utils.route = jest.fn();
        let wrapper = shallow(<Header title="This is a title" backPath="/usage/12345"/>);
        expect(wrapper.find('.title').text()).toContain('This is a title');
        expect(wrapper.find('svg')).toHaveLength(1);
        let back = wrapper.find('.back');
        expect(back).toHaveLength(1);

        back.simulate("click");

        expect(utils.route).toBeCalledWith('/usage/12345');
    });
})

