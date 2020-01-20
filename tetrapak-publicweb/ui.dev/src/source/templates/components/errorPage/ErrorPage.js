import $ from 'jquery';
import { debounce } from '../../../scripts/utils/debounce';
import { $win } from '../../../scripts/utils/commonSelectors';

class ErrorPage {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$header = this.root.find('.js-tp-pw-header');
    this.cache.$footer = this.root.find('.js-tp-pw-footer');
    this.cache.$tpContainer = this.root.find('.js-tp-container');
  }
  bindEvents() {
    this.initCache();
    $win.on('resize',
      debounce(this.setScreenInnerHeight, 200)
    );
  }
  init() {
    this.bindEvents();
    this.setScreenInnerHeight();
  }
  setScreenInnerHeight = () => {
    const headerHeight = this.cache.$header.height() || 0;
    const footerHeight = this.cache.$footer.height() || 0;
    const textContainerHeight = $win.height() - headerHeight - footerHeight;
    this.cache.$tpContainer.css('min-height', textContainerHeight);
  }
}

export default ErrorPage;
