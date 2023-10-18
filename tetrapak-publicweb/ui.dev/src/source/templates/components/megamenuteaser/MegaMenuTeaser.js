import $ from 'jquery';

class MegaMenuTeaser {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};

  initCache() {
    this.cache = {};
  }

  bindEvents() {
    /*add code here*/
  }

  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default MegaMenuTeaser;
