import React from "react";
import {shallow, mount} from "enzyme";
import Categorization from "./Categorization";
import CategorizationGraph from "./CategorizationGraph";
import CategoryList from "./CategoryList";
import * as utils from "./utils"


describe('Categorization', () => {

    it('renders without crashing', () => {
        let wrapper = shallow(<Categorization billing={{}} dataCategorization={{}}/>);
        expect(wrapper.find(CategoryList)).toHaveLength(1);
        expect(wrapper.find(CategorizationGraph)).toHaveLength(1);
    });

    it('injects dataCategorization to CategorizationGraph component', () => {
        let dataCategorization = {};
        let billing = {};
        let wrapper = shallow(<Categorization billing={{}} dataCategorization={dataCategorization}/>);
        
        let categorizationGraph = wrapper.find(CategorizationGraph);
        expect(categorizationGraph).toHaveLength(1);
        expect(categorizationGraph.nodes[0].props).toEqual({billing, dataCategorization});
    });

    it('renders billing cycle', () => {
        let wrapper = shallow(<Categorization billing={{start: "2017-02-15", end: "2017-03-14"}} dataCategorization={{}}/>);
        expect(wrapper.find('span.billing-cycle').text()).toContain('15 Feb - 14 Mar, 2017');
    });

    it('renders graph legend with selected category', () => {
        let wrapper = shallow(<Categorization billing={{}} dataCategorization={{selectedCategory: 'Gaming'}}/>);
        expect(wrapper.find('.graph-legend').text()).toContain("Total Usage");
        expect(wrapper.find('.graph-legend').text()).toContain("Gaming");
        expect(wrapper.find('.graph-legend .legend-selected-colour')).toHaveLength(1);
        expect(wrapper.find('.graph-legend .legend-total-colour')).toHaveLength(1);
    });

    it('has previous and next buttons', () => {
        let wrapper = shallow(<Categorization billing={{}} dataCategorization={{}}/>);
        expect(wrapper.find('.next-button')).toHaveLength(1);
        expect(wrapper.find('.prev-button')).toHaveLength(1);
    });

    it('routes when prev button is clicked', () => {
        utils.route = jest.fn();

        let wrapper = shallow(<Categorization billing={{nextBillingCycleUrl: "token123/1", previousBillingCycleUrl: "token123/-1"}} dataCategorization={{}}/>);
        wrapper.find('.prev-button').simulate('click');
        expect(utils.route).toBeCalledWith('/usage/token123/-1');
    });

    it('routes when next button is clicked', () => {
        utils.route = jest.fn();

        let wrapper = shallow(<Categorization billing={{nextBillingCycleUrl: "token123/1", previousBillingCycleUrl: "token123/-1"}} dataCategorization={{}}/>);
        wrapper.find('.next-button').simulate('click');
        expect(utils.route).toBeCalledWith('/usage/token123/1');
    });

    it('should not route when prev button is clicked if there is no previousBillingCycleUrl', () => {
        utils.route = jest.fn();

        let wrapper = shallow(<Categorization billing={{nextBillingCycleUrl: "token123/1", previousBillingCycleUrl: ""}} dataCategorization={{}}/>);
        wrapper.find('.prev-button-disabled').simulate('click');
        expect(wrapper.find('.prev-button')).toHaveLength(0);
        expect(utils.route).not.toHaveBeenCalled();
    });

    it('should not route when next button is clicked if there is no nextBillingCycleUrl', () => {
        utils.route = jest.fn();

        let wrapper = shallow(<Categorization billing={{nextBillingCycleUrl: "", previousBillingCycleUrl: "token123/1"}} dataCategorization={{}}/>);
        wrapper.find('.next-button-disabled').simulate('click');
        expect(wrapper.find('.next-button')).toHaveLength(0);
        expect(utils.route).not.toHaveBeenCalled();
    });
})

