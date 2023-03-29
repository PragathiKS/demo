import {
  RK_ICON,
  RK_COUNTRY_CODE,
  RK_LINE_CODE,
  RK_EQ_DESC,
  RK_MACHINE_SYSTEM,
  RK_SERIAL_NUMBER,
  RK_EQ_STATUS,
  RK_NUMBER,
  RK_DESC,
  RK_IMPL_STATUS,
  RK_IMPL_DATE,
  RK_IMPL_STATUS_DATE,
  RK_GENERAL_NUMBER,
  RK_TYPE_CODE,
  RK_RELEASE_DATE,
  RK_IMPL_DEADLINE,
  RK_STATUS,
  RK_HANDLING,
  RK_ORDER,
  RK_I18N_COUNTRY_CODE,
  RK_I18N_LINE_CODE,
  RK_I18N_EQ_DESC,
  RK_I18N_MACHINE_SYSTEM,
  RK_I18N_SERIAL_NUMBER,
  RK_I18N_EQ_STATUS,
  RK_I18N_NUMBER,
  RK_I18N_DESC,
  RK_I18N_IMPL_STATUS,
  RK_I18N_IMPL_DATE,
  RK_I18N_IMPL_STATUS_DATE,
  RK_I18N_GENERAL_NUMBER,
  RK_I18N_TYPE_CODE,
  RK_I18N_RELEASE_DATE,
  RK_I18N_IMPL_DEADLINE,
  RK_I18N_STATUS,
  RK_I18N_HANDLING,
  RK_I18N_ORDER,
  RK_I18N_COUNTRY_CODE_TOOLTIP,
  RK_I18N_LINE_CODE_TOOLTIP,
  RK_I18N_EQ_DESC_TOOLTIP,
  RK_I18N_MACHINE_SYSTEM_TOOLTIP,
  RK_I18N_SERIAL_NUMBER_TOOLTIP,
  RK_I18N_EQ_STATUS_TOOLTIP,
  RK_I18N_NUMBER_TOOLTIP,
  RK_I18N_DESC_TOOLTIP,
  RK_I18N_IMPL_STATUS_TOOLTIP,
  RK_I18N_IMPL_DATE_TOOLTIP,
  RK_I18N_IMPL_STATUS_DATE_TOOLTIP,
  RK_I18N_GENERAL_NUMBER_TOOLTIP,
  RK_I18N_TYPE_CODE_TOOLTIP,
  RK_I18N_RELEASE_DATE_TOOLTIP,
  RK_I18N_IMPL_DEADLINE_TOOLTIP,
  RK_I18N_STATUS_TOOLTIP,
  RK_I18N_HANDLING_TOOLTIP,
  RK_I18N_ORDER_TOOLTIP,
  RK_PLANNED_DATE,
  RK_I18N_PLANNED_DATE,
  RK_I18N_PLANNED_DATE_TOOLTIP
} from './constants';

/**
 * Returns an obj containing all table rows to be rendered with HBS
 */
export const _buildTableRows = (data, keys) => {
  const dataObject = {
    row: []
  };

  let icon = '';
  if(data[RK_TYPE_CODE].startsWith('A')) {
    icon = 'Mandatory_Kit';
  } else if(data[RK_TYPE_CODE].startsWith('B')) {
    icon = 'Trolley';
  }
  keys.forEach((key, index) => {
    const value = key === RK_ICON ? icon : data[key];

    if (key === RK_SERIAL_NUMBER) {
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

  const mapKeysToHeaderObj = (i18nKey, i18nToolTipKey) => {
    const toolTipText = i18nKeys[i18nToolTipKey];
    headerObj['keyLabel'] = i18nKeys[i18nKey];
    if (i18nToolTipKey && toolTipText && toolTipText.trim().length > 0) {
      headerObj['showTooltip'] = true;
      headerObj['tooltipText'] = toolTipText;
    } else {
      headerObj['showTooltip'] = false;
      headerObj['tooltipText'] = '';
    }
  };

  switch (key) {
    case RK_COUNTRY_CODE: {
      mapKeysToHeaderObj(RK_I18N_COUNTRY_CODE,RK_I18N_COUNTRY_CODE_TOOLTIP);
      break;
    }
    case RK_LINE_CODE: {
      mapKeysToHeaderObj(RK_I18N_LINE_CODE,RK_I18N_LINE_CODE_TOOLTIP);
      break;
    }
    case RK_EQ_DESC: {
      mapKeysToHeaderObj(RK_I18N_EQ_DESC,RK_I18N_EQ_DESC_TOOLTIP);
      break;
    }
    case RK_MACHINE_SYSTEM: {
      mapKeysToHeaderObj(RK_I18N_MACHINE_SYSTEM,RK_I18N_MACHINE_SYSTEM_TOOLTIP);
      break;
    }
    case RK_SERIAL_NUMBER: {
      mapKeysToHeaderObj(RK_I18N_SERIAL_NUMBER,RK_I18N_SERIAL_NUMBER_TOOLTIP);
      break;
    }
    case RK_EQ_STATUS: {
      mapKeysToHeaderObj(RK_I18N_EQ_STATUS,RK_I18N_EQ_STATUS_TOOLTIP);
      break;
    }
    case RK_NUMBER: {
      mapKeysToHeaderObj(RK_I18N_NUMBER,RK_I18N_NUMBER_TOOLTIP);
      break;
    }
    case RK_DESC: {
      mapKeysToHeaderObj(RK_I18N_DESC,RK_I18N_DESC_TOOLTIP);
      break;
    }
    case RK_IMPL_STATUS: {
      mapKeysToHeaderObj(RK_I18N_IMPL_STATUS,RK_I18N_IMPL_STATUS_TOOLTIP);
      break;
    }
    case RK_IMPL_DATE: {
      mapKeysToHeaderObj(RK_I18N_IMPL_DATE,RK_I18N_IMPL_DATE_TOOLTIP);
      break;
    }
    case RK_IMPL_STATUS_DATE: {
      mapKeysToHeaderObj(RK_I18N_IMPL_STATUS_DATE,RK_I18N_IMPL_STATUS_DATE_TOOLTIP);
      break;
    }
    case RK_GENERAL_NUMBER: {
      mapKeysToHeaderObj(RK_I18N_GENERAL_NUMBER,RK_I18N_GENERAL_NUMBER_TOOLTIP);
      break;
    }
    case RK_TYPE_CODE: {
      mapKeysToHeaderObj(RK_I18N_TYPE_CODE,RK_I18N_TYPE_CODE_TOOLTIP);
      break;
    }
    case RK_RELEASE_DATE: {
      mapKeysToHeaderObj(RK_I18N_RELEASE_DATE,RK_I18N_RELEASE_DATE_TOOLTIP);
      break;
    }
    case RK_IMPL_DEADLINE: {
      mapKeysToHeaderObj(RK_I18N_IMPL_DEADLINE,RK_I18N_IMPL_DEADLINE_TOOLTIP);
      break;
    }
    case RK_PLANNED_DATE: {
      mapKeysToHeaderObj(RK_I18N_PLANNED_DATE,RK_I18N_PLANNED_DATE_TOOLTIP);
      break;
    }
    case RK_STATUS: {
      mapKeysToHeaderObj(RK_I18N_STATUS,RK_I18N_STATUS_TOOLTIP);
      break;
    }
    case RK_HANDLING: {
      mapKeysToHeaderObj(RK_I18N_HANDLING,RK_I18N_HANDLING_TOOLTIP);
      break;
    }
    case RK_ORDER: {
      mapKeysToHeaderObj(RK_I18N_ORDER,RK_I18N_ORDER_TOOLTIP);
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
) => keys.map((key) => {
  const keyMap = _getKeyMap(key, i18nKeys);
  return {
    key,
    i18nKey: keyMap.keyLabel,
    showTooltip: keyMap.showTooltip,
    tooltipText: keyMap.toolTipText
  };
});