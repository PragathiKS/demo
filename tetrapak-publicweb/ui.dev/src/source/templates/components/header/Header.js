import $ from 'jquery';
import { $document, $win } from '../../../scripts/utils/commonSelectors';
import { loc } from '../../../scripts/common/common';

class Header {
  constructor({ el }) {
    this.root = $(el);
  }

  cache = {};

  initCache() {
    this.cache.$searchBoxToggle = this.root.find('.js-tp-pw-header__search-box-toggle');
    this.cache.$searchBox = this.root.find('.js-tp-pw-search-box');
    this.cache.$searchBoxInput = this.root.find('.js-tp-pw-search-box__input');
    this.cache.$closeSearcBbox = this.root.find('.js-tp-pw-search-box__close-search-box');
    this.cache.secondaryNav = this.root.find('.js-tp-pw-header__secondary-navigation');
    this.cache.searchBoxPointer = this.root.find('.js-tp-pw-search-box__pointer');
    this.cache.pointedElement = this.root.find(`.${this.cache.searchBoxPointer.data('pointTo')}`);
    this.cache.url = location.href.split('?')[1] || null;
    this.setOverlayHeight();
  }

  bindEvents() {
    const { $searchBoxToggle, $closeSearcBbox, $searchBoxInput } = this.cache;
    $searchBoxToggle.on('click', this.openSearchBox);
    $closeSearcBbox.on('click', this.closeSearchBox);
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

  init() {
    this.initCache();
    this.bindEvents();
    this.movePointer(this.cache.searchBoxPointer, this.cache.pointedElement);
  }
}

export default Header;
