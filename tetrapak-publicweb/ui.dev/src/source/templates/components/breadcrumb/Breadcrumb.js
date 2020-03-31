import $ from 'jquery';

class Breadcrumb {
  constructor({ el }) {
    this.root = $(el);
  }

  cache = {};

  initCache() {
    this.cache.$breadcrumbLink = this.root.find('.js-tp_pw-breadcrumb__link');
  }

  bindEvents() {
    const { $breadcrumbLink } = this.cache;
    $breadcrumbLink.on('click', this.trackAnalytics);
  }

  trackAnalytics = (e) => {
    e.preventDefault();
    const $target = $(e.target);
    const $this = $target.closest('.js-tp_pw-breadcrumb__link');
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

    //if($this.data('link-section') === 'logo') {
    // var url = $this.attr('href');
    // window.open(url);
    //}
  }

  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default Breadcrumb;
