import React from "react";
import {shallow} from "enzyme";
import {ResponsiveContainer, BarChart, Bar} from "recharts";
import CategorizationGraph from "./CategorizationGraph";


describe('CategorizationGraph', () => {

    let data = {
        overall: [
            {"category": "Video Streaming", "amount": 50},
            {"category": "Browsing", "amount": 25},
            {"category": "Gaming", "amount": 20},
            {"category": "Others", "amount": 5}
        ],
        dailyUsage: [
            {
                "date": "2017-02-28",
                "usage": [
                    {"category": "Browsing", "amount": 456},
                    {"category": "Video Streaming", "amount": 789},
                    {"category": "Gaming", "amount": 123},
                    {"category": "Others", "amount": 7531}
                ]
            },
            {
                "date": "2017-03-01",
                "usage": [
                    {"category": "Browsing", "amount": 213},
                    {"category": "Gaming", "amount": 345},
                    {"category": "Video Streaming", "amount": 654},
                    {"category": "Others", "amount": 553}
                ]
            },
            {
                "date": "2017-03-02",
                "usage": [
                    {"category": "Browsing", "amount": 213},
                    {"category": "Gaming", "amount": 345},
                    {"category": "Video Streaming", "amount": 654},
                    {"category": "Others", "amount": 553}
                ]
            },
            {
                "date": "2017-03-03",
                "usage": [
                    {"category": "Browsing", "amount": 213},
                    {"category": "Gaming", "amount": 345},
                    {"category": "Video Streaming", "amount": 654},
                    {"category": "Others", "amount": 553}
                ]
            }
        ],
        selectedCategory: "Browsing"
    };

    let billing= {
        "usage": 6491872,
        "allowance": 5277944,
        "start": "2017-02-28",
        "end": "2017-03-04",
        "updatedAt": new Date(2017, 2, 2, 14, 5)
    };

    it('highlights the selected category', () => {
        
        let wrapper = shallow(<CategorizationGraph dataCategorization={data} />);

        let find = wrapper.find(BarChart);
        expect(find.props().data).toEqual(  [
            {
                "date": "28 Feb",
                "rest": 8443,
                "selected": 456
            },
            {
                "date": "1 Mar",
                "rest": 1552,
                "selected": 213
            },
            {
                "date": "2 Mar",
                "rest": 1552,
                "selected": 213
            },
            {
                "date": "3 Mar",
                "rest": 1552,
                "selected": 213
            }
        ]);
    });

    it('renders all dates in the billing range with data after the last updated date excluded', () => {
        
        let wrapper = shallow(<CategorizationGraph dataCategorization={data} billing={billing}/>);

        let find = wrapper.find(BarChart);
        expect(find.props().data).toEqual([
            {
                "date": "28 Feb",
                "rest": 8443,
                "selected": 456
            },
            {
                "date": "1 Mar",
                "rest": 1552,
                "selected": 213
            },
            {
                "date": "2 Mar",
                "rest": 0,
                "selected": 0
            },
            {
                "date": "3 Mar",
                "rest": 0,
                "selected": 0
            },
            {
                "date": "4 Mar",
                "rest": 0,
                "selected": 0
            }
        ]);
    });

    it('does not render bar chart when no data', () => {
        let wrapper = shallow(<CategorizationGraph />);
        expect(wrapper.find(ResponsiveContainer)).toHaveLength(0);
        expect(wrapper.find(BarChart)).toHaveLength(0);
    });
});