import $ from 'jquery';
import 'bootstrap';
import { isDesktop } from '../../../scripts/common/common';
import { trackAnalytics } from '../../../scripts/utils/analytics';
class SectionMenu {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$sectionMenuItem = this.root.find('.js-section-menu-navigation-Link');
    this.cache.$overlay = $('.js-pw-overlay');
  }

  bindEvents() {
    const { $sectionMenuItem } = this.cache;
    $sectionMenuItem.on('mouseover', this.handleSectionMenuItemMouseOver);
    $sectionMenuItem.on('mouseout', this.handleSectionMenuItemMouseOut);
    $('.js-section-menu-item-link').off().on('click', this.handleSectionMenuClick);
    $('.js-sub-menu-navigation-link-item').off().on('click', this.handleSubSectionMenuClick);

    $('.js-section-menu-item-link:not(:has(.icon:not(.is-external)))').each(function() {
      $(this).on('click', function (e) {
        e.stopPropagation();
      });
    });
  }

  handleSectionMenuClick =(e) => {
    e.preventDefault();
    const $target = $(e.target);
    const $this = $target.closest('.js-section-menu-item-link');
    const iconEl = $this.find('i.icon:not(.is-external)');
    if(!isDesktop() && iconEl.length > 0) {
      return;
    }
    this.getSectionMenuAnalyticsValue(e);
    window.open($this.data('url-link'), $this.attr('target'));
  }

  handleSubSectionMenuClick =(e) => {
    e.preventDefault();
    const $target = $(e.target);
    const $this = $target.closest('.js-sub-menu-navigation-link-item');
    const parentLink = $this.closest('.js-section-menu-navigation-Link');
    this.getSubSectionAnalyticsValue(e,parentLink);
    window.open($this.attr('href'), $this.attr('target'));
  }


  getSectionMenuAnalyticsValue = (e) => {
    const $target = $(e.target);
    const $this = $target.closest('.js-section-menu-item-link');
    const navigationLinkName = $this.data('link-name');
    const navigationSection = $this.data('link-section');
    this.trackAnalytics({
      navigationLinkName,
      navigationSection
    },$this);
  }

  getSubSectionAnalyticsValue = (e,parentLink) => {
    const $target = $(e.target);
    const $this = $target.closest('.js-sub-menu-navigation-link-item');
    const navigationLinkName = `${parentLink.find('a.js-section-menu-item-link').data('link-name')}:${$this.data('link-text')}`;
    const navigationSection = $this.data('link-section');
    this.trackAnalytics({
      navigationLinkName,
      navigationSection
    },$this);
  }

  trackAnalytics = (trackingObj,$this) => {
    const eventObj = {
      eventType: 'navigation click',
      event: 'Navigation'
    };

    let linkClickObject = null;
    const linkName = $this.data('link-title');

    const linkType = $this.find('i.is-external').length
      ? 'external'
      : 'internal';

    linkClickObject = {
      linkType,
      linkSection: 'Hyperlink Click',
      linkParentTitle: '',
      linkName
    };

    trackAnalytics(
      trackingObj,
      'navigation',
      'navigationClick',
      undefined,
      false,
      eventObj,
      linkClickObject
    );
  }

  handleSectionMenuItemMouseOver = (e) => {
    const { $overlay } = this.cache;
    const $target = $(e.target);
    const $this = $target.closest('.js-section-menu-navigation-Link');
    const $sectionMenuItemAnchor = $this.children('a');
    if(isDesktop()){
      $sectionMenuItemAnchor.attr('href',$sectionMenuItemAnchor.data('url-link'));
    }
    $this.find('.js-sub-menu-navigation-Link').addClass('show').attr('aria-hidden','false').attr('aria-expanded','true'); // value changed because of one extra div added
    $sectionMenuItemAnchor.children('.with-arrow').addClass('icon-up');
    $sectionMenuItemAnchor.children('.with-arrow').length && $overlay.removeClass('d-none');
    /* check modal view port position for web */
    const modalDiv = $this.find('.web-sub-section-custom-class.js-sub-menu-navigation-Link');
    const modalPoints = modalDiv[0] && modalDiv[0].getBoundingClientRect();
    if(modalPoints && modalPoints.left < 0){
      modalDiv.addClass('show-modal-from-left');
    } else if(modalPoints && modalPoints.right > (window.innerWidth || document.documentElement.clientWidth)){
      modalDiv.addClass('show-modal-from-right');
    }
  }

  handleSectionMenuItemMouseOut = (e) => {
    const { $overlay } = this.cache;
    const $target = $(e.target);
    const $this = $target.closest('.js-section-menu-navigation-Link');
    const $sectionMenuItemAnchor = $this.children('a');
    $this.find('.js-sub-menu-navigation-Link').removeClass('show').attr('aria-hidden', 'true').attr('aria-expanded','false');
    $sectionMenuItemAnchor.children('.with-arrow').removeClass('icon-up');
    $overlay.addClass('d-none');
  }

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default SectionMenu;
