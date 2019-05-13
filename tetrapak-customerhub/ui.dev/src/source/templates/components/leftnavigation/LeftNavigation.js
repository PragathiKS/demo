import $ from 'jquery';
import { $body } from '../../../scripts/utils/commonSelectors';

class LeftNavigation {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$container = this.root.find('.tp-left-nav__container');
    this.cache.$sticky = this.root.find('.tpatom-list-item__link--sticky');
    this.cache.$orderBtn = this.root.find('.tpatom-list-item__btn');
    this.cache.$closeBtn = this.root.find('.js-close-btn');
    this.cache.$navOverlay = this.root.find('.js-left-nav__overlay');
    this.cache.$mainHeading = this.root.find('.tp-left-nav__main-heading');
    this.cache.$listItem = this.root.find('.tpmol-list-item', 'tpatom-list-item');
  }
  bindEvents() {
    const { $orderBtn, $closeBtn, $navOverlay, $mainHeading, $listItem } = this.cache;
    $closeBtn.on('click', this.closeSideNav);
    $navOverlay.on('click', this.closeSideNav);
    $orderBtn.on('click', this.openSubMenu);
    $listItem.on('click', (e) => {
      e.stopPropagation();
    });
    $mainHeading.on('click', (e) => {
      e.stopPropagation();
    });
    $body.on('showLeftNav', this.openSideNav);
  }

  closeSideNav = () => {
    const { $container, $navOverlay, $sticky } = this.cache;
    $container.removeClass('translated');
    $navOverlay.removeClass('color-transform');
    $sticky.removeClass('translated');
  }
  openSideNav = () => {
    const { $container, $navOverlay, $sticky } = this.cache;
    $container.addClass('translated');
    $navOverlay.addClass('color-transform');
    $sticky.addClass('translated');
  }
  openSubMenu() {
    const self = $(this);
    if (self.attr('aria-expanded') === 'false') {
      self.attr('aria-expanded', 'true');
    } else {
      self.attr('aria-expanded', 'false');
    }
  }
  init() {
    this.initCache();
    this.bindEvents();
  }
}
export default LeftNavigation;
