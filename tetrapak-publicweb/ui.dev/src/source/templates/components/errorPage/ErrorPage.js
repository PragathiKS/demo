import $ from 'jquery';
import {debounce} from '../../../scripts/utils/debounce';

class ErrorPage {

  constructor({ el }) {
    this.root = $(el);
  }

  cache = {};

  initCache() {
    this.cache.$window = $(window);
    this.cache.$header = $('.js-tp-pw-header', this.root);
    this.cache.$footer = $('.js-tp-pw-footer', this.root);
    this.cache.$jsTpContainer = $('.js-tp-container', this.root);
  }

  bindEvents() {

    this.initCache();
    $(window).on('resize', debounce(this.setScreenInnerHeight, 200));
  }

  init() {
    this.bindEvents();
    this.setScreenInnerHeight();
  }



  setScreenInnerHeight = () => {
    let headerHeight = this.cache.$header.height() || 0;
    let footerHeight = this.cache.$footer.height() || 0;
    let textContainerHeight = this.cache.$window.height() - headerHeight - footerHeight;
    this.cache.$jsTpContainer.css('min-height', textContainerHeight);
  }

}

export default ErrorPage;
