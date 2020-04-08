import $ from 'jquery';

class TextVideo {
  constructor({ el }) {
    this.root = $(el);
    //this.toggleFlag = false;
    //this.toggleButtonId = '#toggle-button';
  }

  cache = {};

  initCache() {
    // eslint-disable-next-line no-console
    console.log('JS init cache called::');
    // this.cache.$mobileMenu = this.root.find('.js-tp-pw-mobile-navigation');
    // this.cache.$hamburgerToggle = this.root.find('.js-tp-pw-header__hamburger');
    // this.cache.$headerLogoPlaceholder = this.root.find('.js-tp-pw-header-logo-digital-data');
  }

  bindEvents() {
    // const { $hamburgerToggle, $headerLogoPlaceholder } = this.cache;
    // $hamburgerToggle.on('click', this.openMobileMenuBoxToggle);
    // $headerLogoPlaceholder.on('click', this.trackAnalytics);
    // $(window).on('resize', this.hideMobileMenuOnResize);
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
    if (window.digitalData) {
      $.extend(window.digitalData, {
        linkClick: {
          linkType: 'internal',
          linkSection: $this.data('link-section'),
          linkParentTitle: $this.data('link-parent-title'),
          linkName: $this.data('link-name')
        }
      });
      if (window._satellite) {
        window._satellite.track('linkClick');
      }
    }

    if($this.data('link-section') === 'logo') {
      var url = $this.attr('href');
      window.open(url, $this.attr('target'));
    }
  }

  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default TextVideo;
