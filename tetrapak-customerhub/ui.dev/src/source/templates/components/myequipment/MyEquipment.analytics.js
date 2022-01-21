import { trackAnalytics } from '../../../scripts/utils/analytics';
import { getI18n } from '../../../scripts/common/common';

export const _hideShowAllFiltersAnalytics = (label, eventType) => {
  const eventObj = {
    event: 'Filter',
    eventType: eventType
  };

  const linkClickObj = {
    linkName: label,
    linkType: 'internal',
    linkSection: 'button click',
    linkParentTitle: 'Text Hyperlink_My Equipment'
  };

  trackAnalytics(linkClickObj, 'linkClick', 'linkClick', undefined, false, eventObj);
};

export const _addFilterAnalytics = ($activeFilterBtn, searchResults, filterValues) => {
  const addedFilter = $activeFilterBtn.data('label').replace(' +', '');
  const eventObj = {
    event: 'Filter',
    eventType: 'search filter applied'
  };

  const searchObj = {
    searchResults: searchResults,
    searchFilters: `${addedFilter}=${filterValues.toString()}`
  };

  trackAnalytics(searchObj, 'search', 'internalFilter', undefined, false, eventObj);
};

export const _removeFilterAnalytics = ($activeFilterBtn, searchResults) => {
  const removedFilter = $activeFilterBtn.data('label').replace(' +', '');
  const eventObj = {
    event: 'Filter',
    eventType: 'search filter removed'
  };

  const searchObj = {
    searchResults: searchResults,
    searchFilters: removedFilter
  };

  trackAnalytics(searchObj, 'search', 'internalFilter', undefined, false, eventObj);
};

export const _removeAllFiltersAnalytics = (items, searchResults) => {
  const eventObj = {
    event: 'Filter',
    eventType: 'search filter removed'
  };

  const searchObj = {
    searchResults: searchResults,
    searchFilters: Object.keys(items).toString()
  };

  trackAnalytics(searchObj, 'search', 'internalFilter', undefined, false, eventObj);
};

export const _paginationAnalytics = ($targetBtn) => {
  let linkName;
  const pageNum = $targetBtn.data('page-number');

  const eventObj = {
    event: 'Pagination',
    eventType: 'paginationClick'
  };

  if ($targetBtn.hasClass('tp-tbl-pagination__last')) {
    linkName = 'last page';
  }

  if ($targetBtn.hasClass('tp-tbl-pagination__first')) {
    linkName = 'first page';
  }

  if ($targetBtn.hasClass('tp-tbl-pagination__prev')) {
    linkName = `Navigation_prev_${pageNum}`;
  }

  if ($targetBtn.hasClass('tp-tbl-pagination__next')) {
    linkName = `Navigation_next_${pageNum}`;
  }

  if ($targetBtn.hasClass('tp-tbl-pagination__direct')) {
    linkName = `Direct_${pageNum}`;
  }

  const linkClickObj = {
    linkName: linkName,
    linkType: 'internal',
    linkSection: 'hyperlink click',
    linkParentTitle: 'Text Hyperlink_My Equipment'
  };

  trackAnalytics(linkClickObj, 'linkClick', 'paginationClick', undefined, false, eventObj);
};

export const _customizeTableBtnAnalytics = ($targetBtn) => {
  const eventObj = {
    event: 'Customize Table',
    eventType: 'Show/Hide Columns'
  };

  const linkClickObj = {
    linkName: $targetBtn.find('.tp-my-equipment__header-action-text').text().replace(' +', ''),
    linkType: 'internal',
    linkSection: 'hyperlink click',
    linkParentTitle: 'Text Hyperlink_My Equipment'
  };

  trackAnalytics(linkClickObj, 'linkClick', 'paginationClick', undefined, false, eventObj);
};

export const _addShowHideFilterAnalytics = (filterData) => {
  const selectedCols = filterData.filter(colItem => colItem.isChecked);
  const selectedColsLabels = selectedCols.map(colItem => getI18n(colItem.optionDisplayText, null, null));
  const eventObj = {
    event: 'Customize Table',
    eventType: 'Show/Hide Columns'
  };

  const searchObj = {
    searchFilters: selectedColsLabels.toString()
  };

  trackAnalytics(searchObj, 'search', 'internalFilter', undefined, false, eventObj);
};

export const _trackEquipmentLinkClick = function (linkName) {
  const eventObj = {
    event: 'equipmentLink',
    eventType: 'linkClick'
  };

  const linkClickObj = {
    linkName: linkName,
    linkType: 'internal',
    linkSection: 'hyperlink click',
    linkParentTitle: ''
  };

  trackAnalytics(linkClickObj, 'linkClick', 'equipmentClick', undefined, false, eventObj);
};
