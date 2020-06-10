import $ from 'jquery';
import { isDesktopMode,getLinkClickAnalytics,addLinkAttr } from '../../../scripts/common/common';
import { isExternal } from '../../../scripts/utils/updateLink';

class Banner {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$itbLink = this.root.find('.js-banner-analytics');
    this.cache.$existingBanner = this.root.find('.pw-banner__content.banner-parent');
    this.cache.$siblingBanner = this.root.find('.pw-banner__content.banner-sibling');
    this.cache.componentName = this.root.find('#componentName-banner').val();
  }

  bindEvents() {
    const { $itbLink } = this.cache;
    if (
      isDesktopMode()) {
      const { $existingBanner } = this.cache;
      const { $siblingBanner } = this.cache;

      $(window).on('load resize', function () {
        const bannerHeight = $existingBanner.outerHeight();
        const bannerWidth = $existingBanner.outerWidth();
        $siblingBanner.css('width', bannerWidth);
        $siblingBanner.css('height', bannerHeight);
      });
    }
    $itbLink.on('click', this.trackAnalytics);

    this.root.find('.js-softconversion-pw-banner').on('click', () => {
      $('body').find('.'+this.cache.componentName).trigger('showsoftconversion-pw');
    });

  }

  trackAnalytics = (e) => {
    e.preventDefault();
    getLinkClickAnalytics(e, 'link-banner-title','Hero Image','.js-banner-analytics');
  }

  addBannerLink() {
    const $bEl = $('.pw-banner');
    $bEl.each(function () {
      const $anchor = $bEl.data('href');
      if ($anchor && $anchor !== '#') {
        $bEl.css('cursor', 'pointer');
      }
    });


    $bEl.click((e) => {
      const $anchor = $bEl.data('href');
      if (!($anchor && $anchor !== '#')) {
        return false;
      }
      if ($(e.target).closest('.pw-banner__content').length) {
        return true;
      }
      if (isExternal($anchor)) {
        window.open($anchor, '_blank');
      } else {
        window.location.href = $anchor;
      }
    });
  }


  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    addLinkAttr('.js-banner-analytics');
    this.addBannerLink();
  }
}

export default Banner;
