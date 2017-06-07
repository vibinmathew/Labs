import React, {Component, PropTypes} from "react";
import {ResponsiveContainer, BarChart, Bar, XAxis, Line} from "recharts";
import {getShortMonthName, getTwoDigits} from "./utils";

class CategorizationGraph extends Component {

    render() {
        const {dataCategorization, billing} = this.props;

        return (
            <div className="outer-container">
                <div id="container">
                    {this.renderDailyList(dataCategorization, billing)}
                </div>
            </div>
        );
    }

    getDayObject(date, usageList, updatedAt) {
        let currentDateStr = `${date.getFullYear()}-${getTwoDigits(date.getMonth() + 1)}-${getTwoDigits(date.getDate())}`;

        let dayObject = usageList.find(x => x.date === currentDateStr);

        if (dayObject && updatedAt.setHours(0,0,0,0) > date) {
            return {date: date, usage: dayObject.usage}; 
        }
        else {
            return {date: date, usage: []}
        }
    }

    buildDataPoint(dayObject, selectedCategory) {
        let rest = 0;
        let selected = 0;

        for (let j = 0; j < dayObject.usage.length; j++) {
            if (dayObject.usage[j].category === selectedCategory) {
                selected = dayObject.usage[j].amount
            } else {
                rest += dayObject.usage[j].amount;
            }
        }
        return {
            "date": dayObject.date.getDate() + ' ' + getShortMonthName(dayObject.date),
            "selected": selected,
            "rest": rest
        };
    }

    renderDailyList(dataCategorization, billing) {
        if (dataCategorization && dataCategorization.dailyUsage) {
            let data = [];

            if (billing && billing.start) {
                let currentDate = new Date(billing.start);
                let endDate = new Date(billing.end);

                currentDate.setHours(0, 0, 0, 0);
                endDate.setHours(0, 0, 0, 0);

                while (currentDate <= endDate) {
                    let dayObject = this.getDayObject(currentDate, dataCategorization.dailyUsage, billing.updatedAt)
                    data.push(this.buildDataPoint(dayObject, dataCategorization.selectedCategory));
                    currentDate.setDate(currentDate.getDate() + 1);
                }
            }
            else {
                let days = dataCategorization.dailyUsage;
                for (let i = 0; i < days.length; i++) {
                    let day = days[i];
                    data.push(this.buildDataPoint({date: new Date(day.date), usage: day.usage}, dataCategorization.selectedCategory));
                }
            }

            const CustomizedLabel = React.createClass({
                render () {
                    const {x, y, stroke, value} = this.props;

                    return <text x={x} y={y} dy={-4} fill={stroke} fontSize={10} textAnchor="middle">{value}</text>
                }
            });
            const CustomizedAxisTick = React.createClass({
                render () {
                    const {x, y, payload} = this.props;
                    return (
                        <g transform={`translate(${x},${y})`}>
                            <text className="x-axis-label" x={0} y={0} dy={0} textAnchor="end" fill="#666" transform="rotate(-90)">{payload.value}</text>
                        </g>
                    );
                }
            });
            return (
                <ResponsiveContainer>
                    <BarChart data={data} barCategoryGap={'5%'}>
                        <XAxis padding={{left:8}} interval="preserveStartEnd" tickLine={false} dataKey="date" height={110} tick={<CustomizedAxisTick/>} />
                        <Bar dataKey="rest" stackId="a" fill="#e4e4e4" isAnimationActive={false}/>
                        <Bar dataKey="selected" stackId="a" fill="#2d86ca" isAnimationActive={false}/>
                        <Line className="x-axis" type="monotone" label={<CustomizedLabel />}/>
                    </BarChart>
                </ResponsiveContainer>
            )
        }
    }
}

CategorizationGraph.propTypes = {
    dataCategorization: PropTypes.object
};
export default CategorizationGraph;
