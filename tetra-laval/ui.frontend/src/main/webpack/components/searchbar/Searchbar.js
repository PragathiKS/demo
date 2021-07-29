import $ from 'jquery';
import { trackAnalytics } from '../../../scripts/utils/analytics';
import { updateQueryStringParameter } from '../../../scripts/common/common';
class Searchbar {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$searchbarClose = this.root.find('.search-bar-close');
    this.cache.$searchIcon = this.root.find('.search-icon');
  }
  bindEvents() {
    /* Bind jQuery events here */
    const { $searchbarClose, $searchIcon } = this.cache;
    $searchbarClose.on('click', this.searchbarCloseClick);
    $searchIcon.on('click', this.searchIconClick);

    // bind enter search
    $('.js-search-bar-input').keypress((e) => {
      if(e.which === 13){//Enter key pressed
        this.searchIconClick(e);//Trigger search button click event
      }
    });
  }

  searchbarCloseClick = () => {
    $('.js-pw-header-search-bar').removeClass('show');
  };

  searchIconClick = e => {
    e.preventDefault();
    const inp = $('.js-search-bar-input');
    if (inp.val().length > 0) {
      const $target = $('.search-icon');
      const $this = $target.closest('button');
      const url = $this.data('search-url');
      let updatedUrl = updateQueryStringParameter(url,'searchTerm',encodeURIComponent(inp.val()));
      updatedUrl = updateQueryStringParameter(updatedUrl,'page',1);

      const searchObj = {
        searchTerm : inp.val(),
        searchFilters : ''
      };

      const eventObj = {
        eventType : 'internal search',
        event : 'Search'
      };

      const linkClickObject = {
        linkType: 'internal',
        linkSection: 'Hyperlink click',
        linkParentTitle: '',
        linkName: 'Search'
      };

      trackAnalytics(searchObj, 'search', 'internalsearch', undefined, false, eventObj, linkClickObject);

      window.open(updatedUrl,'_self');
    }
  };

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default Searchbar;
