import moment from 'moment';

const validMaxDates = {
  getValue(month, year) {
    if (month === 2 && year % 4 === 0) {
      return 29; // Leap year
    } else {
      return this[month];
    }
  },
  '1': 31,
  '2': 28,
  '3': 31,
  '4': 30,
  '5': 31,
  '6': 30,
  '7': 31,
  '8': 31,
  '9': 30,
  '10': 31,
  '11': 30,
  '12': 31
};

export const getDatesBetweenDateRange = (startDate, stopDate) => {
  const dateArray = [];
  let currentDate = startDate;
  while (currentDate <= stopDate) {
    dateArray.push(new Date(currentDate));
    currentDate = moment(currentDate).add(1, 'days')._d;
  }
  return dateArray;
};

/**
 * Validates date (current support for YYYY-MM-DD only)
 * @param {string} date Date format in YYYY-MM-DD
 */
export function isValidDate(date) {
  if (typeof date === 'string') {
    let [year, month, day] = date.split('-');
    year = +year;
    month = +month;
    day = +day;
    return (
      (month > 0 && month <= 12)
      && (day > 0 && day <= validMaxDates.getValue(month, year))
    );
  }
  return false;
}
