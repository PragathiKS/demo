import $ from 'jquery';
import {$document} from '../../../scripts/utils/commonSelectors';

class Header {
  constructor({templates, el}) {
    this.templates = templates;
    this.root = $(el);
  }

  cache = {};

  initCache() {
    this.cache.$searchBoxToggle = $('.js-tp-pw-header__search-box-toggle');
    this.cache.$searchBox = $('.js-tp-pw-search-box');
    this.cache.$searchBoxInput = $('.js-tp-pw-search-box__input');
    this.cache.$closeSearcBbox = $('.js-tp-pw-search-box__close-search-box');
    this.cache.secondaryNav = $('.js-tp-pw-header__secondary-navigation');
    this.cache.searchBoxPointer = $('.js-tp-pw-search-box__pointer');
    this.cache.pointedElement = $('.' + this.cache.searchBoxPointer.data('pointTo'));
    this.cache.url = location.href.split('?')[1] || null;
    this.setOverlayHeight();
  }

  bindEvents() {
    this.cache.$searchBoxToggle.on('click', this.openSearchBox);
    this.cache.$closeSearcBbox.on('click', this.closeSearchBox);
    this.cache.$searchBoxInput.on('keyup', (e) => {
      if (e.which === 13) {
        let searchTerm = $('.js-tp-pw-search-box__input').val();
        this.search(searchTerm);
      }
    });
    $(window).on('resize', () => {
      this.movePointer();
    });
  }

  search = (searchTerm) => {
    let origin = window.location.origin;
    let params = {};
    let searchResultsPath = this.cache.$searchBoxToggle.data('resultsPath');
    params.q = searchTerm;
    //TODO Get existent param values modify q and then build the url again
    let destinationURL = origin + searchResultsPath + '.html?q=' + searchTerm;
    window.location.replace(destinationURL);
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
    let secNavWidth = this.cache.secondaryNav.width();
    $('.js-tp-pw-search-box__pointer').css('right', secNavWidth + 10 + 'px');
  };

  init() {
    this.initCache();
    this.bindEvents();
    this.movePointer(this.cache.searchBoxPointer, this.cache.pointedElement);
  }
}

export default Header;
