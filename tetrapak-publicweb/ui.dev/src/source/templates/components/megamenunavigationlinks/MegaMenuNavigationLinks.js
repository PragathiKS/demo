import $ from 'jquery';

class MegaMenuNavigationLinks {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  
  initCache() {
    this.cache = {
      linkTargetClass: '.tp-pw-megamenunavigationlinks__item'
    };
  }

  getDivsWithEmptyLinks = () => this.root.find(`${this.cache.linkTargetClass}> a`).not('[href]').parent()

  replaceEmptyLinksWithSubheaders = () => {
    this.getDivsWithEmptyLinks().each(function() {
      $(this).find('i').addClass('subheader-prefix');
      $(this).find('a').addClass('subheader').append('<i class="subheader-icon icon-Chevron_Right tp_icon"></i>');
    });
  }


  bindEvents() {
    this.replaceEmptyLinksWithSubheaders();
  }
  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default MegaMenuNavigationLinks;
