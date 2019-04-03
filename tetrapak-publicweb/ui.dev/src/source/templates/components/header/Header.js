import deparam from 'jquerydeparam';
import $ from 'jquery';
import {$document} from '../../../scripts/utils/commonSelectors';

class Header {
  constructor({templates, el}) {
    this.templates = templates;
    this.root = $(el);
  }

  cache = {};

  init() {
    this.initCache();
    this.bindEvents();
  }

  initCache() {
    this.cache.$searchBoxToggle = $('.js-tp-pw-header__search-box-toggle');
    this.cache.$searchBox = $('.js-tp-pw-search-box');
    this.cache.$searchBoxInput = $('.js-tp-pw-search-box__input');
    this.cache.$closeSearcBbox = $('.js-tp-pw-search-box__close-search-box');
    this.setOverlayHeight();
  }

  bindEvents() {
    this.cache.$searchBoxToggle.on('click', this.openSearchBox);
    this.cache.$closeSearcBbox.on('click', this.closeSearchBox);
  }

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
  }
}

export default Header;
