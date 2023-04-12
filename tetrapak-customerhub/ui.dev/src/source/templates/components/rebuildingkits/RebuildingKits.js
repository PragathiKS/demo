import $ from 'jquery';
import 'bootstrap';
import auth from '../../../scripts/utils/auth';
import file from '../../../scripts/utils/file';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { ajaxMethods } from '../../../scripts/utils/constants';
import { _paginationAnalytics, _customizeTableBtnAnalytics } from './RebuildingKits.analytics';
import { _limitFilterSelection } from '../myequipment/MyEquipment';
import { _hideShowAllFiltersAnalytics , _addFilterAnalytics, _removeFilterAnalytics, _removeAllFiltersAnalytics} from '../myequipment/MyEquipment.analytics';
import { _remapFilterProperty, _buildQueryUrl } from './RebuildingKits.utils';
import { _getFormattedCountryData, _remapFilterOptionKey } from '../myequipment/MyEquipment.utils';
import { render } from '../../../scripts/utils/render';
import { logger } from '../../../scripts/utils/logger';
import { getI18n } from '../../../scripts/common/common';
import { _paginate } from './RebuildingKits.paginate';
import { _buildTableRows, _mapHeadings } from './RebuildingKits.table';
import {
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
  RK_PROPERTY_KEYS,
  RK_API_FILTER_KEYS,
  RK_PLANNED_DATE,
  RK_I18N_PLANNED_DATE
} from './constants';
import { renderDatePicker } from '../datepicker';

import FilteredTable from '../filteredtable/FilteredTable';
import config from './RebuildingKits.config';

class RebuildingKits extends FilteredTable {
  constructor({ el }) {
    super({
      el,
      config
    });
  }
}

export default RebuildingKits;
