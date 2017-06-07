import {browserHistory} from 'react-router'

export function getShortMonthName(date) {
    return date.toLocaleString('en-us', {month: "short"});
}

export function getTwoDigits(digit) {
    return digit.toString().length === 1 ? '0' + digit : digit;
}

export function redirect(url) {
    window.location = url
}

export function route(url) {
    browserHistory.push(url)
}

export function keepOneDecimal(value) {
    var val = value.toString().match(/^-?\d+(?:\.\d?)?/)[0];
    return parseFloat(val).toString();
}

export function convertToGb(kb) {
    return keepOneDecimal(kb / (1024 * 1024));
}