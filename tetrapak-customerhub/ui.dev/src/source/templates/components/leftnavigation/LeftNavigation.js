import $ from 'jquery';
import { $body } from '../../../scripts/utils/commonSelectors';
import { logger } from '../../../scripts/utils/logger';


class LeftNavigation {
  constructor({ el }) {
    this.root = $(el);
  }
  init() {
    this.root.find('.js-close-btn').on('click', this.closeSideNav);
    this.root.find('.js-left-nav__overlay').on('click', this.closeSideNav);
    this.root.find('.tpatom-list-item__btn').on('click', this.openSubMenu);
    this.root.find('.tpmol-list-item', 'tpatom-list-item').on('click', (e) => {
      e.stopPropagation();
    });
    this.root.find('.tp-left-nav__main-heading').on('click', (e) => {
      e.stopPropagation();
    });
    $body.on('showLeftNav', this.openSideNav);
  }

  closeSideNav = () => {
    this.root.find('.tp-left-nav__container').removeClass('translated');
    this.root.find('.tp-left-nav__overlay').removeClass('color-transform');
    this.root.find('.tpatom-list-item__link--sticky').removeClass('translated');
  }
  openSideNav = () => {
    this.root.find('.tp-left-nav__container').addClass('translated');
    this.root.find('.tp-left-nav__overlay').addClass('color-transform');
    this.root.find('.tpatom-list-item__link--sticky').addClass('translated');
  }
  openSubMenu = () => {
    // eslint-disable-next-line no-debugger
    debugger;
    // eslint-disable-next-line no-undef
    logger.log('thisss', this);
    if (this.root.find('.tpatom-list-item__btn').attr('aria-expanded') === 'false') {
      this.root.find('.tpatom-list-item__btn').attr('aria-expanded', 'true');
    } else {
      this.root.find('.tpatom-list-item__btn').attr('aria-expanded', 'false');
    }
  }
}
export default LeftNavigation;
