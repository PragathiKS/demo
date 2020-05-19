/* eslint-disable */
import $ from 'jquery';

class SectionMenu {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$sectionMenuHeaderItem = this.root.find('.js-section-menu-navigation-Link');
  }

  bindEvents() {
    const { $sectionMenuHeaderItem } = this.cache;
    $sectionMenuHeaderItem.on('mouseover', this.handleHeaderItemMouseOver);
    $sectionMenuHeaderItem.on('mouseout', this.handleHeaderItemMouseOut);
  }

  handleHeaderItemMouseOver = (e) => {
    const $target = $(e.target);
    const $this = $target.closest('.js-section-menu-navigation-Link');
    const $sectionMenuItemAnchor = $this.children('a');
    $this.children('.js-sub-menu-navigation-Link').addClass('show').attr('aria-hidden','false').attr('aria-expanded','true');
    $sectionMenuItemAnchor.children('.with-arrow').addClass('icon-Chevron_Up').removeClass('icon-Chevron_Down');
  }

  handleHeaderItemMouseOut = (e) => {
    const $target = $(e.target);
    const $this = $target.closest('.js-section-menu-navigation-Link');
    const $sectionMenuItemAnchor = $this.children('a');
    $this.children('.js-sub-menu-navigation-Link').removeClass('show').attr('aria-hidden', 'true').attr('aria-expanded','false');
    $sectionMenuItemAnchor.children('.with-arrow').addClass('icon-Chevron_Down').removeClass('icon-Chevron_Up');
  }

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default SectionMenu;
