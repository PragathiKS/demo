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
    window.open($this.attr('href'), '_self');
  }

  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default Breadcrumb;
