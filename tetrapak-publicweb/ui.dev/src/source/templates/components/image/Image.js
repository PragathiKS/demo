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
    window.open($this.attr('href'), '_self');
  }


  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default Image;
