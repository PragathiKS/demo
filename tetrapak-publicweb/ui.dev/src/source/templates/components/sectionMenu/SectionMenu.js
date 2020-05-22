import $ from 'jquery';
import { updateQueryString } from '../../../scripts/common/common';

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
    $('.js-section-menu-item-link').on('click', this.handleSectionMenuClick);
    $('.js-sub-menu-navigation-link-item').on('click', this.handleSubSectionMenuClick);
  }

  getMainMenuItem = ($target) => {
    const $mainMenuLink = $target.closest('.js-main-menu-link-hover');
    const $mainMenuItem = $mainMenuLink.children('a');
    return $mainMenuItem.data('link-name');
  }

  getL2MenuItem = ($target) => {
    const $l2MenuLink = $target.closest('.js-section-menu-navigation-Link');
    const $l2MenuItem = $l2MenuLink.children('a');
    return $l2MenuItem.data('link-name');
  }

  handleSectionMenuClick =(e) => {
    e.preventDefault();
    const $target = $(e.target);
    const $this = $target.closest('.js-section-menu-item-link');
    const linkName = $this.data('link-name');
    const updatedUrl = updateQueryString($this.attr('href'),'header',this.getMainMenuItem($target));
    const finalUrl = updateQueryString(updatedUrl,'l2',linkName);
    if(finalUrl){
      window.open(finalUrl, '_self');
    }
  }

  handleSubSectionMenuClick =(e) => {
    e.preventDefault();
    const $target = $(e.target);
    const $this = $target.closest('.js-sub-menu-navigation-link-item');
    const linkName = $this.data('link-name');
    const urlWithL1 = updateQueryString($this.attr('href'),'header',this.getMainMenuItem($target));
    const urlWithL2 = updateQueryString(urlWithL1,'l2',this.getL2MenuItem($target));
    const finalUrl = updateQueryString(urlWithL2,'l3',linkName);
    if(finalUrl){
      window.open(finalUrl, '_self');
    }
  }

  handleHeaderItemMouseOver = (e) => {
    const $target = $(e.target);
    const $this = $target.closest('.js-section-menu-navigation-Link');
    const $sectionMenuItemAnchor = $this.children('a');
    $this.children('.js-sub-menu-navigation-Link').addClass('show').attr('aria-hidden','false').attr('aria-expanded','true');
    $sectionMenuItemAnchor.children('.with-arrow').addClass('icon-Chevron_Up').removeClass('icon-Chevron_Down');

    /* check modal view port position */
    const modalPoints =$this.children('.js-sub-menu-navigation-Link')[0] && $this.children('.js-sub-menu-navigation-Link')[0].getBoundingClientRect();
    if(modalPoints && modalPoints.left < 0){
      $this.children('.js-sub-menu-navigation-Link').addClass('show-modal-from-left');
    } else if(modalPoints && modalPoints.right > (window.innerWidth || document.documentElement.clientWidth)){
      $this.children('.js-sub-menu-navigation-Link').addClass('show-modal-from-right');
    }
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
