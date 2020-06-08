import $ from 'jquery';
import 'bootstrap';
import { trackAnalytics } from '../../../scripts/utils/analytics';
import { dynMedia } from '../../../scripts/utils/dynamicMedia';

class Header {
  constructor({ el }) {
    this.root = $(el);
    this.toggleFlag = false;
    this.toggleButtonId = '#toggle-button';
  }

  cache = {};

  initCache() {
    this.cache.$mobileMenu = this.root.find('.js-tp-pw-mobile-navigation');
    this.cache.$hamburgerToggle = this.root.find('.js-tp-pw-header__hamburger');
    this.cache.$headerLogoPlaceholder = this.root.find('.js-tp-pw-header-logo-digital-data');
    this.cache.$headerLogoTracker = this.root.find('.tp-pw-header__logo-placeholder');
    this.cache.$hoverMenuLink = this.root.find('.js-hover-menu-link');
    this.cache.$clickMenuLink = this.root.find('.js-click-menu-link');
    this.cache.$headerMobile = this.root.find('.tp-pw-header__container');
    this.cache.$megaMenuDesktop = this.root.find('.tp-pw-header__container .pw-megamenu');
    this.cache.$megaMenuMobile = this.root.find('.pw-megamenu');
    this.cache.$parentNavElement = this.root.find('.tp-pw-header__main-navigation.col-6');
    this.cache.$menuCloseSol = this.root.find('.js-close-menu-solution');
    this.cache.$bottomTeaserH = this.root.find('.js-bottom-teaser-list');
    this.cache.$headerItem = this.root.find('.js-tp-pw-header-item');
    this.cache.$headerItemLink = this.root.find('.js-main-menu-link-hover');
    this.cache.$overlay = $('.js-pw-overlay');
  }

  bindEvents() {
    const { $hamburgerToggle, $headerLogoPlaceholder, $headerItem, $headerLogoTracker} = this.cache;
    $hamburgerToggle.on('click', this.openMobileMenuBoxToggle);
    $headerLogoPlaceholder.on('click', this.trackAnalytics);
    $(window).on('resize', this.hideMobileMenuOnResize);
    this.cache.$hoverMenuLink.on('mouseover', this.handleMouseOver);
    this.cache.$hoverMenuLink.on('mouseout', this.handleMouseOut);
    this.cache.$headerItemLink.on('mouseover', this.handleHeaderItemMouseOver);
    this.cache.$headerItemLink.on('mouseout', this.handleHeaderItemMouseOut);
    this.cache.$clickMenuLink.on('click', this.handleMenuClick);
    this.cache.$menuCloseSol.on('click', this.handleCloseSolEvent);
    $headerItem.on('click', this.trackNavigationAnalytics);
    $headerLogoTracker.on('click', this.trackBrandLogo);
    $('.js-tp-pw-header-item:not(.js-click-menu-link)').on('click', this.handleMainNavClick);
    this.root.find('.js-header__selected-lang-pw').on('click', (e) => {
      this.root.find('.js-lang-modal').trigger('showlanuagepreferencepopup-pw');
      this.trackLanguageSelector(e);
    });
  }

  trackLanguageSelector = e => {
    const $target = $(e.target).closest('.js-header__selected-lang-pw');
    const trackingObj = {
      linkType: 'internal',
      linkSection: 'Header-language selector',
      linkParentTitle: '',
      linkName: $target.data('language')
    };
    const eventObj = {
      eventType: 'linkClick',
      event: 'Header'
    };
    trackAnalytics(trackingObj, 'linkClick', 'linkClick', undefined, false, eventObj);
  }

  handleMouseOver = () => {
    const { $megaMenuDesktop, $overlay } = this.cache;
    $megaMenuDesktop.addClass('d-block').attr('aria-hidden','false').attr('aria-expanded','true');
    $overlay.removeClass('d-none');
    dynMedia.processImages();
  }

  handleHeaderItemMouseOver = (e) => {
    const $target = $(e.target);
    const $this = $target.closest('.js-main-menu-link-hover');
    if($this.children('.active').length > 0){
      return false;
    }
    $this.children('.pw-navigation').addClass('show').attr('aria-hidden','false').attr('aria-expanded','true');
  }

  handleHeaderItemMouseOut = (e) => {
    const $target = $(e.target);
    const $this = $target.closest('.js-main-menu-link-hover');
    $this.children('.pw-navigation').removeClass('show').attr('aria-hidden', 'true').attr('aria-expanded','false');
  }

  handleMouseOut = () => {
    const { $megaMenuDesktop, $overlay } = this.cache;
    $megaMenuDesktop.removeClass('d-block').attr('aria-hidden', 'true').attr('aria-expanded','false');
    $overlay.addClass('d-none');
  }

  handleMenuClick = (e) => {
    e.preventDefault();
    const { $megaMenuMobile } = this.cache;
    $megaMenuMobile.removeClass('is-close');
    $megaMenuMobile.addClass('is-open');
    // this.cache.$headerMobile.addClass('d-none');
  }

  handleCloseSolEvent = () => {
    const { $megaMenuMobile } = this.cache;
    $megaMenuMobile.removeClass('is-open');
    $megaMenuMobile.addClass('is-close');
    // this.cache.$headerMobile.removeClass('d-none');
  }

  hideMobileMenuOnResize = () => {
    this.cache.$mobileMenu.fadeOut(10);
    this.cache.$hamburgerToggle.children(this.toggleButtonId).removeClass('icon-Close');
    this.cache.$hamburgerToggle.children(this.toggleButtonId).addClass('icon-Burger_pw');
    this.toggleFlag = false;
  }

  openMobileMenuBoxToggle = () => {
    if(!this.toggleFlag){
      this.cache.$mobileMenu.fadeIn(300);
      this.cache.$hamburgerToggle.children(this.toggleButtonId).removeClass('icon-Burger_pw');
      this.cache.$hamburgerToggle.children(this.toggleButtonId).addClass('icon-Close');
      this.toggleFlag = true;
      $('body').css('overflow','hidden');
    }else {
      this.cache.$mobileMenu.fadeOut(300);
      this.cache.$hamburgerToggle.children(this.toggleButtonId).removeClass('icon-Close');
      this.cache.$hamburgerToggle.children(this.toggleButtonId).addClass('icon-Burger_pw');
      this.toggleFlag = false;

      //hide other navigation on close
      const { $megaMenuMobile, $bottomTeaserH } = this.cache;
      $bottomTeaserH.removeClass('active').addClass('hide');
      $megaMenuMobile.removeClass('is-open');
      $megaMenuMobile.addClass('is-close');
      $('body').css('overflow','auto');
    }
  }

  handleMainNavClick =(e) => {
    e.preventDefault();
    const $target = $(e.target);
    const $this = $target.closest('.js-tp-pw-header-item');
    window.open($this.attr('href'), '_self');
  }

  trackBrandLogo = (e) => {
    const $target = $(e.target);
    const $this = $target.closest('.js-tp-pw-header-logo-digital-data');
    const url = $this.attr('href');
    const targetLink = $this.attr('target');
    const linkType = targetLink === '_blank'? 'external' :'internal';
    const trackingObj = {
      linkType,
      linkSection: 'Brand logo',
      linkParentTitle: '',
      linkName: 'TetraPak'
    };
    const eventObj = {
      eventType: 'linkClick',
      event: 'Header'
    };
    trackAnalytics(trackingObj, 'linkClick', 'linkClick', undefined, false, eventObj);

    if(url && targetLink){
      window.open(url, targetLink);
    }
  }

  trackNavigationAnalytics = (e) => {
    const $target = $(e.target);
    const $this = $target.closest('.js-tp-pw-header-item');
    const navigationLinkName = $this.text().trim();
    const linkType = $this.attr('target') === '_blank'? 'external' :'internal';

    const trackingObj = {
      navigationLinkName,
      navigationSection: 'Header Navigation'
    };

    const linkClickTrackingobj = {
      linkType,
      linkSection: 'Header Navigation',
      linkParentTitle:'',
      linkName: navigationLinkName
    };

    const eventObj = {
      eventType: 'linkClick',
      event: 'Header'
    };
    trackAnalytics(
      trackingObj,
      'navigation',
      'navigationClick',
      undefined,
      false,
      eventObj,
      linkClickTrackingobj
    );
  }

  trackAnalytics = (e) => {
    e.preventDefault();
    const $target = $(e.target);
    const $this = $target.closest('.js-tp-pw-header-logo-digital-data');
    const targetLink = $this.attr('target');
    const url = $this.attr('href');

    const linkName = $this.data('link-name');
    if(linkName==='contact us envelope') {
      const trackingObj = {
        linkSection: 'Find my office',
        linkParentTitle: '',
        linkName: 'Contact Us'
      };
      const eventObj = {
        eventType: 'linkClick',
        event: 'findmyoffice'
      };
      trackAnalytics(trackingObj, 'linkClick', 'linkClick', undefined, false, eventObj);
    }

    if(url && targetLink){
      window.open(url, targetLink);
    }
  }

  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default Header;
