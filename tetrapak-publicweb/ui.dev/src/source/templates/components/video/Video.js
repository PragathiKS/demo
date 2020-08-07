import $ from 'jquery';

class Video {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    // define initial method
  }
  bindEvents() {
    /* Bind jQuery events here */
    /**
     * Example:
     * const { $submitBtn } = this.cache;
     * $submitBtn.on('click', () => { ... });
     */
  }

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default Video;
