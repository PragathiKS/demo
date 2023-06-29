import $ from 'jquery';

class MegaMenuTeaser {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};

  initCache() {
    this.cache = {};
  }

  bindEvents() {}

  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default MegaMenuTeaser;
