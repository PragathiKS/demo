/* istanbul ignore file */

/**
 * Get Default Date Range as 90 days
 */
export const _getFilterDateRange = (days, ISOFormat) => {
  const currentDate = new Date();
  const monthsAgo = new Date(currentDate.getFullYear(), currentDate.getMonth(), currentDate.getDate() - Number(days));
  const fromDate = (ISOFormat) ? `${monthsAgo.toISOString().slice(0, 11)}00:00:00` : `${monthsAgo.toISOString().slice(0, 10)}`;
  const toDate = (ISOFormat) ? `${currentDate.toISOString().slice(0, 19)}` : `${currentDate.toISOString().slice(0, 10)}`;

  return [fromDate, toDate];
};

/**
 * Validate the Date field
 */
export const _isValidDate = (dateString) => {
  // First check for the pattern
  if (!/^\d{4}-\d{2}-\d{2}$/.test(dateString)) {
    return false;
  }

  // Parse the date parts to integers
  const parts = dateString.split('-');
  const year = parseInt(parts[0], 10);
  const month = parseInt(parts[1], 10);
  const day = parseInt(parts[2], 10);

  // Check the ranges of month and year
  if (year < 1000 || year > 3000 || month === 0 || month > 12) {
    return false;
  }

  // Check for leap year
  if (month === 2) {
    const isLeapYear = (year % 4 === 0 && year % 100 !== 0) || year % 400 === 0;
    if (isLeapYear && (day < 1 || day > 29)) {
      return false;
    } else if (!isLeapYear && (day < 1 || day > 28)) {
      return false;
    }
  }

  // Check the number of days in the month
  const daysInMonth = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
  if (day < 1 || day > daysInMonth[month - 1]) {
    return false;
  }

  // Date is valid
  return true;
};

/**
 * Validate the From and To Date
 */
export const _isToDateGreaterThanFromDate = (fromDateString, toDateString) => new Date(toDateString).getTime() >= new Date(fromDateString).getTime();

/**
 * Validate the date should be less than or equal today
 */
export const _isDateLessThanOrEqualToToday = (dateString) => {
  const today = new Date();
  today.setHours(0, 0, 0, 0); // Set the time to midnight to compare only the dates
  return new Date(dateString).getTime() <= today.getTime();
};
