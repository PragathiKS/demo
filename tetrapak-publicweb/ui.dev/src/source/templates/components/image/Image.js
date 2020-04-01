import $ from 'jquery';

class Image {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$imageAnchor = this.root.find('.js-tp-pw-image');
  }
  bindEvents() {
    const { $imageAnchor } = this.cache;
    $imageAnchor.on('click', this.trackAnalytics);
  }

  trackAnalytics = (e) => {
    e.preventDefault();
    const $target = $(e.target);
    const $this = $target.closest('.js-tp-pw-image');
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

    window.open($this.attr('href'), '_self');
  }


  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default Image;
