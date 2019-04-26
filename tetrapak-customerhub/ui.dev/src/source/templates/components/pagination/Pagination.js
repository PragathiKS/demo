import $ from 'jquery';
import { render } from '../../../scripts/utils/render';

/**
 * Returns a valid page number object
 * @param {number} pageNumber Page number
 */
function _getPage(pageNumber, currentPage) {
  return {
    isPage: true,
    isSelected: pageNumber === currentPage,
    pageNumber
  };
}

/**
 * Renders pagination component
 * @param  {...any} args Arguments
 */
function _rendePagination(...args) {
  const [e, data] = args;
  const $this = e.data;
  const { config } = $this.cache;
  let currentTarget = $this.root;
  if (config.delegate) {
    currentTarget = $(this);
  }
  const paginationData = $this.calculatePages(data);
  if (paginationData.totalPages > 1) {
    currentTarget.removeClass('d-none');
    render.fn({
      template: 'pagination',
      data: paginationData,
      target: currentTarget
    });
  } else {
    currentTarget.addClass('d-none');
  }
}

/**
 * Triggers a custom event to select a page
 * @param  {object} event Event object
 */
function _selectPage(event) {
  const { data: $this } = event;
  const self = $(this);
  const { customEvent, delegate } = $this.cache.config;
  const pageNumber = self.data('pageNumber');
  let currentRoot = $this.root;
  if (delegate) {
    currentRoot = self.parents('.js-pagination-multiple');
  }
  if (!self.hasClass('active')) {
    currentRoot
      .find('.js-page-number').attr('disabled', 'disabled').end()
      .trigger(customEvent ? `${customEvent}.pagenav` : 'pagenav', [{
        pageNumber,
        pageIndex: pageNumber - 1
      }]);
  }
}

/**
 * Calculates visible pages as per configuration
 * @param {object} data Pagination data
 */
function _calculatePages(data) {
  const { totalPages = 0, currentPage = 1 } = data;
  const { maxVisible = totalPages } = this.cache.config;
  const pages = [];
  if (maxVisible) {
    if (
      maxVisible >= totalPages
      || maxVisible < 4 // There should be at least two pages between start and end page for in-between navigation
    ) {
      for (let i = 1; i <= totalPages; i++) {
        pages.push(_getPage(i, currentPage));
      }
    } else if (maxVisible < totalPages) {
      pages.push(_getPage(1, currentPage));
      // Calculate center elements based on current page
      const totalCenterElements = maxVisible - 2; // Excluding first and last page
      const start = currentPage - Math.floor((totalCenterElements - 1) / 2);
      const preEllipsis = currentPage > totalCenterElements;
      if (preEllipsis) {
        pages.push({
          isEllipsis: true
        });
      }
      let visibleStart = (preEllipsis ? start : 2);
      let visibleEnd = visibleStart + totalCenterElements - 1;
      if (visibleEnd > totalPages - 1) {
        visibleStart = totalPages - totalCenterElements;
        visibleEnd = totalPages - 1;
      }
      for (let i = visibleStart; i <= visibleEnd; i++) {
        pages.push(_getPage(i, currentPage));
      }
      if (visibleEnd < totalPages - 1) {
        pages.push({
          isEllipsis: true
        });
      }
      pages.push(_getPage(totalPages, currentPage));
    }
  }
  return {
    currentPage,
    prevPage: currentPage - 1,
    prevDisabled: currentPage === 1,
    nextPage: currentPage + 1,
    nextDisabled: currentPage === totalPages,
    totalPages,
    pages
  };
}

class Pagination {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.config = this.root.data();
  }
  bindEvents() {
    const { config } = this.cache;
    const $this = this;
    const customEventParams = [
      (config.customEvent ? `${config.customEvent}.paginate` : 'paginate'),
      this,
      this.renderPagination
    ];
    const disableParams = [
      (config.customEvent ? `${config.customEvent}.pagedisabled` : 'pagedisabled'),
      function () {
        const { config } = $this.cache;
        let currentRoot = $this.root;
        if (config.delegate) {
          currentRoot = $(this);
        }
        currentRoot.find('.js-page-number').attr('disabled', 'disabled');
      }
    ];
    if (config.delegate) {
      customEventParams.splice(1, 0, '.js-pagination-multiple');
      disableParams.splice(1, 0, '.js-pagination-multiple');
    }
    this.root
      .on(
        ...customEventParams
      )
      .on(
        ...disableParams
      )
      .on('click', '.js-page-number', this, this.selectPage);

  }
  calculatePages() {
    return _calculatePages.apply(this, arguments);
  }
  renderPagination(...args) {
    return _rendePagination.apply(this, args);
  }
  selectPage() {
    return _selectPage.apply(this, arguments);
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default Pagination;
