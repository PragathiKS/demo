import $ from 'jquery';

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
    this.cache.$hoverMenuLink = this.root.find('.js-hover-menu-link');
    this.cache.$clickMenuLink = this.root.find('.js-click-menu-link');
    this.cache.$headerMobile = this.root.find('.tp-pw-header__container');
    this.cache.$megaMenuDesktop = this.root.find('.tp-pw-header__container .pw-megamenu');
    this.cache.$megaMenuMobile = this.root.find('.pw-megamenu');
    this.cache.$parentNavElement = this.root.find('.tp-pw-header__main-navigation.col-6');
    this.cache.$menuCloseSol = this.root.find('.js-close-menu-solution');
    this.cache.$overlay = $('.js-pw-overlay');
    this.cache.$body = $('body');

  }

  bindEvents() {
    const { $hamburgerToggle, $headerLogoPlaceholder} = this.cache;
    $hamburgerToggle.on('click', this.openMobileMenuBoxToggle);
    $headerLogoPlaceholder.on('click', this.trackAnalytics);
    $(window).on('resize', this.hideMobileMenuOnResize);
    this.cache.$hoverMenuLink.on('mouseover', this.handleMouseOver);
    this.cache.$hoverMenuLink.on('mouseout', this.handleMouseOut);
    this.cache.$clickMenuLink.on('click', this.handleMenuClick);
    this.cache.$menuCloseSol.on('click', this.handleCloseSolEvent);

  }

  handleMouseOver = () => {
    const { $megaMenuDesktop, $parentNavElement, $overlay, $body } = this.cache;
    $parentNavElement.addClass('pw-position-static');
    $megaMenuDesktop.addClass('d-block').attr('aria-hidden','false').attr('aria-expanded','true');
    $body.addClass('pw-position-relative');
    $overlay.removeClass('d-none');
  }

  handleMouseOut = () => {
    const { $megaMenuDesktop,$parentNavElement, $overlay,$body } = this.cache;
    $megaMenuDesktop.removeClass('d-block').attr('aria-hidden', 'true').attr('aria-expanded','false');
    $parentNavElement.removeClass('pw-position-static');
    $body.removeClass('pw-position-relative');
    $overlay.addClass('d-none');
  }

  handleMenuClick = () => {
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
    }else {
      this.cache.$mobileMenu.fadeOut(300);
      this.cache.$hamburgerToggle.children(this.toggleButtonId).removeClass('icon-Close');
      this.cache.$hamburgerToggle.children(this.toggleButtonId).addClass('icon-Burger_pw');
      this.toggleFlag = false;
    }
  }

  trackAnalytics = (e) => {
    e.preventDefault();
    const $target = $(e.target);
    const $this = $target.closest('.js-tp-pw-header-logo-digital-data');
    const targetLink = $this.attr('target');
    const url = $this.attr('href');

    if(targetLink === '_blank'){
      window._satellite.track('linkClick');
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
