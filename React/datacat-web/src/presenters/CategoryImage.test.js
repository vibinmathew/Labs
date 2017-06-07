import React from "react";
import {shallow, mount} from "enzyme";
import CategoryImage from "./CategoryImage";


describe('Category image', () => {
    
    it('renders the correct SVG for gaming', () => {
        let wrapper = shallow(<CategoryImage category="Gaming"/>);
        expect(wrapper.find("svg").text()).toContain('Gaming');
    });

    it('renders the correct SVG for antivirus', () => {
        let wrapper = shallow(<CategoryImage category="Antivirus"/>);
        expect(wrapper.find("svg").text()).toContain('Antivirus');
    });

    it('renders the correct SVG for cloud storage', () => {
        let wrapper = shallow(<CategoryImage category="Cloud Storage & Backup"/>);
        expect(wrapper.find("svg").text()).toContain('Cloud Storage & Backup');
    });

    it('renders the correct SVG for Email & Messaging', () => {
        let wrapper = shallow(<CategoryImage category="Email & Messaging"/>);
        expect(wrapper.find("svg").text()).toContain('Email & Messaging');
    });

    it('renders the correct SVG for File Sharing', () => {
        let wrapper = shallow(<CategoryImage category="File Sharing"/>);
        expect(wrapper.find("svg").text()).toContain('File Sharing');
    });

    it('renders the correct SVG for General Usage', () => {
        let wrapper = shallow(<CategoryImage category="General Usage"/>);
        expect(wrapper.find("svg").text()).toContain('General Usage');
    });

    it('renders the correct SVG for Maps & Navigation', () => {
        let wrapper = shallow(<CategoryImage category="Maps & Navigation"/>);
        expect(wrapper.find("svg").text()).toContain('Maps & Navigation');
    });

    it('renders the correct SVG for Social Media', () => {
        let wrapper = shallow(<CategoryImage category="Social Media"/>);
        expect(wrapper.find("svg").text()).toContain('Social Media');
    });

    it('renders the correct SVG for Streaming Video & Audio', () => {
        let wrapper = shallow(<CategoryImage category="Streaming Video & Audio"/>);
        expect(wrapper.find("svg").text()).toContain('Streaming Video & Audio');
    });

    it('renders the correct SVG for System & Application Updates', () => {
        let wrapper = shallow(<CategoryImage category="System & Application Updates"/>);
        expect(wrapper.find("svg").text()).toContain('System & Application Updates');
    });
    
    it('renders the other SVG for unknown categories', () => {
        let wrapper = shallow(<CategoryImage category="UnknownCategory"/>);
        expect(wrapper.find("svg").text()).toContain('Other');
    });
})

