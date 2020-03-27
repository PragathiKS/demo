import $ from 'jquery';
import { $document, $win } from '../../../scripts/utils/commonSelectors';
import { loc } from '../../../scripts/common/common';

class Header {
  constructor({ el }) {
    this.root = $(el);
    this.toggleFlag = false;
  }

  cache = {};

  initCache() {
    this.cache.$searchBoxToggle = this.root.find('.js-tp-pw-header__search-box-toggle');
    this.cache.$searchBox = this.root.find('.js-tp-pw-search-box');
    this.cache.$mobileMenu = this.root.find('.js-tp-pw-mobile-navigation');
    this.cache.$searchBoxInput = this.root.find('.js-tp-pw-search-box__input');
    this.cache.$closeSearcBbox = this.root.find('.js-tp-pw-search-box__close-search-box');
    this.cache.$hamburgerToggle = this.root.find('.js-tp-pw-header__hamburger');
    //this.cache.$hamburgerClose = this.root.find('.js-tp-pw-header__hamburger-close');
    this.cache.secondaryNav = this.root.find('.js-tp-pw-header__secondary-navigation');
    this.cache.searchBoxPointer = this.root.find('.js-tp-pw-search-box__pointer');
    this.cache.pointedElement = this.root.find(`.${this.cache.searchBoxPointer.data('pointTo')}`);
    this.cache.url = location.href.split('?')[1] || null;
    this.setOverlayHeight();
  }

  bindEvents() {
    const { $searchBoxToggle, $closeSearcBbox, $searchBoxInput, $hamburgerToggle } = this.cache;
    $searchBoxToggle.on('click', this.openSearchBox);
    $closeSearcBbox.on('click', this.closeSearchBox);
    $hamburgerToggle.on('click', this.openMobileMenuBoxToggle);
    //$hamburgerClose.on('click', this.closeMobileMenuBox);
    $searchBoxInput.on('key.return', () => {
      const searchTerm = $('.js-tp-pw-search-box__input').val();
      this.search(searchTerm);
    });
    $win.on('resize', () => {
      this.movePointer();
    });
  }

  search = (searchTerm) => {
    const origin = window.location.origin;
    const params = {};
    const searchResultsPath = this.cache.$searchBoxToggle.data('resultsPath');
    params.q = searchTerm;
    //TODO Get existent param values modify q and then build the url again
    const destinationURL = origin + searchResultsPath + '.html?q=' + searchTerm;
    loc.replace(destinationURL);
  };

  openSearchBox = () => {
    this.cache.$searchBox.fadeIn(300, () => {
      $('.js-tp-pw-search-box__input').focus();
    });
  };
  closeSearchBox = () => {
    this.cache.$searchBox.fadeOut(300);
  };
  setOverlayHeight = () => {
    this.cache.$searchBox.css('height', $document.height());
  };
  movePointer = () => {
    const secNavWidth = this.cache.secondaryNav.width();
    $('.js-tp-pw-search-box__pointer').css('right', secNavWidth + 10 + 'px');
  };

  openMobileMenuBoxToggle = () => {
    if(!this.toggleFlag){
      this.cache.$mobileMenu.fadeIn(300);
      this.cache.$hamburgerToggle.children('#toggle-button').removeClass('icon-Burger');
      this.cache.$hamburgerToggle.children('#toggle-button').addClass('icon-Close');
      this.toggleFlag = true;
    }else {
      this.cache.$mobileMenu.fadeOut(300);
      this.cache.$hamburgerToggle.children('#toggle-button').removeClass('icon-Close');
      this.cache.$hamburgerToggle.children('#toggle-button').addClass('icon-Burger');
      this.toggleFlag = false;
    }
  }

  // closeMobileMenuBox = () => {
  //   // eslint-disable-next-line no-console
  //   console.log('CloseMobileMenuBox called');
  //   this.cache.$mobileMenu.fadeOut(300);
  //   this.cache.$hamburgerToggle.fadeIn(100);
  //   this.cache.$hamburgerClose.fadeOut(100);
  // }

  init() {
    this.initCache();
    this.bindEvents();
    this.movePointer(this.cache.searchBoxPointer, this.cache.pointedElement);
  }
}

export default Header;
