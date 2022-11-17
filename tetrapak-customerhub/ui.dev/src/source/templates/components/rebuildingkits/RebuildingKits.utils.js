/* eslint-disable no-console */
export const _getFormattedCountryData = (array) => {
  array.forEach((item, index) => {
    array[index] = {
      option: item.countryName,
      countryCode: item.countryCode,
      isChecked: index === 0 || item.isChecked ? true : false
    };
  });
  return array;
};
