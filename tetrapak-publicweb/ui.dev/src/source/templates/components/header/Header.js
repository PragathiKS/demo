import $ from 'jquery';
import {$document} from '../../../scripts/utils/commonSelectors';
//import { ajaxWrapper } from '../../../scripts/utils/ajax';

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
  }
}

export default Header;
