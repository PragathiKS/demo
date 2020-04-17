import $ from 'jquery';
class RichText {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$anchorLink = this.root.find('a');
    this.addIcon();
    this.cache.$attributeDivId = this.root.find('.tp-pw-richText-wrapper');
  }
  bindEvents() {
    const { $anchorLink } = this.cache;
    $anchorLink.on('click', this.trackAnalytics);
  }

  trackAnalytics = e => {
    const $this = $(e.target);
    const anchorText = $this.text();
    this.cache.$attributeDivId.attr('data-link-name', '');
    this.cache.$attributeDivId.attr('data-link-name', anchorText);
    return true;
  };

  addIcon = () => {
    const { $anchorLink } = this.cache;
    $.each($anchorLink, function() {
      const $this = $(this);
      $this.append(`<i class="icon icon-Circle_Arrow_Right"></i>`);
    });
  };

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default RichText;
