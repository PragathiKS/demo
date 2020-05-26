import $ from 'jquery';
class RichText {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$anchorLink = this.root.find('a');
    this.cache.$attributeDivId = this.root.find('.tp-pw-richText-wrapper');
  }
  bindEvents() {
    const { $anchorLink } = this.cache;
    $anchorLink.on('click', this.trackAnalytics);
  }

  trackAnalytics = e => {
    const $this = $(e.target);
    const anchorText = $this.text();
    this.cache.$attributeDivId.attr('data-link-name', anchorText);
    return true;
  };

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default RichText;
