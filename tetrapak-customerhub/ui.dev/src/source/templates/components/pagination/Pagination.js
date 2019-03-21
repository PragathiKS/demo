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
    this.root.on(
      (config.customEvent ? `${config.customEvent}.paginate` : 'paginate'),
      this.renderPagination
    );
  }
  calculatePages() {
    return _calculatePages.apply(this, arguments);
  }
  renderPagination = (...args) => {
    const [, data] = args;
    const paginationData = this.calculatePages(data);

    if (paginationData.totalPages > 0) {
      render.fn({
        template: 'pagination',
        data: paginationData,
        target: this.root
      });
    }
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.root.trigger(`${this.cache.config.customEvent}.paginate`, [{
      totalPages: 13,
      currentPage: 1
    }]); // temporary
  }
}

export default Pagination;
