import $ from 'jquery';

class Breadcrumb {
  constructor({ el }) {
    this.root = $(el);
  }

  cache = {};

  initCache() {
    // eslint-disable-next-line no-console
    console.log('breadcrumbs initCache');
    // this.cache.$mobileMenu = this.root.find('.js-tp-pw-mobile-navigation');
    // this.cache.$hamburgerToggle = this.root.find('.js-tp-pw-header__hamburger');
    this.cache.$breadcrumbLink = this.root.find('.js-tp_pw-breadcrumb__link');
  }

  bindEvents() {
    const { $breadcrumbLink } = this.cache;
    // $hamburgerToggle.on('click', this.openMobileMenuBoxToggle);
    $breadcrumbLink.on('click', this.trackAnalytics);
    // $(window).on('resize', this.hideMobileMenuOnResize);
  }

  trackAnalytics = (e) => {
    e.preventDefault();
    const $target = $(e.target);
    const $this = $target.closest('.js-tp_pw-breadcrumb__link');
    // eslint-disable-next-line no-console
    console.log('link clicked:::',$this.data('link-name'));
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
        window._satellite.track('linkClicked');
      }
    }

    //if($this.data('link-section') === 'logo') {
    var url = $this.attr('href');
    window.open(url);
    //}
  }

  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default Breadcrumb;
