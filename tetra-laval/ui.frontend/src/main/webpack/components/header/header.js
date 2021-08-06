// to do track analitycs
// write code
import $ from 'jquery';

class Header {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};

  bindEvents () {
    this.cache.$searchBtn.on('click', () => {
      if (!this.isSearchBarShown()) {
        this.cache.$searchBar.addClass('show');
      }
      if (this.isMobileMode()) {
        const searchIcon = this.cache.$searchBtn.find('.icon-Search_blue_pw');

        if (this.isSearchBarShown()) {
          if (this.cache.$searchBtn.find('.icon-Close_pw')[0]) {
            this.cache.$searchBar.removeClass('show');
            this.cache.$searchBtn.find('i').removeClass('icon-Close_pw');
            this.cache.$searchBtn.find('i').addClass('icon-Search_blue_pw');

            this.setSearchBtnAttributes('Find on page');
          } else {
            searchIcon.removeClass('icon-Search_blue_pw');
            searchIcon.addClass('icon-Close_pw');

            this.setSearchBtnAttributes('Close search');
          }

        } else {
          searchIcon.removeClass('icon-Close_pw');
          searchIcon.addClass('icon-Search_blue_pw');

          this.setSearchBtnAttributes('Find on page');
        }
      }
    })
    this.cache.$searchBarCloseIcon.on('click', () => {
      this.cache.$searchBar.removeClass('show');
    })
  }

  setSearchBtnAttributes = (textValue) => {
    this.cache.$searchBtn.attr('aria-label', textValue)
    this.cache.$searchBtn.attr('title', textValue)
  }

  isSearchBarShown = () => {
    return this.cache.$searchBar.hasClass('show');
  }

  isMobileMode = () => {
    const outerWidth = $(window).outerWidth();
    return outerWidth < 1025;
  }

  init() {
    this.cache.$searchBtn = this.root.find('.js-tp_pw-header__nav-options-search');
    this.cache.$searchBar = this.root.find('.js-pw-header-search-bar');
    this.cache.$searchBarCloseIcon = this.root.find('.search-bar-close');

    this.bindEvents()
  }
}

export default Header;
