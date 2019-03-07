import $ from 'jquery';
import { $body } from '../../../scripts/utils/commonSelectors';

class Header {
  constructor({ el }) {
    this.root = $(el);
  }

  init() {
    this.root.find('.tp-header__burger-menu').on('click', () => {
      $body.trigger('showLeftNav');
    });
  }
}

export default Header;

