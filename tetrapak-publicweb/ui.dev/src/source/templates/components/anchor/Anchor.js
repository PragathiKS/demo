import $ from 'jquery';
import { scrollToElement } from '../../../scripts/common/common';

class Anchor {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$anchorLink = this.root.find('a');
  }
  bindEvents() {
    const { $anchorLink } = this.cache;
    $anchorLink.on('click', this.scrollToSection);
  }

  scrollToSection = e => {
    e.preventDefault();
    const $this = $(e.target);
    const anchorId = $this.data('link-section');
    location.hash = anchorId;
    scrollToElement(null,`#${anchorId}`);
  };

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default Anchor;
