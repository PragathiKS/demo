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

class RebuildingKits {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};

  initCache() {
    this.cache.rkApi = this.root.find('.js-rk-api');
    this.cache.skipIndex = 0;
    this.cache.activePage = 1;
    this.cache.itemsPerPage = 25;
    this.cache.countryData = [];
    this.cache.configJson = this.root.find('.js-rk__config').text();
    this.cache.i18nKeys = JSON.parse(this.cache.configJson);
    this.cache.tableData = [];
    this.cache.customisableTableHeaders = [];
    this.cache.tableHeaders = RK_PROPERTY_KEYS;
    this.cache.$spinner = this.root.find('.tp-spinner');
    this.cache.$content = this.root.find('.tp-rk-content');
    this.cache.$searchResults = this.root.find('.tp-rk__search-count');
    this.cache.$pagination = this.root.find('.js-tbl-pagination');
    this.cache.$modal = this.root.parent().find('.js-filter-modal');
    this.cache.filterModalData = {};
    this.cache.$rkCustomizeTableAction = this.root.find('.js-rk__customise-table-action');
    this.cache.activeFilterForm = 'country';
    this.cache.combinedFiltersObj = {};
    this.cache.downloadservletUrl = this.root.find('#downloadCsvServletUrl').val();

    this.cache.$countryFilterLabel = this.root.find('.tp-rk__country-button-filter');

    this.cache.$functionalLocationFilterLabel = this.root.find('.tp-rk__functionalLocation-filter');
    this.cache.$equipmentDescriptionFilterLabel = this.root.find('.tp-rk__equipmentDescription-filter');
    this.cache.$serialNumberFilterLabel = this.root.find('.tp-rk__serialNumber-filter');
    this.cache.$rkNumberFilterLabel = this.root.find('.tp-rk__rkNumber-filter');
    this.cache.$rkDescFilterLabel = this.root.find('.tp-rk__rkDesc-filter');
    this.cache.$implStatusFilterLabel = this.root.find('.tp-rk__implStatus-filter');
    this.cache.$machineSystemFilterLabel = this.root.find('.tp-rk__machineSystem-filter');
    this.cache.$equipmentStatusFilterLabel = this.root.find('.tp-rk__equipmentStatus-filter');
    this.cache.$rkTypeFilterLabel = this.root.find('.tp-rk__rkType-filter');
    this.cache.$rkStatusFilterLabel = this.root.find('.tp-rk__rkStatus-filter');
    this.cache.$rkHandlingFilterLabel = this.root.find('.tp-rk__rkHandling-filter');
    this.cache.$implDeadlineFilterLabel = this.root.find('.tp-rk__implDeadline-filter');
    this.cache.$plannedDateFilterLabel = this.root.find('.tp-rk__plannedDate-filter');
    this.cache.$releaseDateFilterLabel = this.root.find('.tp-rk__releaseDate-filter');
    this.cache.$generalRkNumberFilterLabel = this.root.find('.tp-rk__generalRkNumber-filter');

    this.cache.$mobileHeadersActions = this.root.find('.js-mobile-header-actions');
    this.cache.$showHideAllFiltersBtn = this.root.find('.js-tp-rk__show-hide-all-button');
    this.cache.$removeAllFiltersBtn = this.root.find('.js-tp-rk__remove-all-button');
    this.cache.$activeFilterBtn = {};

    this.cache.allApiFilterValsObj = this.getFilterValObj();
    this.cache.currentApiFilterValsObj = { ...this.cache.allApiFilterValsObj};
  }

  getFilterValObj = () => {
    const filterValObj = {};
    RK_API_FILTER_KEYS.forEach(key => filterValObj[key] = []);
    return filterValObj;
  }

  processTableData = (data) => {
    if (Array.isArray(data.summary)) {
      data.summary = data.summary.map((summary) => _buildTableRows.call(this, summary, this.cache.tableHeaders));
      data.summaryHeadings = _mapHeadings(this.cache.tableHeaders, data.i18nKeys);
    }
    return data;
  }

  mapTableColumn = () => {
    const { tableHeaders, customisableTableHeaders } = this.cache;
    for (let i = 0; i < tableHeaders.length; i++) {
      if (customisableTableHeaders[i]) {
        customisableTableHeaders[i] = {
          ...customisableTableHeaders[i],
          colIndex: i
        };
      }
    }
  };

  renderSearchCount = () => {
    this.cache.$searchResults.text(
      `${this.cache.meta.total} ${getI18n(
        this.cache.i18nKeys['searchResults']
      )}`
    );
  };

  getFilterModalData(filterByProperty) {
    const { combinedFiltersObj, allApiFilterValsObj, currentApiFilterValsObj } = this.cache;
    const alphabeticalSortKey = 'option';
    const optionDisplayTextKey = filterByProperty;
    const optionValueKey = filterByProperty;
    const filterOptionsArr = [];
    let filterOptionsDatasource = [];
    const allAvailableApiFilterCheckboxes = [...allApiFilterValsObj[_remapFilterProperty(filterByProperty)]];
    const currentSelectionApiFilterCheckboxes = [...currentApiFilterValsObj[_remapFilterProperty(filterByProperty)]];

    if (Object.keys(combinedFiltersObj).length === 0) {
      filterOptionsDatasource = allAvailableApiFilterCheckboxes;
    } else {
      filterOptionsDatasource = currentSelectionApiFilterCheckboxes;
    }

    filterOptionsDatasource.forEach((row) => {
      filterOptionsArr.push({
        option: row[optionValueKey],
        optionDisplayText: row[optionDisplayTextKey],
        isChecked: combinedFiltersObj[filterByProperty] ? combinedFiltersObj[filterByProperty].includes(row[optionValueKey]) : false,
        businessType: row['businessType'] ? row['businessType'] : null
      });
    });

    filterOptionsArr.sort(function(a, b) {
      return a[alphabeticalSortKey].localeCompare(b[alphabeticalSortKey]);
    });

    return filterOptionsArr;
  }

  renderPaginationTableData = (list) => {
    const paginationObj = _paginate(
      list.meta.total,
      this.cache.activePage,
      this.cache.itemsPerPage,
      3
    );

    if (list.summary.length === 0) {
      render.fn({
        template: 'rebuildingkitsTable',
        data: {
          noDataMessage: true,
          noDataFound: this.cache.i18nKeys['noDataFound']
        },
        target: '.tp-rk__table_wrapper',
        hidden: false
      });
    } else {
      render.fn(
        {
          template: 'rebuildingkitsTable',
          data: {
            ...list,
            summary: list.summary,
            paginationObj: paginationObj
          },
          target: '.tp-rk__table_wrapper',
          hidden: false
        },
        () => {
          this.hideShowColumns();
          $(function () {
            $('[data-toggle="tooltip"]').tooltip();
          });
        }
      );
    }
  };

  hideShowColumns = () => {
    const { customisableTableHeaders } = this.cache;
    for(const i in customisableTableHeaders){
      if(!customisableTableHeaders[i].isChecked){
        $(`.js-rk__table-summary__cellheading--${customisableTableHeaders[i].index}`).addClass('hide');
        $(`.js-rk__table-summary__cell--${customisableTableHeaders[i].index}`).addClass('hide');
      } else {
        $(`.js-rk__table-summary__cellheading--${customisableTableHeaders[i].index}`).removeClass('hide');
        $(`.js-rk__table-summary__cell--${customisableTableHeaders[i].index}`).removeClass('hide');
      }
    }
  }

  renderDefaultCountry = () => {
    this.cache.$spinner.removeClass('d-none');
    const countryApi = this.cache.rkApi.data('country-api');
    const rkApi = this.cache.rkApi.data('rklist-api');
    auth.getToken(({ data: authData }) => {
      ajaxWrapper
        .getXhrObj({
          url: countryApi,
          method: ajaxMethods.GET,
          cache: true,
          dataType: 'json',
          contentType: 'application/json',
          beforeSend(jqXHR) {
            jqXHR.setRequestHeader(
              'Authorization',
              `Bearer ${authData.access_token}`
            );
            jqXHR.setRequestHeader(
              'Content-Type',
              'application/x-www-form-urlencoded'
            );
          },
          showLoader: true
        })
        .then((res) => {
          this.cache.countryData = _getFormattedCountryData(res.data);
          this.cache.authData = authData;
          const { countryCode } = this.cache.countryData && this.cache.countryData[0];
          const { itemsPerPage } = this.cache;

          this.getAllAvailableFilterVals(RK_API_FILTER_KEYS, true);

          ajaxWrapper
            .getXhrObj({
              url: `${rkApi}?skip=0&count=${itemsPerPage}&countrycodes=${countryCode}&sort=lineCode asc,rkGeneralNumber asc,position asc`,
              method: 'GET',
              contentType: 'application/json',
              dataType: 'json',
              beforeSend(jqXHR) {
                jqXHR.setRequestHeader(
                  'Authorization',
                  `Bearer ${authData.access_token}`
                );
                jqXHR.setRequestHeader(
                  'Content-Type',
                  'application/x-www-form-urlencoded'
                );
              },
              showLoader: true
            })
            .then((response) => {
              this.cache.$spinner.addClass('d-none');
              this.cache.$content.removeClass('d-none');
              this.cache.tableData = response.data;
              this.cache.tableData = this.cache.tableData.map((item) => ({
                ...item
              }));
              this.cache.meta = response.meta;
              this.cache.countryData.splice(0, 1, {
                ...this.cache.countryData[0],
                isChecked: true
              });
              const tableData = this.processTableData({
                summary: this.cache.tableData,
                i18nKeys: this.cache.i18nKeys,
                meta: this.cache.meta
              });
              this.renderPaginationTableData(tableData);
              this.renderSearchCount();
              this.mapTableColumn();
            })
            .fail(() => {
              this.cache.$content.removeClass('d-none');
              this.cache.$spinner.addClass('d-none');
            });
        });
    });
  };

  renderNewPage = ({resetSkip}) => {
    const {itemsPerPage, activeSortData, combinedFiltersObj} = this.cache;

    const rkApi = this.cache.rkApi.data('rklist-api');
    let apiUrlRequest = '';
    const countryCode = this.getActiveCountryCode();
    const filtersQuery = _buildQueryUrl(combinedFiltersObj);
    const skipIndex = resetSkip ? 0 : this.cache.skipIndex;

    this.cache.$content.addClass('d-none');
    this.cache.$spinner.removeClass('d-none');

    if (resetSkip) {
      this.cache.activePage = 1;
      this.cache.skipIndex = 0;
    }

    apiUrlRequest = `${rkApi}?skip=${skipIndex}&count=${itemsPerPage}&countrycodes=${countryCode}&sort=lineCode asc,rkGeneralNumber asc,position asc`;

    if (filtersQuery) {
      apiUrlRequest += `&${filtersQuery}`;
    }

    if (activeSortData) {
      let sortingParam = `${activeSortData.sortedByKey.toLowerCase()} ${activeSortData.sortOrder}`;

      if (activeSortData.sendPosition) {
        sortingParam = `${activeSortData.sortedByKey.toLowerCase()} ${activeSortData.sortOrder},position`;
      }

      apiUrlRequest += `&sort=${sortingParam}`;
    }

    auth.getToken(({ data: authData, analyticsAction }) => {
      ajaxWrapper
        .getXhrObj({
          url: apiUrlRequest,
          method: 'GET',
          contentType: 'application/json',
          dataType: 'json',
          beforeSend(jqXHR) {
            jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
            jqXHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
          },
          showLoader: true
        }).then(response => {
          this.cache.$spinner.addClass('d-none');
          this.cache.$content.removeClass('d-none');
          this.cache.tableData = response.data;
          this.cache.tableData = this.cache.tableData.map((item) => ({
            ...item
          }));
          this.cache.meta = response.meta;
          const tableData = this.processTableData({
            summary:this.cache.tableData,
            i18nKeys:this.cache.i18nKeys,
            meta:this.cache.meta
          });
          this.renderPaginationTableData(tableData);
          this.renderSearchCount();
          this.mapTableColumn();

          if (analyticsAction && analyticsAction.action === 'removedFilter') {
            _removeFilterAnalytics(analyticsAction.targetFilter, response.meta.total);
          }

          if (analyticsAction && analyticsAction.action === 'removedAllFilters') {
            _removeAllFiltersAnalytics(analyticsAction.items, response.meta.total);
          }

          if (analyticsAction && analyticsAction.action === 'addedFilter') {
            _addFilterAnalytics(analyticsAction.targetFilter, response.meta.total, analyticsAction.items);
          }
        }).fail(() => {
          this.cache.$content.removeClass('d-none');
          this.cache.$spinner.addClass('d-none');
        });
    });
  }

  renderFilterForm = (data, formDetail, $filterBtn) => {
    const { i18nKeys } = this.cache;
    let selectedItemsNo = 0;

    if (formDetail.header === i18nKeys['country']) {
      data.forEach((item,index) => {
        data[index]= {
          ...item,
          optionDisplayText: item.option,
          isCountry:true
        };
      });
    }

    if (formDetail.maxFiltersSelection) {
      selectedItemsNo = data.filter(item => item.isChecked).length;
    }

    render.fn({
      template: 'filterForm',
      data: {
        header: formDetail.header,
        formData: data,
        maxItemsNo: formDetail.maxFiltersSelection ? formDetail.maxFiltersSelection : null,
        selectedItemsNo: selectedItemsNo ? selectedItemsNo : 0,
        isEquipmentType: formDetail.header === i18nKeys['equipmentType'],
        ...i18nKeys,
        singleButton: formDetail.singleButton === true,
        customiseTable: formDetail.activeForm === 'customise-table',
        isRadio: formDetail.isRadio === true ? true : false,
        radioGroupName: formDetail.radioGroupName,
        isTextInput: formDetail.isTextInput,
        isDatePicker: formDetail.isDatePicker,
        autoLocatorModal: `${formDetail.activeForm}Overlay`,
        autoLocatorInput: `${formDetail.activeForm}InputBox`,
        autoLocatorCheckbox: `${formDetail.activeForm}FilterCheckboxOverlay`,
        autoLocatorCheckboxText: `${formDetail.activeForm}FilterItemOverlay`
      },
      target: '.tp-rk__filter-form',
      hidden: false
    });

    if (['implDeadline', 'plannedDate', 'releaseDate'].includes(formDetail.activeForm)) {
      renderDatePicker({
        el: $('.js-dp'),
        type: 'range',
        onCorrectValue: this.handleDatePickerChange,
        onIncorrectValue: this.handleDatePickerChange,
        onEmptyValue: this.handleDatePickerChange,
        startDate: data.startDate,
        endDate: data.endDate
      });
    }

    this.cache.activeFilterForm = formDetail.activeForm;
    this.cache.$activeFilterBtn = $filterBtn;
  }

  handleDatePickerChange= (dateFrom, dateTo) => {
    const isDateFromValid = dateFrom instanceof Date;
    const isDateToValid = dateTo instanceof Date;

    if (isDateFromValid && isDateToValid) {
      this.enableFilterApplyBtn();
    } else {
      this.disableFilterApplyBtn();
    }
  }

  applyFilter = (options) => {
    const { activeFilterForm, $activeFilterBtn, i18nKeys } = this.cache;
    const $filtersCheckbox = this.root.find('.js-tp-my-equipment-filter-checkbox:not(.js-tp-my-equipment-filter-group-checkbox)');
    const $filtersRadio = this.root.find('.js-tp-my-equipment-filter-radio');
    const $freeTextFilterInput = this.root.find('.js-tp-my-equipment-filter-input');
    const $dateFromInput = this.root.find('.js-dp-input-from');
    const $dateToInput = this.root.find('.js-dp-input-to');
    const dateFromValue = $dateFromInput.val();
    const dateToValue = $dateToInput.val();

    let filterCount = 0;
    let filterData = [];
    let analyticsAction = {};
    let label;

    switch (activeFilterForm) {
      case 'country':{
        filterData = this.cache.countryData;
        $filtersRadio.each(function(index) {
          if ($(this).is(':checked')) {
            filterCount++;
            filterData[index].isChecked = true;
          } else {
            filterData[index].isChecked = false;
          }
        });

        label = i18nKeys['country'];
        break;
      }
      case 'lineCode': {
        filterCount = this.addCombinedFilter(activeFilterForm, $filtersCheckbox);
        label = i18nKeys['functionalLocation'];
        break;
      }
      case 'equipmentDescription': {
        this.cache.combinedFiltersObj['equipmentdesc'] = $freeTextFilterInput.val();
        filterCount = $freeTextFilterInput.val() !== '' ? 1 : 0;
        label = i18nKeys['equipmentDescription'];
        break;
      }
      case 'serialNumber': {
        this.cache.combinedFiltersObj['serialNumber'] = $freeTextFilterInput.val();
        filterCount = $freeTextFilterInput.val() !== '' ? 1 : 0;
        label = i18nKeys['serialNumber'];
        break;
      }
      case 'rkNumber': {
        this.cache.combinedFiltersObj['rkNumber'] = $freeTextFilterInput.val();
        filterCount = $freeTextFilterInput.val() !== '' ? 1 : 0;
        label = i18nKeys['rkNumber'];
        break;
      }
      case 'rkDesc': {
        this.cache.combinedFiltersObj['rkDesc'] = $freeTextFilterInput.val();
        filterCount = $freeTextFilterInput.val() !== '' ? 1 : 0;
        label = i18nKeys['rkDesc'];
        break;
      }
      case 'rkTypeCode': {
        filterCount = this.addCombinedFilter(activeFilterForm, $filtersCheckbox);
        label = i18nKeys['rkType'];
        break;
      }
      case 'rebuildingKitStatus': {
        filterCount = this.addCombinedFilter(activeFilterForm, $filtersCheckbox);
        label = i18nKeys['rkStatus'];
        break;
      }
      case 'implDeadline': {
        this.cache.combinedFiltersObj['impldeadlinestart'] = dateFromValue;
        this.cache.combinedFiltersObj['impldeadlineend'] = dateToValue;

        filterCount = dateFromValue && dateToValue ? 1 : 0;
        label = i18nKeys['implDeadline'];
        break;
      }
      case 'plannedDate': {
        this.cache.combinedFiltersObj['planneddatestart'] = dateFromValue;
        this.cache.combinedFiltersObj['planneddateend'] = dateToValue;

        filterCount = dateFromValue && dateToValue ? 1 : 0;
        label = i18nKeys['plannedDate'];
        break;
      }
      case 'releaseDate': {
        this.cache.combinedFiltersObj['releasedatestart'] = dateFromValue;
        this.cache.combinedFiltersObj['releasedateend'] = dateToValue;

        filterCount = dateFromValue && dateToValue ? 1 : 0;
        label = i18nKeys['releaseDate'];
        break;
      }
      case 'rkGeneralNumber': {
        this.cache.combinedFiltersObj['rkGeneralNumber'] = $freeTextFilterInput.val();
        filterCount = $freeTextFilterInput.val() !== '' ? 1 : 0;
        label = i18nKeys['generalRkNumber'];
        break;
      }

      case 'customise-table':{
        this.onCustomizeTableColumn($filtersCheckbox, options);
        break;
      }

      default: {
        filterCount = this.addCombinedFilter(activeFilterForm, $filtersCheckbox);
        label = i18nKeys[activeFilterForm];
        break;
      }
    }

    if (options && options.removeFilter) {
      switch (activeFilterForm) {
        case 'implDeadline': {
          this.deleteFilterValue('impldeadlinestart');
          this.deleteFilterValue('impldeadlineend');
          break;
        }
        case 'plannedDate': {
          this.deleteFilterValue('planneddatestart');
          this.deleteFilterValue('planneddateend');
          break;
        }
        case 'releaseDate': {
          this.deleteFilterValue('releasedatestart');
          this.deleteFilterValue('releasedateend');
          break;
        }
        default: {
          this.deleteFilterValue(activeFilterForm);
          break;
        }
      }

      filterCount = null;
    }

    if ($activeFilterBtn) {
      if (filterCount) {
        $activeFilterBtn.addClass('active');
      } else {
        $activeFilterBtn.removeClass('active');
      }
    }

    analyticsAction = {
      action: options && options.removeFilter ? 'removedFilter' : 'addedFilter',
      targetFilter: $activeFilterBtn,
      items: this.cache.combinedFiltersObj[activeFilterForm]
    };

    switch (activeFilterForm) {
      case 'customise-table': {
        const tableData = this.processTableData({ summary:this.cache.tableData,i18nKeys:this.cache.i18nKeys,meta:this.cache.meta });
        this.renderPaginationTableData(tableData);
        this.cache.$modal.modal('hide');
        break;
      }
      case 'country': {
        analyticsAction.items = this.getActiveCountryCode();
        this.renderNewCountry(label, analyticsAction);
        this.cache.$modal.modal('hide');
        break;
      }
      default: {
        this.updateFilterCountValue(label,filterCount,$activeFilterBtn);
        this.renderNewPage({'resetSkip': true, analyticsAction});
        this.getAllAvailableFilterVals(RK_API_FILTER_KEYS, false, activeFilterForm);
        this.cache.$modal.modal('hide');
        this.toggleRemoveAllFilters(true);
        break;
      }
    }
  }

  onCustomizeTableColumn = ($filtersCheckbox, options) => {
    let filterData = [];
    filterData = this.cache.customisableTableHeaders;
    if (options && options.removeFilter) {
      // reset default
      $filtersCheckbox.each(function(index) {
        $(this).prop('checked', filterData[index].isChecked);
      });
    }
    $filtersCheckbox.each(function(index) {
      if ($(this).is(':checked')) {
        filterData[index].isChecked = true;
      } else {
        filterData[index].isChecked = false;
      }
    });
  }

  deleteFilterValue = (key) => {
    if (this.cache.combinedFiltersObj[key]) {
      delete this.cache.combinedFiltersObj[key];
    }
  }

  renderNewCountry = (label, analyticsAction) => {
    const { $countryFilterLabel, itemsPerPage } = this.cache;
    const rkApi = this.cache.rkApi.data('rklist-api');
    this.cache.$spinner.removeClass('d-none');
    this.cache.$content.addClass('d-none');

    auth.getToken(({ data: authData }) => {
      this.getAllAvailableFilterVals(RK_API_FILTER_KEYS, true);
      ajaxWrapper
        .getXhrObj({
          url: `${rkApi}?skip=0&count=${itemsPerPage}&countrycodes=${this.getActiveCountryCode()}&sort=lineCode asc,rkGeneralNumber asc,position asc`,
          method: 'GET',
          contentType: 'application/json',
          dataType: 'json',
          beforeSend(jqXHR) {
            jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
            jqXHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
          },
          showLoader: true
        }).then(response => {
          this.cache.$spinner.addClass('d-none');
          this.cache.$content.removeClass('d-none');
          this.cache.tableData = response.data;
          this.cache.meta = response.meta;
          this.cache.activePage = 1;
          this.cache.tableData = this.cache.tableData.map((item) => ({
            ...item,
            equipmentStatus: item.equipmentStatus || ''
          }));

          this.deleteAllFilters();
          const tableData = this.processTableData({
            summary: this.cache.tableData,
            i18nKeys: this.cache.i18nKeys,
            meta: this.cache.meta
          });
          this.renderPaginationTableData(tableData);
          this.renderSearchCount();
          this.updateFilterCountValue(label,1,$countryFilterLabel);

          if (analyticsAction && analyticsAction.action === 'addedFilter') {
            _addFilterAnalytics(analyticsAction.targetFilter, response.meta.total, analyticsAction.items);
          }
        }).fail(() => {
          this.cache.$spinner.addClass('d-none');
          this.cache.$content.removeClass('d-none');
        });
    });
  }

  toggleRemoveAllFilters = (show) => {
    if (show && Object.keys(this.cache.combinedFiltersObj).length > 0) {
      this.cache.$removeAllFiltersBtn.removeAttr('hidden');
    } else {
      this.cache.$removeAllFiltersBtn.attr('hidden', 'hidden');
    }
  }

  getAllAvailableFilterVals(filterValuesArr, newCountry, appliedFilter) {
    const rkApi = this.cache.rkApi.data('rklist-api');
    const { combinedFiltersObj } = this.cache;

    filterValuesArr.forEach(filterVal => {
      const appliedFilterApiKey = _remapFilterProperty(appliedFilter);

      let apiUrlRequest = `${rkApi}/${filterVal}?countrycodes=${this.getActiveCountryCode()}`;

      if (!newCountry) {
        apiUrlRequest += `&${_buildQueryUrl(combinedFiltersObj, filterVal)}`;
      }

      auth.getToken(({ data: authData }) => {
        ajaxWrapper
          .getXhrObj({
            url: apiUrlRequest,
            method: 'GET',
            contentType: 'application/json',
            dataType: 'json',
            beforeSend(jqXHR) {
              jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
              jqXHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            },
            showLoader: true
          }).then(res => {
            if (newCountry) {
              this.cache.allApiFilterValsObj[filterVal] = res.data;
            } else {
              this.cache.currentApiFilterValsObj[filterVal] = res.data;
              if (appliedFilterApiKey !== filterVal) {
                this.checkActiveFilterSets(filterVal, res.data);
              }
            }
            const rkStatuses = [];
            if (filterVal === 'rkstatuses') {
              res.data.forEach(function (data) {
                for (const [key, value] of Object.entries(data)) {
                  if (key === 'rebuildingKitStatus') {
                    data['rkStatus'] = value;
                  }
                }
                rkStatuses.push(data);
              });
              this.cache.allApiFilterValsObj[filterVal] = rkStatuses;
            }
          });
      });
    });
  }

  checkActiveFilterSets = (filterVal) => {
    const { combinedFiltersObj, currentApiFilterValsObj } = this.cache;

    if (Object.keys(combinedFiltersObj).length === 0) {
      return;
    }

    Object.keys(combinedFiltersObj).forEach(enabledFilter => {
      if (combinedFiltersObj[enabledFilter].length && filterVal === _remapFilterProperty(enabledFilter)) {
        const filterPropertyRemap = _remapFilterProperty(enabledFilter);
        const activeFilterItemsArr = currentApiFilterValsObj[filterPropertyRemap];

        const availableFiltersInDataSet = activeFilterItemsArr.map(item => item[_remapFilterOptionKey(enabledFilter)]);

        if (combinedFiltersObj[enabledFilter].length > availableFiltersInDataSet.length) {
          combinedFiltersObj[enabledFilter] = availableFiltersInDataSet;
          this.updateFilterBtnCount(enabledFilter, availableFiltersInDataSet.length);
        }
      }
    });
  }

  updateFilterBtnCount = (filterProperty, filterCount) => {
    let label;
    let $btnElem;
    const { i18nKeys } = this.cache;

    switch (filterProperty) {
      case 'lineCode': {
        label = i18nKeys['line'];
        $btnElem = this.cache.$lineFilterLabel;
        break;
      }
      default: {
        break;
      }
    }

    $btnElem.text(`${getI18n(label)}: ${filterCount}`);
  }

  addCombinedFilter = (activeFilterForm, $filtersCheckbox) => {
    if ($filtersCheckbox.filter(':checked').length > 0) {
      this.cache.combinedFiltersObj[activeFilterForm] = [];
      $filtersCheckbox.each((index, item) => {
        if ($(item).is(':checked')) {
          this.cache.combinedFiltersObj[activeFilterForm].push($(item).val());
        }
      });
      return this.cache.combinedFiltersObj[activeFilterForm].length;
    } else {
      delete this.cache.combinedFiltersObj[activeFilterForm];
      return 0;
    }
  }

  updateFilterCountValue = (label,filterCount,htmlUpdate) => {
    const { $modal } = this.cache;

    if (htmlUpdate) {
      if (!filterCount) {
        htmlUpdate.text(`${getI18n(label)} +`);
      } else {
        htmlUpdate.text(`${getI18n(label)}: ${filterCount}`);
      }
    }
    $modal.modal('hide');
  }

  downloadCsv = () => {
    auth.getToken(() => {
      const url = this.cache.downloadservletUrl;
      file.get({
        extension: 'csv',
        url: `${url}?countrycodes=${this.getActiveCountryCode()}`,
        method: ajaxMethods.GET
      });
    });
  }

  getActiveCountryCode = () => {
    try {
      const { countryData } = this.cache;
      const activeCountry = countryData.find(e => e.isChecked);
      if (activeCountry) {
        return activeCountry.countryCode;
      } else {
        throw Error('Couldn\'t get active country');
      }
    } catch (err) {
      logger.error(err.message);
    }
  }

  showHideAllFilters = () => {
    const $allBtnFilters = this.root.find('.tp-rk__filter-button-all');
    const showLabel = this.cache.$showHideAllFiltersBtn.data('show-label');
    const hideLabel = this.cache.$showHideAllFiltersBtn.data('hide-label');
    const $label = this.cache.$showHideAllFiltersBtn.find('.tpatom-btn__text');
    const currentLabel = $label.text().trim();

    _hideShowAllFiltersAnalytics(currentLabel, currentLabel === showLabel ? 'filterShow' : 'filterHide');

    $allBtnFilters.toggle();
    $label.text(currentLabel === showLabel ? hideLabel : showLabel);
  }

  deleteAllFilters = () => {
    const $filterBtns = this.root.find('.tp-rk__filter-button:not(.tp-rk__country-button-filter)');
    // if no filters are active
    if (Object.keys(this.cache.combinedFiltersObj).length === 0) {
      return;
    }

    const analyticsAction = {
      action: 'removedAllFilters',
      targetFilter: null,
      items: this.cache.combinedFiltersObj
    };

    this.cache.combinedFiltersObj = {};
    this.cache.currentApiFilterValsObj = this.getFilterValObj();

    this.cache.activeSortData = null;

    $filterBtns.each((_index, item) => {
      const initialLabel = $(item).data('label');
      $(item).removeClass('active');
      $(item).text(initialLabel);
    });

    this.renderNewPage({'resetSkip': true, analyticsAction});
  }

  enableFilterApplyBtn = () => {
    $('.js-apply-filter-button').prop('disabled', false);
  }

  disableFilterApplyBtn = () => {
    $('.js-apply-filter-button').prop('disabled', true);
  }

  setDefaultTableHeaders = () => {
    this.cache.customisableTableHeaders = [
      {key:RK_COUNTRY_CODE,option:RK_COUNTRY_CODE,optionDisplayText:this.cache.i18nKeys[RK_I18N_COUNTRY_CODE],isChecked:false,index:1,isDisabled:false},
      {key:RK_LINE_CODE,option:RK_LINE_CODE,optionDisplayText:this.cache.i18nKeys[RK_I18N_LINE_CODE],isChecked:true,index:2,isDisabled:false},
      {key:RK_EQ_DESC,option:RK_EQ_DESC,optionDisplayText:this.cache.i18nKeys[RK_I18N_EQ_DESC],isChecked:true,index:3,isDisabled:false},
      {key:RK_MACHINE_SYSTEM,option:RK_MACHINE_SYSTEM,optionDisplayText:this.cache.i18nKeys[RK_I18N_MACHINE_SYSTEM],isChecked:false,index:4,isDisabled:false},
      {key:RK_SERIAL_NUMBER,option:RK_SERIAL_NUMBER,optionDisplayText:this.cache.i18nKeys[RK_I18N_SERIAL_NUMBER],isChecked:true,index:5,isDisabled:true},
      {key:RK_EQ_STATUS,option:RK_EQ_STATUS,optionDisplayText:this.cache.i18nKeys[RK_I18N_EQ_STATUS],isChecked:false,index:6,isDisabled:false},
      {key:RK_NUMBER,option:RK_NUMBER,optionDisplayText:this.cache.i18nKeys[RK_I18N_NUMBER],isChecked:true,index:7,isDisabled:true},
      {key:RK_DESC,option:RK_DESC,optionDisplayText:this.cache.i18nKeys[RK_I18N_DESC],isChecked:true,index:8,isDisabled:false},
      {key:RK_IMPL_STATUS,option:RK_IMPL_STATUS,optionDisplayText:this.cache.i18nKeys[RK_I18N_IMPL_STATUS],isChecked:true,index:9,isDisabled:false},
      {key:RK_IMPL_DATE,option:RK_IMPL_DATE,optionDisplayText:this.cache.i18nKeys[RK_I18N_IMPL_DATE],isChecked:false,index:10,isDisabled:false},
      {key:RK_IMPL_STATUS_DATE,option:RK_IMPL_STATUS_DATE,optionDisplayText:this.cache.i18nKeys[RK_I18N_IMPL_STATUS_DATE],isChecked:false,index:11,isDisabled:false},
      {key:RK_GENERAL_NUMBER,option:RK_GENERAL_NUMBER,optionDisplayText:this.cache.i18nKeys[RK_I18N_GENERAL_NUMBER],isChecked:false,index:12,isDisabled:false},
      {key:RK_TYPE_CODE,option:RK_TYPE_CODE,optionDisplayText:this.cache.i18nKeys[RK_I18N_TYPE_CODE],isChecked:false,index:13,isDisabled:false},
      {key:RK_RELEASE_DATE,option:RK_RELEASE_DATE,optionDisplayText:this.cache.i18nKeys[RK_I18N_RELEASE_DATE],isChecked:false,index:14,isDisabled:false},
      {key:RK_PLANNED_DATE,option:RK_PLANNED_DATE,optionDisplayText:this.cache.i18nKeys[RK_I18N_PLANNED_DATE],isChecked:false,index:15,isDisabled:false},
      {key:RK_IMPL_DEADLINE,option:RK_IMPL_DEADLINE,optionDisplayText:this.cache.i18nKeys[RK_I18N_IMPL_DEADLINE],isChecked:false,index:16,isDisabled:false},
      {key:RK_STATUS,option:RK_STATUS,optionDisplayText:this.cache.i18nKeys[RK_I18N_STATUS],isChecked:false,index:17,isDisabled:false},
      {key:RK_HANDLING,option:RK_HANDLING,optionDisplayText:this.cache.i18nKeys[RK_I18N_HANDLING],isChecked:false,index:18,isDisabled:false},
      {key:RK_ORDER,option:RK_ORDER,optionDisplayText:this.cache.i18nKeys[RK_I18N_ORDER],isChecked:false,index:19,isDisabled:false}
    ];
  }

  bindEvents = () => {
    const $this = this;
    const {$modal,i18nKeys,$rkCustomizeTableAction, $mobileHeadersActions } = this.cache;
    this.setDefaultTableHeaders();
    const getNOfOptions = (keyCode) => {
      const data = this.cache.filterModalData[keyCode];
      if (data) {
        return data.length;
      }
      return 10;
    };

    $modal.on('shown.bs.modal', () => {
      const $datePickerInput = $('.js-dp-input-from');
      $datePickerInput.length && $datePickerInput.trigger('focus');
    });

    this.cache.$countryFilterLabel.on('click', () => {
      const formDetail = { activeForm:'country',header:i18nKeys['country'], singleButton: true, isRadio: true, radioGroupName: 'countryRadio' };
      this.renderFilterForm(this.cache.countryData, formDetail, this.cache.$countryFilterLabel);
      $modal.modal();
    });

    this.cache.$functionalLocationFilterLabel.on('click', () => {
      this.cache.filterModalData['lineCode'] = this.getFilterModalData('lineCode');
      const formDetail = {activeForm:'lineCode',header:i18nKeys['functionalLocation'],maxFiltersSelection:getNOfOptions('lineCode')};
      this.renderFilterForm(this.cache.filterModalData['lineCode'], formDetail, this.cache.$functionalLocationFilterLabel);
      $modal.modal();
    });

    this.cache.$equipmentDescriptionFilterLabel.on('click', () => {
      const formDetail = {activeForm:'equipmentDescription',header:i18nKeys['equipmentDescription'], isTextInput: true};
      const currValue = this.cache.combinedFiltersObj['equipmentdesc'] ? this.cache.combinedFiltersObj['equipmentdesc'] : '';
      this.renderFilterForm(currValue, formDetail, this.cache.$equipmentDescriptionFilterLabel);
      $modal.modal();
    });

    this.cache.$serialNumberFilterLabel.on('click', () => {
      const formDetail = {activeForm:'serialNumber',header:i18nKeys['serialNumber'], isTextInput: true};
      const currValue = this.cache.combinedFiltersObj['serialNumber'] ? this.cache.combinedFiltersObj['serialNumber'] : '';
      this.renderFilterForm(currValue, formDetail, this.cache.$serialNumberFilterLabel);
      $modal.modal();
    });

    this.cache.$rkNumberFilterLabel.on('click', () => {
      const formDetail = {activeForm:'rkNumber',header:i18nKeys['rkNumber'], isTextInput: true};
      const currValue = this.cache.combinedFiltersObj['rkNumber'] ? this.cache.combinedFiltersObj['rkNumber'] : '';
      this.renderFilterForm(currValue, formDetail, this.cache.$rkNumberFilterLabel);
      $modal.modal();
    });

    this.cache.$rkDescFilterLabel.on('click', () => {
      const formDetail = {activeForm:'rkDesc',header:i18nKeys['rkDesc'], isTextInput: true};
      const currValue = this.cache.combinedFiltersObj['rkDesc'] ? this.cache.combinedFiltersObj['rkDesc'] : '';
      this.renderFilterForm(currValue, formDetail, this.cache.$rkDescFilterLabel);
      $modal.modal();
    });

    this.cache.$implStatusFilterLabel.on('click', () => {
      this.cache.filterModalData['implStatus'] = this.getFilterModalData('implStatus');
      const formDetail = {activeForm:'implStatus',header:i18nKeys['implStatus'],maxFiltersSelection:getNOfOptions('implStatus')};
      this.renderFilterForm(this.cache.filterModalData['implStatus'], formDetail, this.cache.$implStatusFilterLabel);
      $modal.modal();
    });

    this.cache.$machineSystemFilterLabel.on('click', () => {
      this.cache.filterModalData['machineSystem'] = this.getFilterModalData('machineSystem');
      const formDetail = {activeForm:'machineSystem',header:i18nKeys['machineSystem'],maxFiltersSelection:getNOfOptions('machineSystem')};
      this.renderFilterForm(this.cache.filterModalData['machineSystem'], formDetail, this.cache.$machineSystemFilterLabel);
      $modal.modal();
    });

    this.cache.$equipmentStatusFilterLabel.on('click', () => {
      this.cache.filterModalData['equipmentStatus'] = this.getFilterModalData('equipmentStatus');
      const formDetail = {activeForm:'equipmentStatus',header:i18nKeys['equipmentStatus'],maxFiltersSelection:getNOfOptions('equipmentStatus')};
      this.renderFilterForm(this.cache.filterModalData['equipmentStatus'], formDetail, this.cache.$equipmentStatusFilterLabel);
      $modal.modal();
    });

    this.cache.$rkTypeFilterLabel.on('click', () => {
      this.cache.filterModalData['rkTypeCode'] = this.getFilterModalData('rkTypeCode');
      const formDetail = {activeForm:'rkTypeCode',header:i18nKeys['rkType'],maxFiltersSelection:getNOfOptions('rkTypeCode')};
      this.renderFilterForm(this.cache.filterModalData['rkTypeCode'], formDetail, this.cache.$rkTypeFilterLabel);
      $modal.modal();
    });

    this.cache.$rkStatusFilterLabel.on('click', () => {
      this.cache.filterModalData['rkStatus'] = this.getFilterModalData('rkStatus');
      const formDetail = {activeForm:'rkStatus',header:i18nKeys['rkStatus'],maxFiltersSelection:getNOfOptions('rkStatus')};
      this.renderFilterForm(this.cache.filterModalData['rkStatus'], formDetail, this.cache.$rkStatusFilterLabel);
      $modal.modal();
    });

    this.cache.$rkHandlingFilterLabel.on('click', () => {
      this.cache.filterModalData['rkHandling'] = this.getFilterModalData('rkHandling');
      const formDetail = {activeForm:'rkHandling',header:i18nKeys['rkHandling'],maxFiltersSelection:getNOfOptions('rkHandling')};
      this.renderFilterForm(this.cache.filterModalData['rkHandling'], formDetail, this.cache.$rkHandlingFilterLabel);
      $modal.modal();
    });

    this.cache.$implDeadlineFilterLabel.on('click', () => {
      const formDetail = {activeForm:'implDeadline',header:i18nKeys['implDeadline'],isDatePicker: true};
      const currValue = {
        startDate: this.cache.combinedFiltersObj['impldeadlinestart'] || '',
        endDate: this.cache.combinedFiltersObj['impldeadlineend'] || ''
      };
      this.renderFilterForm(currValue, formDetail, this.cache.$implDeadlineFilterLabel);
      $modal.modal();
    });

    this.cache.$plannedDateFilterLabel.on('click', () => {
      const formDetail = {activeForm:'plannedDate',header:i18nKeys['plannedDate'],isDatePicker: true};
      const currValue = {
        startDate: this.cache.combinedFiltersObj['planneddatestart'] || '',
        endDate: this.cache.combinedFiltersObj['planneddateend'] || ''
      };
      this.renderFilterForm(currValue, formDetail, this.cache.$plannedDateFilterLabel);
      $modal.modal();
    });

    this.cache.$releaseDateFilterLabel.on('click', () => {
      const formDetail = {activeForm:'releaseDate',header:i18nKeys['releaseDate'],isDatePicker: true};
      const currValue = {
        startDate: this.cache.combinedFiltersObj['releasedatestart'] || '',
        endDate: this.cache.combinedFiltersObj['releasedateend'] || ''
      };
      this.renderFilterForm(currValue, formDetail, this.cache.$releaseDateFilterLabel);
      $modal.modal();
    });


    this.cache.$generalRkNumberFilterLabel.on('click', () => {
      const formDetail = {activeForm:'rkGeneralNumber',header:i18nKeys['generalRkNumber'], isTextInput: true};
      const currValue = this.cache.combinedFiltersObj['rkGeneralNumber'] ? this.cache.combinedFiltersObj['rkGeneralNumber'] : '';
      this.renderFilterForm(currValue, formDetail, this.cache.$generalRkNumberFilterLabel);
      $modal.modal();
    });


    this.cache.$removeAllFiltersBtn.on('click', () => {
      this.deleteAllFilters();
      this.toggleRemoveAllFilters(false);
    });

    this.root.on('change', '.js-tp-my-equipment-filter-group-checkbox', (e) => {
      const $currentTarget = $(e.target);
      const $modal = $currentTarget.parents('.tp-my-equipment__modal-content');
      const $currentTargetWrapper = $currentTarget.parents('.tp-my-equipment__type-group-option');
      const $checkboxGroupInputs = $currentTargetWrapper.next().find('.tpatom-checkbox__input').not(':disabled');
      $checkboxGroupInputs.each((index, item) => {
        $(item).prop('checked', $currentTarget.is(':checked'));
      });
      _limitFilterSelection($modal);
    });

    this.root.on('change', '.tp-my-equipment-group-filter-options .js-tp-my-equipment-filter-checkbox', (e) => {
      const $currentTarget = $(e.target);
      const $thisGroupAllWrapper = $currentTarget.parents('.tp-my-equipment-group-filter-options').prev();
      const $thisGroupAllCheckbox = $thisGroupAllWrapper.find('.js-tp-my-equipment-filter-group-checkbox');
      if (!$currentTarget.is(':checked')) {
        $thisGroupAllCheckbox.prop('checked', false);
      }
    });

    this.root.on('click', '.js-tp-my-equipment__remove-button',  () => {
      this.applyFilter({removeFilter:true});
    });

    // Limit selection of checkbox filters to a max number
    this.root.on('change', '.tp-my-equipment-filter-options .js-tp-my-equipment-filter-checkbox', (e) => {
      const $currentTarget = $(e.target);
      const $modal = $currentTarget.parents('.tp-my-equipment__modal-content');
      _limitFilterSelection($modal);
    });

    // Show/Hide Columns
    $rkCustomizeTableAction.on('click', () => {
      this.renderFilterForm(this.cache.customisableTableHeaders, { activeForm:'customise-table',header:i18nKeys['customizeTable'],singleButton:true });
      $('.tp-rk__header-actions').removeClass('show');
      $modal.modal();
      _customizeTableBtnAnalytics($rkCustomizeTableAction);
    });

    this.root.on('click', '.js-tp-rk__set-default-button',  () => {
      this.setDefaultTableHeaders();
      this.applyFilter({removeFilter:true});
    });

    $mobileHeadersActions.on('click', () => {
      if($('.tp-rk__header-actions').hasClass('show')){
        $('.tp-rk__header-actions').removeClass('show');
      } else {
        $('.tp-rk__header-actions').addClass('show');
      }
    });

    // Apply filters
    this.root.on('click', '.js-apply-filter-button',  () => {
      this.applyFilter();
    });

    // Apply filters on 'Enter'
    this.root.on('keydown', '.js-tp-my-equipment-filter-input',  (e) => {
      const currentKeyCode = e.keyCode || e.which;
      if (currentKeyCode === 13) {
        this.applyFilter();
      }
    });

    // Close modal
    this.root.on('click', '.js-close-btn',  () => {
      $modal.modal('hide');
    });

    // Pagination
    this.root.on('click', '.js-page-number',  (e) => {
      const $btn = $(e.currentTarget);
      const $pagination = $btn.parents('.js-tbl-pagination');

      if (!$btn.hasClass('active')) {
        // Stop pointer events until new page is rendered, in order to not send multiple AJAX calls
        $pagination.addClass('pagination-lock');
        this.cache.activePage = $btn.data('page-number');
        this.cache.skipIndex = $btn.data('skip');
        this.renderNewPage({'resetSkip': false});
        _paginationAnalytics($btn);
      }
    });

    // Redirect to RK Detail Page
    this.root.on('click', '.tp-rk__table-summary__row:not(".tp-rk__table-summary__rowheading")',  (e) => {
      const clickLink = $(e.currentTarget);
      const equipmentNumber = clickLink.find('.tpmol-table__key-equipmentNumber').text();
      const rkNumber = clickLink.find('.tpmol-table__key-rkNumber').text();
      const equipmentDetailsUrl = $this.cache.rkApi.data('rkdetail-page');
      window.open(`${equipmentDetailsUrl}?rkNumber=${rkNumber}&equipment=${equipmentNumber}`, '_blank');
    });

    // Download CSV
    this.root.on('click', '.js-rk__export-csv-action',  () => {
      this.downloadCsv();
    });

    this.cache.$showHideAllFiltersBtn.on('click', () => {
      this.showHideAllFilters();
    });
  };

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.renderDefaultCountry();
  }
}

export default RebuildingKits;
