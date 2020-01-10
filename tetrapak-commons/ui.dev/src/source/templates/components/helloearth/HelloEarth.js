import $ from 'jquery';

class HelloEarth {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  init() {
    this.initCache();
    this.bindEvents();
  }
  initCache() {
    // TODO: Initialize cache
  }
  bindEvents() {
    // TODO: Bind events
  }
}

export default HelloEarth;
