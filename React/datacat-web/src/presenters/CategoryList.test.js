import React from "react";
import {shallow, mount} from "enzyme";
import CategoryList from "./CategoryList";
import CategoryImage from "./CategoryImage";


describe('CategoryList', () => {
    let dataCat = {
        "overall": [
            {"category": "Streaming", "amount": 70},
            {"category": "Gaming", "amount": 30}
        ],
        selectedCategory: "Streaming"
    };

    it('renders list of categories with percentage', () => {
        let wrapper = shallow(<CategoryList dataCategorization={dataCat}/>);
        let lis = wrapper.find('li');
        lis.forEach((li) => {
            expect(li.text()).toMatch(/^.+\d+%$/)
        });
        
        expect(lis.nodes[0].key).toEqual('Streaming');
        expect(lis.nodes[0].props.className).toContain('selected');
        expect(lis.nodes[1].key).toEqual('Gaming');
        expect(lis.nodes[1].props.className).not.toContain('selected');
    });

    it('renders list of categories with image', () => {
        let wrapper = shallow(<CategoryList dataCategorization={dataCat}/>);
        let lis = wrapper.find('li');
        expect(lis.first().find(CategoryImage).props().category).toEqual('Streaming');
        expect(lis.last().find(CategoryImage).props().category).toEqual('Gaming');
        
    });

    it('renders list of categories with the selected category highlighted', () => {
        
        dataCat.selectedCategory = 'Gaming';
        
        let wrapper = shallow(<CategoryList dataCategorization={dataCat}/>);
        let lis = wrapper.find('li');

        expect(lis.nodes[0].key).toEqual('Streaming');
        expect(lis.nodes[0].props.className).not.toContain('selected');
        expect(lis.nodes[1].key).toEqual('Gaming');
        expect(lis.nodes[1].props.className).toContain('selected');
    });

    describe('click on a category', () => {
        it('dispatches the new category selection', () => {
            let selectCategory = jest.fn();

            let wrapper = shallow(<CategoryList dataCategorization={dataCat} selectCategory={selectCategory}/>);
            let find = wrapper.find('li#Gaming');
            find.simulate('click');
            expect(selectCategory).toBeCalledWith('Gaming');
        })
    })
});

