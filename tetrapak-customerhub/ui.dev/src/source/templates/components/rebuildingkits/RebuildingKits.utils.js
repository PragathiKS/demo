/* eslint-disable */

export const _mapQueryParams = (key) => {
  return key.toLowerCase();
};


export const _remapFilterProperty = (filterProperty) => {
  switch (filterProperty) {
    case 'lineCode':
      return 'lines';
    case 'implStatus':
      return 'implstatuses';
    case 'machineSystem':
      return 'machinesystems';
    case 'equipmentStatus':
      return 'equipmentstatuses';
    case 'rkTypeCode':
      return 'rktypes';
    case 'rebuildingKitStatus':
      return 'rkstatuses';
    case 'rkHandling':
      return 'rkhandlings';
    default:
      return filterProperty;
  }
};

export const _buildQueryUrl = (combinedFiltersObj, filterVal) => {
  console.log({ combinedFiltersObj });
  if (Object.keys(combinedFiltersObj).length) {
    const queryString = Object.keys(combinedFiltersObj).map(key => {
      const appliedFilterApiKey = _remapFilterProperty(key);
      if (appliedFilterApiKey !== filterVal) {
        return `${_mapQueryParams(key)}=${combinedFiltersObj[key]}`;
      }
    }).join('&');

    return queryString;
  } else {
    return '';
  }
};

