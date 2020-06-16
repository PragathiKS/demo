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

    // bind enter search
    $('.js-search-bar-input').keypress((e) => {
      if(e.which === 13){//Enter key pressed
        this.searchIconClick(e);//Trigger search button click event
      }
    });
  }

  searchbarCloseClick = () => {
    $('.js-pw-search-bar').removeClass('show');
  };

  searchIconClick = e => {
    e.preventDefault();
    const inp = $('.js-search-bar-input');
    if (inp.val().length > 0) {
      const url = '/content/publicweb-ux/searchresults.html';
      let updatedUrl = updateQueryStringParameter(url,'searchTerm',inp.val());
      updatedUrl = updateQueryStringParameter(updatedUrl,'page',1);
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
