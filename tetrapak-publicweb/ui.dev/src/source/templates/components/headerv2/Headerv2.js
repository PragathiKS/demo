/* eslint-disable */
import $ from 'jquery';
import 'bootstrap';
import { HEADER_MIN_MARGIN } from './Headerv2.constants';
import { isMobile } from '../../../scripts/common/common';
// import { trackAnalytics } from '../../../scripts/utils/analytics';
// import { checkActiveOverlay, isDesktop } from '../../../scripts/common/common';


class Headerv2 {
  constructor({ el }) {
    this.root = $(el);
  }

  initCache() {
    this.cache = {
      constants: {
        'extraMargin': 0
      },
      variables: {
        'mainNavigationLinksInititalWidth': this.getMainNavigationWidth(),
        /*
          The summed width of all the items in the main navigation menu. 
        */
        'nOfElementsInSubmenu': 0,
        /* 
          The 'nOfElementsInSubmenu' is equal to the elements in the collapsible menu.
        */
        'mainNavigationLinksWidthList': this.getMainNavigationLinksWidthList(),
        /*
          The 'mainNavigationLinksWidthList' contains each of the main navigation menu's item width.
        */
      },
      $elements: {
        'mainNavigationLinks': this.root.find('.tp-pw-headerv2-main-navigation a')
      }
    };
  }

  bindEvents() {
    this.bindWindowSizeChangeEvent();
    this.bindSubmenuOpenEvent();
    this.bindSubmenuMobileOpenEvent();
  }

  getMainNavigationWidth = () => this.root.find('.tp-pw-headerv2-main-navigation').width();
  getMainNavigationLinksWidthList = () => {
    const navLinks = Array.from(this.root.find('.js-tp-pw-headerv2-main-navigation-link'));
    const navLinksWidths = navLinks.map(el => $(el).width());

    return navLinksWidths;
  }

  bindWindowSizeChangeEvent = () => {
    const handleResize = () => {
      const windowWidth = $(window).width();
      const navWidth = $('.tp-pw-headerv2-main-navigation').width();
      const widthDiff = windowWidth - navWidth;
      const navLinkWidths = this.cache.variables['mainNavigationLinksWidthList'];
      const navLinkLastWidthIndex = navLinkWidths.length - this.cache.variables.nOfElementsInSubmenu;
      const lastItemsWidth = navLinkWidths[navLinkLastWidthIndex];
      const minMargin = HEADER_MIN_MARGIN + this.cache.constants.extraMargin;
      const isLargerThanMargin = widthDiff <= minMargin;
      const isLargerThanLastItemWithMargin = this.cache.variables.nOfElementsInSubmenu && 
        (widthDiff > lastItemsWidth + minMargin * 2);

      if (isLargerThanMargin) {
        this.shiftMainNavigationLink();
      } 
      else if (isLargerThanLastItemWithMargin) {
        this.unshiftMainNavigationLink();
      }
    }

    let isLargerThanMargin = true;
    let isLargerThanLastItemWithMargin = true;
    while (isLargerThanMargin || isLargerThanLastItemWithMargin) {
      $(window).off('resize');
      $(window).on('resize', handleResize);
      const windowWidth = $(window).width();
      const navWidth = $('.tp-pw-headerv2-main-navigation').width();
      const widthDiff = windowWidth - navWidth;
      const navLinkWidths = this.cache.variables['mainNavigationLinksWidthList'];
      const navLinkLastWidthIndex = navLinkWidths.length - this.cache.variables.nOfElementsInSubmenu;
      const lastItemsWidth = navLinkWidths[navLinkLastWidthIndex];
      isLargerThanMargin = widthDiff <= HEADER_MIN_MARGIN;
      isLargerThanLastItemWithMargin = this.cache.variables.nOfElementsInSubmenu && 
        (widthDiff > lastItemsWidth + HEADER_MIN_MARGIN * 2);
      handleResize();
    }
  }

  shiftMainNavigationLink = () => {
    this.cache.variables.nOfElementsInSubmenu++;
    const lastEl = this.getLastMainNavigationLink();
    const lastElClone = lastEl.clone();
    lastEl.css('display', 'none');
    this.root.find('.tp-pw-headerv2-submenu').prepend(lastElClone);
    this.root.find('.tp-pw-headerv2-submenu-wrapper').toggle(true);
  }

  unshiftMainNavigationLink = () => {
    const lastEl = this.getLastMainNavigationLink();
    lastEl.css('display', 'block');
    this.cache.variables.nOfElementsInSubmenu--;
    this.root.find('.tp-pw-headerv2-submenu a:first').remove();
    if (!this.cache.variables.nOfElementsInSubmenu) {
      this.root.find('.tp-pw-headerv2-submenu-wrapper').toggle(false);
    }
  }

  getLastMainNavigationLink = () => {
    const navLinks = this.cache.$elements['mainNavigationLinks'];
    const lastElIndex = navLinks.length - this.cache.variables.nOfElementsInSubmenu;
    return $(navLinks[lastElIndex]);
  }

  bindSubmenuOpenEvent = () => {
    const $subMenu = this.root.find('.tp-pw-headerv2-submenu');
    const $subMenuIcon = this.root.find('.js-submenu-icon');

    if (!this.cache.variables.nOfElementsInSubmenu) {
      this.root.find('.tp-pw-headerv2-submenu-wrapper').toggle(false);
    }
    $subMenu.toggle(false);

    $subMenuIcon.on('click', () => {
      $subMenu.toggle();
      $subMenuIcon.hasClass('active') ? $subMenuIcon.removeClass('active') : $subMenuIcon.addClass('active');
    });
  }

  bindSubmenuMobileOpenEvent = () => {
    const $subMenu = this.root.find('.tp-pw-headerv2-mobile-secondary-navigation-menu');
    const $subMenuIcon = this.root.find('.js-submenu-mobile-icon');

    if (isMobile()) {
      $subMenu.toggle(true);
    } else {
      $subMenu.toggle(false);
    }

    $subMenuIcon.on('click', () => {
      $subMenu.toggle();
      $subMenuIcon.hasClass('active') ? $subMenuIcon.removeClass('active') : $subMenuIcon.addClass('active');
    });
  }

  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default Headerv2;