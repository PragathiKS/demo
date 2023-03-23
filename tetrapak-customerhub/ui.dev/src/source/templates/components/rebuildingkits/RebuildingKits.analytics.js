import { trackAnalytics } from '../../../scripts/utils/analytics';

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
    linkParentTitle: 'Text Hyperlink_My RebuildingKits'
  };

  trackAnalytics(linkClickObj, 'linkClick', 'paginationClick', undefined, false, eventObj);
};

export const _customizeTableBtnAnalytics = ($targetBtn) => {
  const eventObj = {
    event: 'Customize Table',
    eventType: 'Show/Hide Columns'
  };

  const linkClickObj = {
    linkName: $targetBtn.find('.tp-rk__header-action-text').text().replace(' +', ''),
    linkType: 'internal',
    linkSection: 'hyperlink click',
    linkParentTitle: 'Text Hyperlink_Rebuilding Kits'
  };

  trackAnalytics(linkClickObj, 'linkClick', 'paginationClick', undefined, false, eventObj);
};
