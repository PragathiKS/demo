import $ from 'jquery';
import { isDesktopMode,getLinkClickAnalytics,addLinkAttr } from '../../../scripts/common/common';

class Banner {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$itbLink = this.root.find('.js-banner-analytics');
    this.cache.$existingBanner=this.root.find('.pw-banner__content.banner-parent');
    this.cache.$siblingBanner=this.root.find('.pw-banner__content.banner-sibling');
  }
  bindEvents() {
    const { $itbLink } = this.cache;
    if (
      isDesktopMode() ) {
      const { $existingBanner } = this.cache;
      const { $siblingBanner }= this.cache;

      $(window).on('load resize',function(){
        const bannerHeight = $existingBanner.outerHeight();
        const bannerWidth = $existingBanner.outerWidth();
        $siblingBanner.css('width',bannerWidth);
        $siblingBanner.css('height',bannerHeight);
      });
    }
    $itbLink.on('click', this.trackAnalytics);
  }
  trackAnalytics = (e) => {
    e.preventDefault();
    getLinkClickAnalytics(e, 'link-banner-title','Hero Image','.js-banner-analytics');
  }

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    addLinkAttr('.js-banner-analytics');
  }
}

export default Banner;
