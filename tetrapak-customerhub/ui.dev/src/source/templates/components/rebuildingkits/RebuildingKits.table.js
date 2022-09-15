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
 * Return obj. contaning table header labels and tooltip text
 */
export const _getKeyMap = (key, i18nKeys) => {
  const headerObj = {};
  switch (key) {
    case 'lineCode': {
      headerObj['keyLabel'] = i18nKeys['functionalLocation'];
      headerObj['showTooltip'] =
        i18nKeys['functionalLocationToolTip'].trim().length > 0 ? true : false;
      headerObj['tooltipText'] = i18nKeys['functionalLocationToolTip'];
      break;
    }
    case 'equipmentDesc': {
      headerObj['keyLabel'] = i18nKeys['equipmentDescription'];
      headerObj['showTooltip'] =
        i18nKeys['equipDescToolTip'].trim().length > 0 ? true : false;
      headerObj['tooltipText'] = i18nKeys['equipDescToolTip'];
      break;
    }
    case 'serialNumber': {
      headerObj['keyLabel'] = i18nKeys['serialNumber'];
      headerObj['showTooltip'] =
        i18nKeys['serialNumToolTip'].trim().length > 0 ? true : false;
      headerObj['tooltipText'] = i18nKeys['serialNumToolTip'];
      break;
    }
    case 'rkNumber': {
      headerObj['keyLabel'] = i18nKeys['rkNumber'];
      headerObj['showTooltip'] =
        i18nKeys['rkNumberToolTip'].trim().length > 0 ? true : false;
      headerObj['tooltipText'] = i18nKeys['rkNumberToolTip'];
      break;
    }
    case 'rkDesc': {
      headerObj['keyLabel'] = i18nKeys['rkDesc'];
      headerObj['showTooltip'] =
        i18nKeys['rkDescToolTip'].trim().length > 0 ? true : false;
      headerObj['tooltipText'] = i18nKeys['rkDescToolTip'];
      break;
    }
    case 'implStatus': {
      headerObj['keyLabel'] = i18nKeys['implStatus'];
      headerObj['showTooltip'] =
        i18nKeys['implStatusToolTip'].trim().length > 0 ? true : false;
      headerObj['tooltipText'] = i18nKeys['implStatusToolTip'];
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
export const _mapHeadings = (
  keys,
  i18nKeys
) => {
  return keys.map((key) => ({
    key,
    i18nKey: _getKeyMap(key, i18nKeys).keyLabel,
    showTooltip: _getKeyMap(key, i18nKeys).showTooltip,
    tooltipText: _getKeyMap(key, i18nKeys).tooltipText
  }));
};