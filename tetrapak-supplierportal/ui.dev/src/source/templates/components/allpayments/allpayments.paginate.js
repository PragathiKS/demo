/**
 * Returns an obj containing pagination data, based on totalItems, currentPage, pageSize, maxPages
 */
export const _paginate = (totalItems, currentPage, pageSize, maxPages) => {
  // calculate total pages
  const totalPages = Math.ceil(totalItems / pageSize);
  // ensure current page isn't out of range
  if (currentPage < 1) {
    currentPage = 1;
  } else if (currentPage > totalPages) {
    currentPage = totalPages;
  }

  let startPage, endPage;
  if (totalPages <= maxPages) {
    // total pages less than max so show all pages
    startPage = 1;
    endPage = totalPages;
  } else {
    // total pages more than max so calculate start and end pages
    const maxPagesBeforeCurrentPage = Math.floor(maxPages / 2);
    const maxPagesAfterCurrentPage = Math.ceil(maxPages / 2) - 1;
    if (currentPage <= maxPagesBeforeCurrentPage) {
      // current page near the start
      startPage = 1;
      endPage = maxPages;
    } else if (currentPage + maxPagesAfterCurrentPage >= totalPages) {
      // current page near the end
      startPage = totalPages - maxPages + 1;
      endPage = totalPages;
    } else {
      // current page somewhere in the middle
      startPage = currentPage - maxPagesBeforeCurrentPage;
      endPage = currentPage + maxPagesAfterCurrentPage;
    }
  }

  // calculate start and end item indexes
  const startIndex = (currentPage - 1) * pageSize;
  const endIndex = Math.min(startIndex + pageSize - 1, totalItems - 1);

  // create an array of pages
  const allPages = Array.from(Array((endPage + 1) - startPage).keys()).map(i => startPage + i);

  const pagesHbsArr = [];
  allPages.forEach(page => {
    const pageObj = {
      isPage: true,
      isSelected: page === currentPage,
      pageNumber: page,
      skip: (page - 1) * pageSize
    };
    pagesHbsArr.push(pageObj);
  });

  return {
    totalItems: totalItems,
    currentPage: currentPage,
    pageSize: pageSize,
    totalPages: totalPages,
    startPage: startPage,
    endPage: endPage,
    startIndex: startIndex,
    endIndex: endIndex,
    prevDisabled: currentPage === 1,
    nextDisabled: currentPage === totalPages,
    prevPage: currentPage - 1,
    nextPage: currentPage + 1,
    prevSkip: (currentPage - 2) * pageSize,
    nextSkip: currentPage * pageSize,
    lastSkip: (totalPages - 1) * pageSize,
    pages: pagesHbsArr
  };
};
