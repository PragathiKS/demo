import { trackAnalytics } from '../../../scripts/utils/analytics';

export const _paginationAnalytics = (target) => {
  let linkName;
  const pageNum = Number(target.getAttribute('data-page-number'));

  const eventObj = {
    event: 'Pagination',
    eventType: 'paginationClick'
  };

  if (target.classList.contains('tp-tbl-pagination__last')) {
    linkName = 'last page';
  }
  else if (target.classList.contains('tp-tbl-pagination__first')) {
    linkName = 'first page';
  }
  else if (target.classList.contains('tp-tbl-pagination__prev')) {
    linkName = `Navigation_prev_${pageNum}`;
  }
  else if (target.classList.contains('tp-tbl-pagination__next')) {
    linkName = `Navigation_next_${pageNum}`;
  }
  else if (target.classList.contains('tp-tbl-pagination__direct')) {
    linkName = `Direct_${pageNum}`;
  }

  const linkClickObj = {
    linkName: linkName,
    linkType: 'internal',
    linkSection: 'hyperlink click',
    linkParentTitle: 'Text Hyperlink_All Payments'
  };

  trackAnalytics(linkClickObj, 'linkClick', 'paginationClick', undefined, false, eventObj);
};
