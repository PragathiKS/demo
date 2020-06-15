import $ from 'jquery';
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
  }

  searchbarCloseClick = () => {
    $('.js-pw-search-bar').removeClass('show');
  };

  searchIconClick = e => {
    e.preventDefault();
    const inp = $('.js-search-bar-input');
    if (inp.val().length > 0) {
      const updatedUrl = updateQueryStringParameter('/content/publicweb-ux/searchresults.html','searchTerm',inp.val());
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
