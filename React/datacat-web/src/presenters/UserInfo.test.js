import React from "react";
import {shallow, mount} from "enzyme";
import UserInfo from "./UserInfo";


describe('User Info', () => {
    let fetchUserInfo = jest.fn();

    let props = {
        msisdn: '0412345678',
        fetchUserInfo: fetchUserInfo
    };


    it('renders properly without data', () => {
        let wrapper = shallow(<UserInfo />);
        expect(wrapper.find('.userinfo').text()).not.toContain('This data is an approximation');
        expect(wrapper.find('.userinfo').text()).not.toContain('Last updated at');
    });

    it('renders user MSISDN', () => {
        let wrapper = shallow(<UserInfo user={{msisdn: '0412345678'}}/>);
        expect(wrapper.find('.userinfo').text()).toContain('This data is an approximation of actual usage for 0412345678.');
    });

    it('renders updatedAt', () => {
        let date = new Date(2017, 2, 17, 20, 5);
        let wrapper = shallow(<UserInfo billing={{updatedAt: date}}/>);
        expect(wrapper.find('.userinfo').text()).toContain('Last updated at 8:05 PM on 17/3/2017');
    });
    
});

