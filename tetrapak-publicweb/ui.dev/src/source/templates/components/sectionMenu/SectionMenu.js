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
    $('.js-section-menu-item-link').on('click', this.handleSectionMenuClick);
  }

  updateQueryString=(uri, key, value) => {
    var re = new RegExp('([?|&])' + key + '=.*?(&|$)', 'i');
    const separator = uri.indexOf('?') !== -1 ? '&' : '?';
    if (uri.match(re)) {
      return uri.replace(re, `$1${  key  }=${  value  }$2`);
    }
    else {
      return `${uri + separator + key  }=${  value}`;
    }
  }

  handleSectionMenuClick =(e)=>{
    const $target = $(e.target);
    const $parentLink = $target.closest('.js-main-menu-link-hover');
    const $mainHeaderAnchor = $parentLink.children('a');
    const $this = $target.closest('.js-section-menu-item-link');
    const mainMenuLinkName = $mainHeaderAnchor.data('link-name');
    const linkName = $this.data('link-name');
    const updatedUrl = this.updateQueryString($this.attr('href'),'header',mainMenuLinkName);
     finalUrl = this.updateQueryString(updatedUrl,'header',linkName);
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
