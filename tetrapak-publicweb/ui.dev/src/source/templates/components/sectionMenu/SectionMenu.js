import $ from 'jquery';
import 'bootstrap';
import { isMobile } from '../../../scripts/common/common';
class SectionMenu {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$sectionMenuItem = this.root.find('.js-section-menu-navigation-Link');
  }

  bindEvents() {
    const { $sectionMenuItem } = this.cache;
    $sectionMenuItem.on('mouseover', this.handleSectionMenuItemMouseOver);
    $sectionMenuItem.on('mouseout', this.handleSectionMenuItemMouseOut);
    $('.js-section-menu-item-link').on('click', this.handleSectionMenuClick);
    // $('.js-sub-menu-navigation-link-item').on('click', this.handleSubSectionMenuClick);
  }

  handleSectionMenuClick =(e) => {
    e.preventDefault();
    const $target = $(e.target);
    const $this = $target.closest('.js-section-menu-item-link');
    const iconEl = $this.find('i.icon:not(.is-external)');
    if(isMobile() && iconEl.length > 0) {
      return;
    }
    window.open($this.data('url-link'), $this.attr('target'));
  }

  // handleSubSectionMenuClick =(e) => {
  //   e.preventDefault();
  //   const $target = $(e.target);
  //   const $this = $target.closest('.js-sub-menu-navigation-link-item');
  //   window.open($this.attr('href'), $this.attr('target'));
  // }

  handleSectionMenuItemMouseOver = (e) => {
    const $target = $(e.target);
    const $this = $target.closest('.js-section-menu-navigation-Link');
    const $sectionMenuItemAnchor = $this.children('a');
    $this.find('.js-sub-menu-navigation-Link').addClass('show').attr('aria-hidden','false').attr('aria-expanded','true'); // value changed because of one extra div added
    $sectionMenuItemAnchor.children('.with-arrow').addClass('icon-up');

    /* check modal view port position */
    const modalPoints =$this.children('.js-sub-menu-navigation-Link')[0] && $this.children('.js-sub-menu-navigation-Link')[0].getBoundingClientRect();
    if(modalPoints && modalPoints.left < 0){
      $this.children('.js-sub-menu-navigation-Link').addClass('show-modal-from-left');
    } else if(modalPoints && modalPoints.right > (window.innerWidth || document.documentElement.clientWidth)){
      $this.children('.js-sub-menu-navigation-Link').addClass('show-modal-from-right');
    }
  }

  handleSectionMenuItemMouseOut = (e) => {
    const $target = $(e.target);
    const $this = $target.closest('.js-section-menu-navigation-Link');
    const $sectionMenuItemAnchor = $this.children('a');
    $this.find('.js-sub-menu-navigation-Link').removeClass('show').attr('aria-hidden', 'true').attr('aria-expanded','false');
    $sectionMenuItemAnchor.children('.with-arrow').removeClass('icon-up');
  }

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default SectionMenu;
