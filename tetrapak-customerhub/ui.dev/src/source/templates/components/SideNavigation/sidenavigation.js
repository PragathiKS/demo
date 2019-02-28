// import { logger } from '../../../scripts/utils/logger';
import $ from 'jquery';

class sidenavigation {
  constructor({ el }) {
    this.root = $(el);
  }
  init() {
    this.root.find('.js-close-btn').on('click', () => this.closeSideNav());

  }
  closeSideNav() {
    this.root.find('.tp-left-nav-container').css('display', 'none');
  }
}
export default sidenavigation;
