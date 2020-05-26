import $ from 'jquery';

class Navigation {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$stickySectionMenu = this.root.closest('.sticky-section-menu');
    this.cache.$navigationElement= this.root;
  }
  bindEvents() {
    const { $stickySectionMenu } = this.cache;
    if ($stickySectionMenu.length > 0) {
      $('.body-content').addClass('body-top-padding');
    }
    this.showSelectedHeader();
  }
  showSelectedHeader = () => {
    const { $navigationElement } = this.cache;
    $('.js-tp-pw-header-item-desktop').each(function() {
      if ($(this).data('link-name') === $navigationElement.data('selected-header')) {
        $(this).addClass('active');
      }
    });
  };
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default Navigation;
