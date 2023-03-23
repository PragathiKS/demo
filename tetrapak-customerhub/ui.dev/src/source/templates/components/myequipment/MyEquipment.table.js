/* istanbul ignore file */

/**
 * Returns an obj containing all table rows to be rendered with HBS
 */
export const _buildTableRows = (data, keys) => {
  const dataObject = {
    row: []
  };

  keys.forEach((key, index) => {
    const value = data[key];

    if (key === 'serialNumber') {
      const permanentVolumeConversion = data['permanentVolumeConversion'];
      dataObject.row[index] = {
        key,
        value,
        permanentVolumeConversion
      };
    } else {
      dataObject.row[index] = {
        key,
        value
      };
    }

    if (data['id']) {
      dataObject.rowLink = data['id'];
      dataObject.isClickable = true;
    }
  });

  return dataObject;
};

/**
 * Groups equipmentTypes according to bussinesType: Packaging or Processing
 */
export const _groupByBusinessType = (filterOptionsArr) => {
  const businessTypeArr = [];
  const groupedFilterOptions = [];
  // get all unique business types
  filterOptionsArr.forEach((item) => {
    if (businessTypeArr.indexOf(item['businessType']) === -1) {
      businessTypeArr.push(item['businessType']);
    }
  });

  // create an object for each business type with it's corresponding items
  businessTypeArr.forEach((businessType) => {
    const optionsForBusinessType = filterOptionsArr.filter(filterOption => filterOption.businessType === businessType);

    groupedFilterOptions.push({
      businessTypeLabel: businessType,
      options: optionsForBusinessType,
      isChecked: !optionsForBusinessType.some(filterOption => filterOption.isChecked === false)
    });
  });

  return groupedFilterOptions;
};

/**
 * Return obj. contaning table header labels and tooltip text
 */
export const _getKeyMap = (key, i18nKeys) => {
  const headerObj = {};
  switch (key) {
    case 'countryCode': {
      headerObj['keyLabel'] = i18nKeys['country'];
      headerObj['showTooltip'] = i18nKeys['countryToolTip'].trim().length > 0 ? true : false;
      headerObj['tooltipText'] = i18nKeys['countryToolTip'];
      break;
    }
    case 'site': {
      headerObj['keyLabel'] = i18nKeys['site'];
      headerObj['showTooltip'] = i18nKeys['siteToolTip'].trim().length > 0 ? true : false;
      headerObj['tooltipText'] = i18nKeys['siteToolTip'];
      break;
    }
    case 'lineCode': {
      headerObj['keyLabel'] = i18nKeys['line'];
      headerObj['showTooltip'] = i18nKeys['lineToolTip'].trim().length > 0 ? true : false;
      headerObj['tooltipText'] = i18nKeys['lineToolTip'];
      break;
    }
    case 'equipmentName': {
      headerObj['keyLabel'] = i18nKeys['equipmentDescription'];
      headerObj['showTooltip'] = i18nKeys['equipDescToolTip'].trim().length > 0 ? true : false;
      headerObj['tooltipText'] = i18nKeys['equipDescToolTip'];
      break;
    }
    case 'siteDesc': {
      headerObj['keyLabel'] = i18nKeys['siteDescription'];
      headerObj['showTooltip'] = i18nKeys['siteDescToolTip'].trim().length > 0 ? true : false;
      headerObj['tooltipText'] = i18nKeys['siteDescToolTip'];
      break;
    }
    case 'location': {
      headerObj['keyLabel'] = i18nKeys['location'];
      headerObj['showTooltip'] = i18nKeys['locationToolTip'].trim().length > 0 ? true : false;
      headerObj['tooltipText'] = i18nKeys['locationToolTip'];
      break;
    }
    case 'serialNumber': {
      headerObj['keyLabel'] = i18nKeys['serialNumber'];
      headerObj['showTooltip'] = i18nKeys['serialNumToolTip'].trim().length > 0 ? true : false;
      headerObj['tooltipText'] = i18nKeys['serialNumToolTip'];
      break;
    }
    case 'equipmentStatusDesc': {
      headerObj['keyLabel'] = i18nKeys['equipmentStatus'];
      headerObj['showTooltip'] = i18nKeys['equipStatToolTip'].trim().length > 0 ? true : false;
      headerObj['tooltipText'] = i18nKeys['equipStatToolTip'];
      break;
    }
    case 'functionalLocation': {
      headerObj['keyLabel'] = i18nKeys['functionalLocation'];
      headerObj['showTooltip'] = i18nKeys['functionalLocationToolTip'].trim().length > 0 ? true : false;
      headerObj['tooltipText'] = i18nKeys['functionalLocationToolTip'];
      break;
    }
    default: {
      break;
    }
  }
  return headerObj;
};

/**
 * Return obj. contaning table header data used in HBS rendering
 */
export const _mapHeadings = (keys, i18nKeys, activeSortData, sortableKeysArr) => {
  const sortByKey = activeSortData && activeSortData.sortedByKey;
  const sortOrder = activeSortData && activeSortData.sortOrder;
  return keys.map(key => ({
    key,
    myEquipment: true,
    isSortable: sortableKeysArr.includes(key),
    isActiveSort: key === sortByKey,
    sortOrder: sortOrder,
    i18nKey: _getKeyMap(key,i18nKeys).keyLabel,
    showTooltip: _getKeyMap(key,i18nKeys).showTooltip,
    tooltipText: _getKeyMap(key,i18nKeys).tooltipText
  }));
};
