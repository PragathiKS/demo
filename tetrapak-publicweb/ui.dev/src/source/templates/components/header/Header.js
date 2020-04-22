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
  }

  bindEvents() {
    const { $hamburgerToggle, $headerLogoPlaceholder } = this.cache;
    $hamburgerToggle.on('click', this.openMobileMenuBoxToggle);
    $headerLogoPlaceholder.on('click', this.trackAnalytics);
    $(window).on('resize', this.hideMobileMenuOnResize);
  }

  hideMobileMenuOnResize = () => {
    this.cache.$mobileMenu.fadeOut(10);
    this.cache.$hamburgerToggle.children(this.toggleButtonId).removeClass('icon-Close');
    this.cache.$hamburgerToggle.children(this.toggleButtonId).addClass('icon-Burger');
    this.toggleFlag = false;
  }

  openMobileMenuBoxToggle = () => {
    if(!this.toggleFlag){
      this.cache.$mobileMenu.fadeIn(300);
      this.cache.$hamburgerToggle.children(this.toggleButtonId).removeClass('icon-Burger');
      this.cache.$hamburgerToggle.children(this.toggleButtonId).addClass('icon-Close');
      this.toggleFlag = true;
    }else {
      this.cache.$mobileMenu.fadeOut(300);
      this.cache.$hamburgerToggle.children(this.toggleButtonId).removeClass('icon-Close');
      this.cache.$hamburgerToggle.children(this.toggleButtonId).addClass('icon-Burger');
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
