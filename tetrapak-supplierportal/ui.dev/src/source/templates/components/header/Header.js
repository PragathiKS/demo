import $ from 'jquery';
import { logger } from '../../../scripts/utils/logger';

class Header {
  constructor({ el }) {
    this.root = $(el);
  }

  bindEvents() {
    this.root.find('.tp-header__burger-menu').on('click', () => {
      logger.log('trigger leftnav');
      $(document.body).trigger('showleftnav');
    });
  }

  init() {
    this.bindEvents();
  }
}

export default Header;

