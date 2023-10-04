import $ from 'jquery';
import 'bootstrap';
import auth from '../../../scripts/utils/auth';
import file from '../../../scripts/utils/file';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { ajaxMethods } from '../../../scripts/utils/constants';
import { logger } from '../../../scripts/utils/logger';
import { render } from '../../../scripts/utils/render';
import { getI18n } from '../../../scripts/common/common';
import { renderDatePicker } from '../datepicker';
import { _paginate } from './FilteredTable.paginate';
import {
  _paginationAnalytics,
  _customizeTableBtnAnalytics,
  _hideShowAllFiltersAnalytics,
  _addFilterAnalytics,
  _removeFilterAnalytics,
  _removeAllFiltersAnalytics
} from './FilteredTable.analytics';
import {
  REQUIRED_INIT_CACHE_KEYS,
  FILTER_TYPE_TEXT,
  FILTER_TYPE_RADIO,
  FILTER_TYPE_CHECKBOX,
  FILTER_TYPE_CHECKBOX_GROUP,
  FILTER_TYPE_DATE,
  FILTER_TYPE_DATE_RANGE,
  FILTER_TYPE_CUSTOMISE_TABLE,
  REMOVE_FILTER_ACTION,
  REMOVE_ALL_FILTERS_ACTION,
  ADD_FILTER_ACTION,
  HIDE_FILTERS_ACTION,
  SHOW_FILTERS_ACTION
} from './constants';


class FilteredTable {
  constructor({ el, config }) {
    this.root = $(el);
    this._initCache(config);
  }

  _downloadCsv = () => {
    const countryConfig = this._getCountryConfig();
    const countryCode = this.cache.variables.filters.values[countryConfig.queryParam];
    $(`.${this.cache.classes.mobileHeadersClass}`).removeClass('show');
    auth.getToken(() => {
      const url = this.cache.api.downloadServletUrl;
      file.get({
        extension: 'csv',
        url: `${url}?${countryConfig.queryParam}=${countryCode}`,
        method: ajaxMethods.GET
      });
    });
  }

  _getCountryConfig = () => this.cache.data.countryConfig

  _handleDatePickerChange= (dateFrom, dateTo) => {
    const isDateFromValid = dateFrom instanceof Date;
    const isDateToValid = dateTo instanceof Date;

    if (isDateFromValid && isDateToValid) {
      this._enableFilterApplyBtn();
    } else {
      this._disableFilterApplyBtn();
    }
  }

  _enableFilterApplyBtn = () => {
    $(`.${this.cache.classes.filterApplyClass}`).prop('disabled', false);
  }

  _disableFilterApplyBtn = () => {
    $(`.${this.cache.classes.filterApplyClass}`).prop('disabled', true);
  }

  _hideSpinner = () => {
    this.cache.elements.$spinner.addClass('d-none');
    this.cache.elements.$content.removeClass('d-none');
  }

  _showSpinner = (withDimBackground=true) => {
    this.cache.elements.$spinner.removeClass('d-none');
    if (withDimBackground) {
      this.cache.elements.$content.addClass('d-none');
    }
  }

  _toggleRemoveAllFilters = (show) => {
    if (show && Object.keys(this.cache.variables.filters.values).length > 0) {
      this.cache.elements.$removeAllFiltersBtn.removeAttr('hidden');
    } else {
      this.cache.elements.$removeAllFiltersBtn.attr('hidden', 'hidden');
    }
  }

  _deleteAllFilters = () => {
    const $filterBtns = this.cache.elements.$filtersWrapper.find('button');
    const countryConfig = this._getCountryConfig();
    const countryValue = this.cache.variables.filters.values[countryConfig.queryParam];
    const countryData = this.cache.variables.filters.data[countryConfig.apiKey];

    this.cache.variables.filters.values = {
      [countryConfig.queryParam]: countryValue
    };
    this.cache.variables.filters.data = {
      [countryConfig.apiKey]: countryData
    };
    this.cache.variables.sortData.sortedByKey = false;
    this.cache.variables.sortData.sortOrder = false;

    $filterBtns.each((_index, item) => {
      const initialLabel = $(item).data('label');
      $(item).removeClass('active');
      $(item).text(initialLabel);
    });

    this._render(false, false, {
      action: REMOVE_ALL_FILTERS_ACTION,
      items: this.cache.variables.filters.values
    });
  }

  _getCountries = async () => new Promise((resolve, reject) => {
    auth.getToken(async ({ data: authData }) => {
      ajaxWrapper
        .getXhrObj({
          url: this.cache.api.country,
          method: ajaxMethods.GET,
          cache: true,
          dataType: 'json',
          contentType: 'application/json',
          beforeSend(jqXHR) {
            jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
            jqXHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
          },
          showLoader: true
        }).then(res => {
          const countryConfig = this._getCountryConfig();
          const apiKey = countryConfig.apiKey;
          this.cache.variables.filters.data[apiKey] = res.data;
          const firstAvailableCountry = this.cache.variables.filters.data[apiKey][0];
          this.cache.variables.filters.values[countryConfig.queryParam] = firstAvailableCountry[countryConfig.key];
          resolve(firstAvailableCountry);
        }).catch(err => {
          reject(err);
        });
    });
  })

  _getAllAvailableFilterVals = async () => {
    const api = this.cache.api.list;
    return Promise.all(this.cache.data.filterDataEndpoints.map(endpoint => {
      let apiUrlRequest = `${api}/${endpoint}`;
      const config = this.cache.data.tableConfig.find(({ apiKey }) => apiKey === endpoint);
      const queryParam = config && config.queryParam;
      const filterUrlParams = this._getFilterUrlParams(queryParam);
      apiUrlRequest += filterUrlParams ? `?${filterUrlParams}` : '';

      return auth.getToken(({ data: authData }) => {
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
            this.cache.variables.filters.data[endpoint] = res.data;
          });
      });
    }));
  }

  _getFilterUrlParams = (queryParam) => {
    const { values } = this.cache.variables.filters;
    const queryParams = Object.keys(values);

    if (queryParams.length) {
      const queryString = this.cache.data.tableConfig.map((column) => {
        switch(column.filterType) {
          case FILTER_TYPE_DATE: {
            const dateStart = values[column.queryParam[0]];
            if (dateStart) {
              return `${column.queryParam[0]}=${dateStart}`;
            }
            return null;
          }
          case FILTER_TYPE_DATE_RANGE: {
            const dateStart = values[column.queryParam[0]];
            const dateEnd = values[column.queryParam[1]];
            if (dateStart && dateEnd) {
              return `${column.queryParam[0]}=${dateStart}&${column.queryParam[1]}=${dateEnd}`;
            }
            return null;
          }
          case FILTER_TYPE_CHECKBOX: {
            const value = values[column.queryParam];
            
            if (value && column.queryParam !== queryParam) {
              return `${column.queryParam}=${value.join(',')}`;
            } else {
              return null;
            }
          }
          default: {
            const value = values[column.queryParam];
            if (value) {
              return `${column.queryParam}=${value}`;
            } else {
              return null;
            }
          }
        }
      }).filter((val) => !!val).join('&');

      return queryString;
    }
    return '';
  };

  _resetPagination = () => {
    this.cache.variables.pagination.activePage = 1;
    this.cache.variables.pagination.skipIndex = 0;
  }

  _hideShowColums = () => {
    const { customisableTableHeaders } = this.cache;
    for(const i in customisableTableHeaders){
      if(!customisableTableHeaders[i].isChecked){
        $(`.js-equipment-list__table-summary__cellheading--${customisableTableHeaders[i].index}`).addClass('hide');
        $(`.js-equipment-list__table-summary__cell--${customisableTableHeaders[i].index}`).addClass('hide');
      } else {
        $(`.js-equipment-list__table-summary__cellheading--${customisableTableHeaders[i].index}`).removeClass('hide');
        $(`.js-equipment-list__table-summary__cell--${customisableTableHeaders[i].index}`).removeClass('hide');
      }
    }
  }

  _applyFiltersVisibility = (toggle=false) => {
    const $hiddenByDefaultButtons = this.cache.elements.$filtersWrapper.find(`.${this.cache.classes.hideFilterByDefaultClass}`);
    const showLabel = this.cache.elements.$showHideAllFiltersBtn.data('show-label');
    const hideLabel = this.cache.elements.$showHideAllFiltersBtn.data('hide-label');
    const $label = this.cache.elements.$showHideAllFiltersBtn.find('.tpatom-btn__text');
    const currentLabel = this.cache.variables.filters.showAllFilters ? hideLabel : showLabel;
    const action = this.cache.variables.filters.showAllFilters ? HIDE_FILTERS_ACTION : SHOW_FILTERS_ACTION;

    _hideShowAllFiltersAnalytics(currentLabel, action);

    if (toggle) {
      this.cache.variables.filters.showAllFilters = !this.cache.variables.filters.showAllFilters;
    }

    $hiddenByDefaultButtons.toggle(this.cache.variables.filters.showAllFilters);
    $label.text(this.cache.variables.filters.showAllFilters ? hideLabel : showLabel);
  }

  _focusDatePickerInput = () => {
    const $datePickerInput = $('.js-dp-input-from');
    $datePickerInput.length && $datePickerInput.trigger('focus');
  }

  _getRowIcon = (rkTypeCode) => {
    if(rkTypeCode.startsWith('A')) {
      return 'Mandatory_Kit';
    } else if(rkTypeCode.startsWith('B')) {
      return 'Trolley';
    }
    return '';
  }

  _buildTableRow = (data, visibleColumnKeys) => {
    const dataObject = {
      row: [
        {
          key: 'icon'
        }
      ]
    };
    const keys = ['icon', ...visibleColumnKeys];

    keys.forEach((key, index) => {
      const value = data[key];
      dataObject.row[index] = { key, value };

      if (key === 'serialNumber') {
        const permanentVolumeConversion = data['permanentVolumeConversion'];
        dataObject.row[index]['permanentVolumeConversion'] = permanentVolumeConversion;
      }
      if (data['id']) {
        dataObject.rowLink = data['id'];
        dataObject.isClickable = true;
      }
      if (key === 'rkTypeCode') {
        dataObject.row[0].value = this._getRowIcon(value);
      }
    });

    return dataObject;
  };

  _processTableData = (data) => {
    const result = { ...data };

    result.summary = data.summary.map((summary) => this._buildTableRow(
      summary,
      this.cache.variables.filters.visibleColumns
    ));
    result.summaryHeadings = this._mapHeadings(
      this.cache.variables.filters.visibleColumns
    );

    result.summaryHeadings.unshift({
      key: 'icon',
      showTooltip: false
    });

    return result;
  }

  _mapHeadings = (keys) => keys.map((key) => {
    const { i18nKeys, tableConfig } = this.cache.data;
    const columnData = tableConfig.find((item) => item.key === key);
    return {
      key,
      i18nKey: i18nKeys[columnData.i18nKey],
      showTooltip: !!i18nKeys[columnData.i18nTooltipKey],
      tooltipText: i18nKeys[columnData.i18nTooltipKey]
    };
  });

  _getActiveFilterConfig = () => {
    const { activeFilterKey } = this.cache.variables.filters;

    if (activeFilterKey === FILTER_TYPE_CUSTOMISE_TABLE) {
      return {
        filterType: FILTER_TYPE_CUSTOMISE_TABLE
      };
    }

    return this.cache.data.tableConfig.find(({ key }) => key === activeFilterKey);
  }

  _resetColumns = () => {
    this.cache.variables.filters.visibleColumns = this.cache.data.defaultVisibleColumns;

    const tableData = this._processTableData({
      summary: this.cache.variables.data,
      i18nKeys: this.cache.data.i18nKeys,
      meta: this.cache.variables.meta
    });
    this._renderPaginatedTable(tableData);
    this.cache.elements.$modal.modal('hide');
  }


  _applyFilter = () => {
    const _getFilterValue = () => {
      const activeFilterConfig = this._getActiveFilterConfig();

      switch (activeFilterConfig.filterType) {
        case FILTER_TYPE_RADIO: {
          const checked = Array.from($(`.${this.cache.classes.filterRadioClass}:checked`))[0];
          return checked.value;
        }
        case FILTER_TYPE_CUSTOMISE_TABLE:
        case FILTER_TYPE_CHECKBOX: {
          const checked = Array.from($(`.${this.cache.classes.filterCheckboxClass} input:checked`));
          return checked.map(el => el.value);
        }
        case FILTER_TYPE_DATE:
        case FILTER_TYPE_DATE_RANGE: {
          const dateFrom = $(`.${this.cache.classes.filterDPFromClass}`).val();
          const dateTo = $(`.${this.cache.classes.filterDPToClass}`).val();

          return [dateFrom, dateTo];
        }
        case FILTER_TYPE_TEXT:
        default: {
          return $(`.${this.cache.classes.filterInputClass}`).val();
        }
      }
    };

    const { values } = this.cache.variables.filters;
    const { $modal } = this.cache.elements;
    const activeFilterConfig = this._getActiveFilterConfig();
    const value = _getFilterValue();

    switch (activeFilterConfig.filterType) {
      case FILTER_TYPE_CUSTOMISE_TABLE: {
        this.cache.variables.filters.visibleColumns = value;
        break;
      }
      case FILTER_TYPE_DATE:
      case FILTER_TYPE_DATE_RANGE: {
        values[activeFilterConfig.queryParam[0]] = value[0];
        values[activeFilterConfig.queryParam[1]] = value[1];
        break;
      }
      default: {
        values[activeFilterConfig.queryParam] = value;
        break;
      }
    }

    switch (activeFilterConfig.filterType) {
      case FILTER_TYPE_CUSTOMISE_TABLE: {
        const tableData = this._processTableData({
          summary: this.cache.variables.data,
          i18nKeys: this.cache.data.i18nKeys,
          meta: this.cache.variables.meta
        });
        this._renderPaginatedTable(tableData);
        break;
      }
      default: {
        if (!activeFilterConfig.isCountry) {
          this._toggleRemoveAllFilters(true);
        }
        this._render(false, false, {
          action: ADD_FILTER_ACTION,
          targetFilter: $(this.cache.elements.$filters[this.cache.variables.filters.activeFilterKey]),
          items: this.cache.variables.filters.values
        });
        break;
      }
    }

    $modal.modal('hide');
  }


  _renderFilterForm = (data) => {
    const { i18nKeys } = this.cache.data;
    const { values } = this.cache.variables.filters;

    const _getValue = () => {
      switch (data.filterType) {
        case FILTER_TYPE_CHECKBOX:
        case FILTER_TYPE_CHECKBOX_GROUP: {
          return values[data.queryParam] || [];
        }
        case FILTER_TYPE_DATE_RANGE: {
          return [values[data.queryParam[0]], values[data.queryParam[1]]] || [];
        }
        default: {
          return values[data.queryParam] || '';
        }
      }
    };

    const countryConfig = this._getCountryConfig();
    const value = _getValue();
    const fetchedData = this.cache.variables.filters.data[data.apiKey];

    const maxItemsNo = [FILTER_TYPE_CHECKBOX_GROUP, FILTER_TYPE_CHECKBOX].includes(data.filterType) ?
      fetchedData.length : null;
    const selectedItemsNo = [FILTER_TYPE_CHECKBOX_GROUP, FILTER_TYPE_CHECKBOX].includes(data.filterType) ?
      value.length : null;
    const noRemoveButton = data.key === countryConfig.key;
    const header = i18nKeys[data.i18nKey];
    const isRadio = data.filterType === FILTER_TYPE_RADIO;
    const isTextInput = data.filterType === FILTER_TYPE_TEXT;
    const isDatePicker = [FILTER_TYPE_DATE, FILTER_TYPE_DATE_RANGE].includes(data.filterType);
    const isCheckboxGroup = data.filterType === FILTER_TYPE_CHECKBOX_GROUP;

    const target = this.cache.elements.$filterForm;

    const _getFormTemplateData = () => {
      switch (data.filterType) {
        case FILTER_TYPE_CHECKBOX_GROUP:
        case FILTER_TYPE_CHECKBOX: {
          return fetchedData.map((item) => {
            const key = item[data.key];
            const displayKey = item[data.displayKey || data.key];
            return {
              option: key,
              optionDisplayText: displayKey,
              isChecked: value.includes(key)
            };
          });
        }
        case FILTER_TYPE_RADIO: {
          return fetchedData.map((item) => {
            const key = item[data.key];
            const displayKey = item[data.displayKey || data.key];
            return {
              option: key,
              optionDisplayText: displayKey,
              isChecked: value === key
            };
          });
        }
        case FILTER_TYPE_DATE:
        case FILTER_TYPE_DATE_RANGE:
        case FILTER_TYPE_TEXT:
        default: {
          return value;
        }
      }
    };

    render.fn({
      template: 'filteredTableForm',
      data: {
        header,
        formData: _getFormTemplateData(),
        maxItemsNo,
        selectedItemsNo,
        isCheckboxGroup,
        isRadio,
        isTextInput,
        isDatePicker,
        noRemoveButton,
        radioGroupName: `${data.key}Group`,
        autoLocatorModal: `${data.key}Overlay`,
        autoLocatorInput: `${data.key}InputBox`,
        autoLocatorCheckbox: `${data.key}FilterCheckboxOverlay`,
        autoLocatorCheckboxText: `${data.key}FilterItemOverlay`,
        ...i18nKeys
      },
      target,
      hidden: false
    });

    if (isDatePicker) {
      renderDatePicker({
        el: $('.js-dp'),
        type: data.filterType === FILTER_TYPE_DATE_RANGE ? 'range' : 'default',
        onCorrectValue: this._handleDatePickerChange,
        onIncorrectValue: this._handleDatePickerChange,
        onEmptyValue: this._handleDatePickerChange,
        startDate: value[0],
        endDate: value[1]
      });
    }

    this.cache.variables.filters.activeFilterKey = data.key;
  }

  _renderCustomiseTableForm = () => {
    const { i18nKeys } = this.cache.data;
    const target = this.cache.elements.$filterForm;

    const _getFormTemplateData = () => this.cache.data.tableConfig.map((row) => row.key === 'equipmentNumber' ? null: ({
      option: row.key,
      optionDisplayText: this.cache.data.i18nKeys[row.i18nKey],
      isChecked: this.cache.variables.filters.visibleColumns.includes(row.key),
      isDisabled: row.showColumnDisabled
    }));

    this.cache.variables.filters.activeFilterKey = FILTER_TYPE_CUSTOMISE_TABLE;

    render.fn({
      template: 'filteredTableForm',
      data: {
        header: i18nKeys['customizeTable'],
        formData: _getFormTemplateData(),
        noRemoveButton: true,
        isCustomiseTable: true,
        radioGroupName: 'customiseTableGroup',
        autoLocatorModal: 'customiseTableOverlay',
        autoLocatorCheckbox: 'customiseTableFilterCheckboxOverlay',
        autoLocatorCheckboxText: 'customiseTableFilterItemOverlay',
        ...i18nKeys
      },
      target,
      hidden: false
    });

    $(`.${this.cache.classes.mobileHeadersClass}`).removeClass('show');
    this.cache.elements.$modal.modal();
  }

  _renderPaginatedTable = (list) => {
    const paginationObj = _paginate(
      list.meta.total,
      this.cache.variables.pagination.activePage,
      this.cache.variables.pagination.itemsPerPage,
      3
    );

    if (list.summary.length === 0) {
      render.fn({
        template: 'filteredTableTable',
        data: {
          noDataMessage: true,
          noDataFound: this.cache.data.i18nKeys['noDataFound']
        },
        target: this.cache.elements.$tableWrapper,
        hidden: false
      });
    }
    else {
      render.fn({
        template: 'filteredTableTable',
        data: {
          ...list,
          paginationObj: paginationObj
        },
        target: this.cache.elements.$tableWrapper,
        hidden: false
      },() => {
        this._hideShowColums();
        $(function () {
          $('[data-toggle="tooltip"]').tooltip();
        });
      });
    }
  }

  _renderSearchCount = () => {
    this.cache.elements.$searchResults.text(
      `${this.cache.variables.meta.total} ${getI18n(
        this.cache.data.i18nKeys['searchResults']
      )}`
    );
  };

  _renderFilters = () => {
    const target = this.cache.elements.$filtersWrapper;
    const filters = this.cache.data.tableConfig
      .filter((item) => !!item.queryParam)
      .map((item) => {
        if(item.key !== 'equipmentNumber') {
          const _getFilterCount = () => {
            const { values } = this.cache.variables.filters;
            const value = values[item.queryParam];
  
            switch (item.filterType) {
              case FILTER_TYPE_TEXT:
              case FILTER_TYPE_RADIO: {
                return value ? 1 : 0;
              }
              case FILTER_TYPE_CHECKBOX:
              case FILTER_TYPE_CHECKBOX_GROUP: {
                return value ? value.length : 0;
              }
              case FILTER_TYPE_DATE: {
                const dateStart = values[item.queryParam[0]];
                return dateStart ? 1 : 0;
              }
              case FILTER_TYPE_DATE_RANGE: {
                const dateStart = values[item.queryParam[0]];
                const dateEnd = values[item.queryParam[1]];
                return dateStart && dateEnd ? 1 : 0;
              }
              default: {
                break;
              }
            }
          };
          const filterCount = _getFilterCount();
          const showFilterClass = item.showFilterByDefault ?
            '' : this.cache.classes.hideFilterByDefaultClass;
          const activeClass = filterCount ?
            ' active' : '';
          const className = `${showFilterClass}${activeClass}`;
  
          return {
            label: getI18n(this.cache.data.i18nKeys[item.i18nKey]),
            class: className,
            key: item.key,
            filterCount
          };
        }
      });

    render.fn({
      template: 'filteredTableFilters',
      data: { filters },
      target,
      hidden: false
    }, () => {
      filters.forEach((filter) => {
        const $filter = this.cache.elements.$filtersWrapper.find(`button[data-key=${filter.key}]`);
        this.cache.elements.$filters[filter.key] = $filter;
      });
    });
  }

  _renderWrapper = () => {
    const { i18nKeys } = this.cache.data;
    const target = this.cache.elements.$contentRoot;
    render.fn({
      template: 'filteredTableContent',
      data: i18nKeys,
      target,
      hidden: false
    }, () => {
      this.cache.elements = {
        ...this.cache.elements,
        $pagination: this.root.find('.js-tbl-pagination'),
        $modal: this.root.parent().find('.js-filter-modal'),
        $mobileHeadersActions: this.root.find('.js-mobile-header-actions'),
        $content: this.root.find('.tp-filtered-table__content'),
        $filterForm: this.root.find('.tp-filtered-table__filter-form'),
        $filtersWrapper: this.root.find('.tp-filtered-table__filter-chips'),
        $customiseTableBtn: this.root.find('.js-filtered-table__customise-table-action'),
        $tableWrapper: this.root.find('.tp-filtered-table__table_wrapper'),
        $searchResults: this.root.find('.tp-filtered-table__search-count'),
        $showHideAllFiltersBtn: this.root.find('.js-tp-filtered-table__show-hide-all-button'),
        $removeAllFiltersBtn: this.root.find('.js-tp-filtered-table__remove-all-button')
      };
    });
  }

  _getApiUrlRequest = () => {
    const { skipIndex, itemsPerPage } = this.cache.variables.pagination;
    const api = this.cache.api.list;
    const { sortedByKey, sortOrder, sendPosition } = this.cache.variables.sortData;
    const filterUrlParams = this._getFilterUrlParams();
    const skipIndexParam = `skip=${skipIndex}`;
    const countParam = `&count=${itemsPerPage}`;
    const filtersQuery = filterUrlParams ? `&${filterUrlParams}` : '';
    const sortPosition = sendPosition ? ',position' : '';
    const sortParam = sortedByKey ? `&${sortedByKey.toLowerCase()} ${sortOrder}${sortPosition}` : '';

    const apiUrlRequest = `${api}?${skipIndexParam}${countParam}${sortParam}${filtersQuery}`;

    return apiUrlRequest;
  }

  async _render(isFirstRender=false, resetPagination=false, analyticsAction={}) {
    this._showSpinner(!isFirstRender);

    if (isFirstRender) {
      await this._getCountries();
    }

    if (resetPagination) {
      this._resetPagination();
    }

    await this._getAllAvailableFilterVals();

    return auth.getToken(({ data: authData }) => {
      ajaxWrapper
        .getXhrObj({
          url: this._getApiUrlRequest(),
          method: 'GET',
          contentType: 'application/json',
          dataType: 'json',
          beforeSend(jqXHR) {
            jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
            jqXHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
          },
          showLoader: true
        }).then((response) => {
          this._hideSpinner();
          this.cache.variables.meta = response.meta;
          this.cache.variables.data = response.data;

          const tableData = this._processTableData({
            summary: this.cache.variables.data,
            i18nKeys: this.cache.data.i18nKeys,
            meta: this.cache.variables.meta
          });
          this._renderPaginatedTable(tableData);
          this._renderSearchCount();
          this._renderFilters();
          this._applyFiltersVisibility();

          switch (analyticsAction.action) {
            case REMOVE_FILTER_ACTION: {
              _removeFilterAnalytics(
                analyticsAction.targetFilter,
                response.meta.total
              );
              break;
            }
            case REMOVE_ALL_FILTERS_ACTION: {
              _removeAllFiltersAnalytics(
                analyticsAction.items,
                response.meta.total
              );
              break;
            }
            case ADD_FILTER_ACTION: {
              _addFilterAnalytics(
                analyticsAction.targetFilter,
                response.meta.total,
                analyticsAction.items
              );
              break;
            }
            default: {
              break;
            }
          }

        }).fail(() => {
          this._showSpinner();
        });
    });
  }

  _initCache = (config) => {
    try {
      if (config) {
        REQUIRED_INIT_CACHE_KEYS.forEach((key) => {
          if (!config[key]) {
            throw new Error(`Missing key '${key}' in FilteredTable config.`);
          }
        });

        const {
          apiSelector,                // String, '.js-equipment-list-api'
          configSelector,            // String, '.js-equipment-list__config'
          contentSelector,          // String, '.tp-rk'
          listApiKey,              // String, 'rklist-api'
          countryApiKey,          // String, 'country-api'
          detailsApiKey,         // String, 'rkdetail-page'
          downloadServletUrlId, // String, '#downloadCsvServletUrl'
          tableConfig          // Array of objects describing how to view each column in the list (object structure below)
        } = config;
        /*
          The "tableConfig" schema:
          [{                                                             Value ///////////////// Description
              //////////////////////////////////////////////////////////////////////////
              index: 0,                                                              // Number (place in order of columns)
              key: RK_COUNTRY_CODE,                                                 // String (object property key in the API response)
              textKey: RK_I18N_COUNTRY_CODE,                                       // String (i18n key code)
              tooltipTextKey: RK_I18N_COUNTRY_CODE_TOOLTIP,                       // String (i18n key code)
              showColumnDisabled: false,                                         // Boolean (should column view be disabled for editting)
              showColumnByDefault: false,                                       // Boolean (should column be visible by default)
              showFilterByDefault: true                                        // Should the filter chip be hidden before "show all filters" button is clicked
              filterType: 'radio'                                             // 'text' | 'radio' | 'checkbox-group' | 'checkbox' | 'date' | 'date-range'
              apiKey: 'country',                                             // String (the path of the API endpoint used to get filter data)
              queryParam: 'countrycodes',                                   // String (the key used in the API GET requests as query parameter, 1x2 array if FILTER_TYPE_DATE_RANGE filterType)
              isCountry: true                                              // Property indicates whether above data should be used in country setting manipulation
              //////////////////////////////////////////////////////////////
          }]
        */

        const $api = this.root.find(apiSelector);
        const $downloadServletUrl = $(downloadServletUrlId);
        const defaultVisibleColumns = tableConfig.filter((item) => item.showColumnByDefault).map((item) => item.key);

        this.cache = {
          api: {
            list: $api.data(listApiKey),
            country: $api.data(countryApiKey),
            details: $api.data(detailsApiKey),
            downloadServletUrl: $downloadServletUrl && $downloadServletUrl.val()
          },
          classes: {
            hideFilterByDefaultClass: 'js-hide-filter-by-default',
            filterApplyClass: 'js-apply-filter-button',
            filterInputClass: 'js-tp-filtered-table-filter-input',
            filterRadioClass: 'js-tp-filtered-table-filter-radio',
            filterCheckboxClass: 'js-tp-filtered-table-filter-checkbox',
            filterDPFromClass: 'js-dp-input-from',
            filterDPToClass: 'js-dp-input-to',
            filterRemoveClass: 'js-tp-filtered-table__remove-button',
            filterCloseClass: 'js-close-btn',
            exportCsvClass: 'js-export-btn',
            mobileHeadersClass: 'tp-filtered-table__header-actions',
            itemRowClass: 'tp-filtered-table__table-summary__row:not(".tp-filtered-table__table-summary__rowheading")',
            resetColumnsClass: 'js-tp-filtered-table__set-default-button',
            rkNumberClass: 'tpmol-table__key-rkNumber',
            equipmentNumberClass: 'tpmol-table__key-equipmentNumber',
            paginationNumberClass: 'js-page-number',
            paginationClass: 'js-tbl-pagination',
            paginationLockClass: 'pagination-lock'
          },
          elements: {
            $contentRoot: this.root.find(contentSelector),
            $spinner: this.root.find('.tp-spinner'),
            $content: null,
            $filterForm: null,
            $filtersWrapper: null,
            $customiseTableBtn: null,
            $tableWrapper: null,
            $searchResults: null,
            $filters: {}
          },
          data: {
            tableConfig,
            countryConfig: tableConfig.find((item) => item.isCountry),
            i18nKeys: JSON.parse(this.root.find(configSelector).text()),
            filterDataEndpoints: [...new Set(tableConfig.filter((item) => !!item.apiKey && !item.isCountry).map((item) => item.apiKey))],
            defaultVisibleColumns
          },
          variables: {
            meta: {},
            data: [],
            filters: {
              activeFilterKey: '',
              showAllFilters: false,
              values: {},
              data: {},
              visibleColumns: defaultVisibleColumns
            },
            sortData: {
              sortedByKey: false,
              sortOrder: false,
              sendPosition: false
            },
            pagination: {
              skipIndex: 0,
              activePage: 1,
              itemsPerPage: 25
            }
          }
        };

        if (typeof this.initCache === 'function') {
          this.initCache.call(this);
        }
      } else {
        throw new Error('Error: No config provided for the FilteredTable initialization');
      }
    } catch (err) {
      logger.error(err.message);
    }
  }

  _bindFilterFormButtonEvents = () => {
    this.cache.data.tableConfig.filter((column) => !!column.filterType).forEach((column) => {
      this.cache.elements.$filtersWrapper.on('click', `button[data-key=${column.key}]`, () => {
        const $btn = this.cache.elements.$filters[column.key];
        this._renderFilterForm({
          ...column,
          filterType: column.filterType || FILTER_TYPE_TEXT
        }, $btn);
        this.cache.elements.$modal.modal();
      });
    });
  }

  _bindShowModalEvent = () => {
    this.cache.elements.$modal.on('shown.bs.modal', () => {
      this._focusDatePickerInput();
    });
  }

  _bindRemoveAllFiltersEvent = () => {
    this.cache.elements.$removeAllFiltersBtn.on('click', () => {
      this._deleteAllFilters();
      this._toggleRemoveAllFilters(false);
    });
  }

  _bindToggleShowAllFiltersEvent = () => {
    this.cache.elements.$showHideAllFiltersBtn.on('click', () => {
      this._applyFiltersVisibility(true);
    });
  }

  _bindFilterFormCloseEvent = () => {
    this.root.on('click', `.${this.cache.classes.filterCloseClass}`,  () => {
      this.cache.elements.$modal.modal('hide');
    });
  }

  _bindFilterFormApplyEvent = () => {
    this.root.on('click', `.${this.cache.classes.filterApplyClass}`,  () => {
      this._applyFilter();
    });
  }

  _bindFilterFormEnterKeydownEvent = () => {
    this.root.on('keydown', `.${this.cache.classes.filterInputClass}`,  (e) => {
      const currentKey = e.key;
      if (currentKey === 'Enter') {
        this._applyFilter();
      }
    });
  }

  _bindDownloadCsvEvent = () => {
    this.root.on('click', `.${this.cache.classes.exportCsvClass}`,  () => {
      this._downloadCsv();
    });
  }

  _bindCustomiseTableEvent = () => {
    this.cache.elements.$customiseTableBtn.on('click',  () => {
      this._renderCustomiseTableForm();
      _customizeTableBtnAnalytics(this.cache.elements.$customiseTableBtn);
    });
  }

  _removeFilter = (filterKey) => {
    const filterConfig = this.cache.data.tableConfig.find(({ key }) => key === filterKey);
    switch(filterConfig.filterType) {
      case FILTER_TYPE_DATE:
      case FILTER_TYPE_DATE_RANGE: {
        filterConfig.queryParam.forEach((param) => {
          delete this.cache.variables.filters.values[param];
        });
        break;
      }
      default: {
        delete this.cache.variables.filters.values[filterConfig.queryParam];
        break;
      }
    }
    this.cache.elements.$modal.modal('hide');
    this._render(false, false, {
      action: REMOVE_FILTER_ACTION,
      targetFilter: $(this.cache.elements.$filters[filterKey])
    });
  }

  _bindRemoveFilterEvent = () => {
    this.root.on('click', `.${this.cache.classes.filterRemoveClass}`, () => {
      this._removeFilter(this.cache.variables.filters.activeFilterKey);
    });
  }

  _bindMobileDialogShowEvent = () => {
    this.cache.elements.$mobileHeadersActions.on('click', () => {
      if($(`.${this.cache.classes.mobileHeadersClass}`).hasClass('show')){
        $(`.${this.cache.classes.mobileHeadersClass}`).removeClass('show');
      } else {
        $(`.${this.cache.classes.mobileHeadersClass}`).addClass('show');
      }
    });
  }

  _bindRedirectEvent = () => {
    this.root.on('click', `.${this.cache.classes.itemRowClass}`,  (e) => {
      const clickLink = $(e.currentTarget);
      const rkNumber = clickLink.find(`.${this.cache.classes.rkNumberClass}`).text();
      const equipmentNumber = clickLink.find(`.${this.cache.classes.equipmentNumberClass}`).text();
      const equipmentDetailsUrl = this.cache.api.details;
      window.open(`${equipmentDetailsUrl}?rkNumber=${rkNumber}&equipment=${equipmentNumber}`, '_blank');
    });
  }

  _bindChangePageEvent = () => {
    this.root.on('click', `.${this.cache.classes.paginationNumberClass}`,  (e) => {
      const $btn = $(e.currentTarget);
      const $pagination = $btn.parents(`.${this.cache.classes.paginationClass}`);

      if (!$btn.hasClass('active')) {
        $pagination.addClass(this.cache.classes.paginationLockClass);
        this.cache.variables.pagination = {
          ...this.cache.variables.pagination,
          activePage: $btn.data('page-number'),
          skipIndex: $btn.data('skip')
        };
        this._render();
        _paginationAnalytics($btn);
      }
    });
  }

  _bindResetColumns = () => {
    this.root.on('click', `.${this.cache.classes.resetColumnsClass}`, () => {
      this._resetColumns();
    });
  }

  _bindEvents = () => {
    this._bindFilterFormButtonEvents();       // <-- Open filter form on the filter chip click
    this._bindShowModalEvent();               // <-- Execute callback on modal render
    this._bindRemoveAllFiltersEvent();        // <-- Remove all filter selections on the button click
    this._bindRemoveFilterEvent();            // <-- Remove filter selection on the button click
    this._bindToggleShowAllFiltersEvent();    // <-- Limit the list of available filter chips on the button click
    this._bindFilterFormApplyEvent();         // <-- Apply filters on apply button click
    this._bindFilterFormEnterKeydownEvent();  // <-- Apply filters on the keystroke "Enter"
    this._bindFilterFormCloseEvent();         // <-- Close filter form on the close icon click
    this._bindDownloadCsvEvent();             // <-- Download csv file with table contents on button click
    this._bindMobileDialogShowEvent();        // <-- Expand mobile dialog on button click
    this._bindRedirectEvent();                // <-- Redirect to details page on row click
    this._bindCustomiseTableEvent();          // <-- Open customise table filter form on the icon click
    this._bindChangePageEvent();              // <-- Change page on button click
    this._bindResetColumns();                 // <-- Reset column settings to default on reset CTA click

    if (typeof this.bindEvents === 'function') {
      this.bindEvents.call(this);
    }
  }

  init() {
    this._renderWrapper();    // <-- Render DOM elements based on the "filteredTableContent.hbs" template
    this._render(true);       // <-- Get countries list, get available filter options, get list data and render the table
    this._bindEvents();
  }
}

export default FilteredTable;
