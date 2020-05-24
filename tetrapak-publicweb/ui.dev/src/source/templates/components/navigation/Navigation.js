import $ from 'jquery';

class Navigation {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$stickySectionMenu = this.root.closest('.sticky-section-menu');
  }
  bindEvents() {
    const { $stickySectionMenu } = this.cache;
    if($stickySectionMenu.length > 0){
      $('.body-content').addClass('body-top-padding');
    }
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default Navigation;
