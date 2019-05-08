Date.prototype.addDays = function (days) {
  let date = new Date(this.valueOf());
  date.setDate(date.getDate() + days);
  return date;
};

export const getDatesBetweenDateRange = (startDate, stopDate) => {
  let dateArray = [];
  let currentDate = startDate;
  while (currentDate <= stopDate) {
    dateArray.push(new Date(currentDate));
    currentDate = currentDate.addDays(1);
  }
  return dateArray;
};
export const getFormattedDate = (date) => {
  var month = date.getMonth() <= 8 ? `0${date.getMonth() + 1}` : date.getMonth() + 1;
  var day = date.getDate() <= 9 ? `0${date.getDate()}` : date.getDate();
  var year = date.getFullYear();
  return `${year}-${month}-${day}`;
};
