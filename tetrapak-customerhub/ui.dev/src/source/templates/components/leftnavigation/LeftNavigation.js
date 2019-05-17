import $ from 'jquery';
import { $body } from '../../../scripts/utils/commonSelectors';
import { TRANSITION_END } from '../../../scripts/utils/constants';
import { logger } from '../../../scripts/utils/logger';
/**
 * Handles submenu events
 * @param {object} $this Class reference
 */
function _openSubMenu($this) {
  const self = $(this);
  if ($this.cache.anim) {
    return;
  }
  const section = self.next('.collapsible');
  if (self.attr('aria-expanded') === 'false') {
    self.attr('aria-expanded', 'true');
    section
      .attr('aria-hidden', 'false')
      .removeClass('collapsible').addClass('animating')
      .css('height', 0);
    const scrollHeight = section[0].scrollHeight;
    section.one(TRANSITION_END, () => {
      section.removeClass('animating');
      section.addClass('collapsible active');
      section.css('height', '');
      $this.cache.anim = false;
    });
    section.css('height', `${scrollHeight}px`);
    $this.cache.anim = true;
  } else {
    section.css('height', `${section.outerHeight()}px`);
    $this.reflow(section[0]);
    self.attr('aria-expanded', 'false');
    section
      .attr('aria-hidden', 'true');
    section.addClass('animating').removeClass('collapsible').removeClass('active');
    section.one(TRANSITION_END, () => {
      section.removeClass('animating');
      section.addClass('collapsible');
      $this.cache.anim = false;
    });
    section.css('height', '');
    $this.cache.anim = true;
  }
}

class LeftNavigation {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$container = this.root.find('.tp-left-nav__container');
    this.cache.$sticky = this.root.find('.tpatom-list-item__link--sticky');
    this.cache.$submenuSections = this.root.find('.tpatom-list-item__btn');
    this.cache.$closeBtn = this.root.find('.js-close-btn');
    this.cache.$navOverlay = this.root.find('.js-left-nav__overlay');
    this.cache.$mainHeading = this.root.find('.tp-left-nav__main-heading');
    this.cache.$listItem = this.root.find('.tpmol-list-item', 'tpatom-list-item');
  }
  bindEvents() {
    const { $submenuSections, $closeBtn, $navOverlay, $mainHeading, $listItem } = this.cache;
    const $this = this;
    $closeBtn.on('click', this.closeSideNav);
    $navOverlay
      .on('click', this.closeSideNav)
      .on(TRANSITION_END, this.hideAside);
    $submenuSections.on('click', function () {
      return $this.openSubMenu.apply(this, [$this, ...arguments]);
    });
    $listItem.on('click', (e) => {
      e.stopPropagation();
    });
    $mainHeading.on('click', (e) => {
      e.stopPropagation();
    });
    $body.on('showLeftNav', this.openSideNav);
  }

  closeSideNav = () => {
    logger.log('click1');
    const { $container, $navOverlay, $sticky } = this.cache;
    $container.removeClass('translated');
    $navOverlay.removeClass('color-transform');
    $sticky.removeClass('translated');
  }
  openSideNav = () => {
    logger.log('click1 2');
    const { $container, $navOverlay, $sticky } = this.cache;
    $navOverlay.removeClass('d-none d-lg-block');
    this.reflow($navOverlay[0]);
    $container.addClass('translated');
    $navOverlay.addClass('color-transform');
    $sticky.addClass('translated');
  }
  reflow = (el) => el && el.offsetHeight;
  openSubMenu() {
    return _openSubMenu.apply(this, arguments);
  }
  hideAside() {
    logger.log('click1 3');
    const $this = $(this);
    if (!$this.hasClass('color-transform')) {
      $this.addClass('d-none d-lg-block');
    }
  }
  init() {
    this.initCache();
    this.bindEvents();
  }
}
export default LeftNavigation;
