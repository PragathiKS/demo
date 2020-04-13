import $ from 'jquery';

class Anchor {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$anchorLink = this.root.find('a');
    this.cache.$attributeDivId = this.root.find('.pw-anchor-list');
  }
  bindEvents() {
    const { $anchorLink } = this.cache;
    $anchorLink.on('click', this.trackAnalytics);
  }

  trackAnalytics = e => {
    e.preventDefault();
    const $this = $(e.target);
    const anchorText = $this.text();
    this.cache.$attributeDivId.attr('data-link-name', '');
    this.cache.$attributeDivId.attr('data-link-name', anchorText);
  };

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default Anchor;
