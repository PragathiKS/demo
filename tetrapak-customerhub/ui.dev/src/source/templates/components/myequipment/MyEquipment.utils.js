/**
 * Maps data keys to corresponding values in the API
 */
export const _mapQueryParams = (key) => {
  switch (key) {
    case 'customer':
      return 'customernumbers';
    case 'equipmentStatusDesc':
      return 'equipment-statuses';
    case 'equipmentType':
      return 'equipment-types';
    case 'lineName':
      return 'linecodes';
    default:
      return key.toLowerCase();
  }
};

/**
 * Maps data keys to corresponding values in the API
 */
export const _remapFilterProperty = (filterProperty) => {
  switch (filterProperty) {
    case 'equipmentStatusDesc':
      return 'statuses';
    case 'equipmentType':
      return 'types';
    case 'customer':
      return 'customers';
    case 'lineName':
      return 'lines';
    default:
      return filterProperty;
  }
};

/**
 * Build string of query parameters based on currently active filters
 */
export const _buildQueryUrl = (combinedFiltersObj) => {
  if (Object.keys(combinedFiltersObj).length) {
    const queryString = Object.keys(combinedFiltersObj).map(key =>
      `${_mapQueryParams(key)}=${combinedFiltersObj[key]}`).join('&');
    return queryString;
  } else {
    return '';
  }
};

export const _getFormattedCountryData = (array) => {
  array.forEach((item,index) => {
    array[index] = {
      option: item.countryName,
      countryCode: item.countryCode,
      isChecked: index === 0 || item.isChecked ? true: false
    };
  });
  return array;
};

export const _remapFilterOptionKey = (key) => {
  switch (key) {
    case 'lineName':
      return 'lineCode';
    case 'customer':
      return 'customerNumber';
    case 'equipmentStatusDesc':
      return 'equipmentStatus';
    default:
      return key;
  }
};
