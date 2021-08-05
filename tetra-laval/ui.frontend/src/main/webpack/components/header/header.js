// to do track analitycs
// write code
import $ from 'jquery';

class Header {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};

  bindEvents () {
    this.cache.$searchIcon.on('click', () => {
      debugger;
      this.cache.$searchBar.addClass('show');
    })
    this.cache.$searchBarCloseIcon.on('click', () => {
      this.cache.$searchBar.removeClass('show');
    })

  }

  init() {
    this.cache.$searchIcon = this.root.find('.js-tp_pw-header__nav-options-search');
    this.cache.$searchBar = this.root.find('.js-pw-header-search-bar');
    this.cache.$searchBarCloseIcon = this.root.find('.search-bar-close');

    this.bindEvents()
  }
}

export default Header;
