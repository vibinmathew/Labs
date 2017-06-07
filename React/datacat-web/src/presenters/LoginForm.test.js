import React from "react";
import {shallow, mount} from "enzyme";
import LoginForm from "./LoginForm"
import Header from "./Header"
import NotFound from "./NotFound"
import Button from "./Button"
import Spinner from "./Spinner"

describe('Login', () => {
    let sendSMS = jest.fn();
    let resetSentStatus = jest.fn();

    let props = {
        sendSMS: sendSMS,
        resetSentStatus: resetSentStatus,
        login: {sent: false}
    };

    beforeEach(() => {
        sendSMS.mockClear();
    });

    it('renders login screen', () => {
        let wrapper = shallow(<LoginForm {...props}/>);
        expect(wrapper.find(Header)).toHaveLength(1);
        expect(wrapper.find(Header).props().title).toEqual("My Telstra data usage");
        expect(wrapper.find('.welcome-text').text()).toContain("You've been selected to preview a new tool from Telstra");
        expect(wrapper.find('input')).toHaveLength(1);
        expect(wrapper.find(Button)).toHaveLength(1);
        expect(wrapper.find(Button).props().buttonText).toEqual("Send me an SMS");
        expect(wrapper.find(Button).props().clickAction).toEqual(wrapper.instance().handleSubmit);
        expect(wrapper.find('.info-text')).toHaveLength(0);
        expect(wrapper.find('.tick')).toHaveLength(0);
        expect(wrapper.find('.invalid-number-message').text()).toEqual(' ')
    });

    it('Clicking should dispactch sendSMS with phone number', () => {
        let wrapper = shallow(<LoginForm {...props} />);
        wrapper.find('input').simulate('change', {target: {value: '61412345678'}});
        wrapper.instance().handleSubmit();
        expect(sendSMS).toBeCalledWith('61412345678');
    });

    it('Replace leading 04 with 614', () => {
        let wrapper = shallow(<LoginForm {...props} />);
        wrapper.find('input').simulate('change', {target: {value: '0412345678'}});
        wrapper.instance().handleSubmit();
        expect(sendSMS).toBeCalledWith('61412345678');
    });

    it('Replace leading 4 with 614', () => {
        let wrapper = shallow(<LoginForm {...props} />);
        wrapper.find('input').simulate('change', {target: {value: '412345678'}});
        wrapper.instance().handleSubmit();
        expect(sendSMS).toBeCalledWith('61412345678');
    });


    it('Replace leading 00614 with 614', () => {
        let wrapper = shallow(<LoginForm {...props} />);
        wrapper.find('input').simulate('change', {target: {value: '0061412345678'}});
        wrapper.instance().handleSubmit();
        expect(sendSMS).toBeCalledWith('61412345678');
    });

    it('Replace leading +614 with 614', () => {
        let wrapper = shallow(<LoginForm {...props} />);
        wrapper.find('input').simulate('change', {target: {value: '+61412345678'}});
        wrapper.instance().handleSubmit();
        expect(sendSMS).toBeCalledWith('61412345678');
    });

    it('Should strip unneeded chars (, ), space, tab', () => {
        let wrapper = shallow(<LoginForm {...props} />);
        wrapper.find('input').simulate('change', {target: {value: '(614)12 345   678'}});
        wrapper.instance().handleSubmit();
        expect(sendSMS).toBeCalledWith('61412345678');
    });

    it('Clicking should dispactch sendSMS with phone number', () => {
        let wrapper = shallow(<LoginForm {...props} login={{sent: true}}/>);
        expect(wrapper.find('.info-text')).toHaveLength(1);
        expect(wrapper.find('.welcome-text')).toHaveLength(0);
    });

    it('renders not found page when receiving 404 from API', () => {
        let wrapper = shallow(<LoginForm {...props} login={{isNotFound: true}}/>);
        expect(wrapper.find(NotFound)).toHaveLength(1);
    });

    it('renders Spinner while loading', () => {
        let wrapper = shallow(<LoginForm {...props} login={{isFetching: true}}/>);
        expect(wrapper.find(Spinner)).toHaveLength(1);
        expect(wrapper.find(Spinner).props().spinnerText).toEqual(["Hang tight!", "We are looking for your number in our system"]);
    });
    
    it('renders tick when the number entered is valid', () => {
        let wrapper = shallow(<LoginForm {...props} />);
        wrapper.find('input').simulate('change', {target: {value: '61412345678'}});
        expect(wrapper.find('.tick')).toHaveLength(1);
    });

    it('doesnt renders tick when the number entered is invalid', () => {
        let wrapper = shallow(<LoginForm {...props} />);
        wrapper.find('input').simulate('change', {target: {value: '614123'}});
        expect(wrapper.find('.tick')).toHaveLength(0);
    });

    it('renders error message when the button is clicked and the number entered is invalid', () => {
        let wrapper = shallow(<LoginForm {...props} />);
        wrapper.find('input').simulate('change', {target: {value: '614123'}});
        wrapper.instance().handleSubmit();
        expect(wrapper.find('.invalid-number-message').text()).toEqual("Please enter a valid mobile number and try again");
        expect(sendSMS).not.toHaveBeenCalled();
    });

    it('clicking on the go back link should dispatch the reset action and reset the state', () => {
        let wrapper = shallow(<LoginForm {...props} login={{sent: true}} />);
        wrapper.find('.back-link').simulate('click');

        expect(resetSentStatus).toHaveBeenCalled();

        wrapper.setProps({
            login: {
                sent: false
            }
        });
        expect(wrapper.find('.welcome-text').text()).toContain("You've been selected to preview a new tool from Telstra");
        expect(wrapper.find('input')).toHaveLength(1);
        expect(wrapper.find('.invalid-number-message').text()).toEqual(' ')
        expect(wrapper.find('input').props().value).toEqual('')
    });
});

