import $ from 'jquery';
import { isDesktopMode,getLinkClickAnalytics,addLinkAttr } from '../../../scripts/common/common';
import { isExternal, isDownloable  } from '../../../scripts/utils/updateLink';

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

  trackBannerImageClick = (e) => {
    const $target = $(e.target);
    const $this = $target.closest('.pw-banner');
    const $anchor = $this.data('href');
    if (!($anchor && $anchor !== '#')) {
      return false;
    }
    if ($(e.target).closest('.pw-banner__content').length) {
      return true;
    }

    if (isDownloable($anchor)) {
      $this.data('download-type', 'download');
    }

    if (isExternal($anchor)) {
      $this.attr('target', '_blank');
    }

    getLinkClickAnalytics(e, 'link-banner-title','Hero Image','.pw-banner', false);  


    if (isExternal($anchor)) {
      window.open($anchor, '_blank');
    } else {
      window.location.href = $anchor;
    }
  }

  addBannerLink() {
    const $bEl = $('.pw-banner');
    $bEl.each(function () {
      const $anchor = $bEl.data('href');
      if ($anchor && $anchor !== '#') {
        $bEl.css('cursor', 'pointer');
      }
    });


    $bEl.on('click', this.trackBannerImageClick);

    // $bEl.click((e) => {
    //   const $anchor = $bEl.data('href');
    //   if (!($anchor && $anchor !== '#')) {
    //     return false;
    //   }
    //   if ($(e.target).closest('.pw-banner__content').length) {
    //     return true;
    //   }

    //   if (isDownloable($anchor)) {
    //     $bEl.data('download-type', 'download');
    //   }

    //   if (isExternal($anchor)) {
    //     $bEl.attr('target', '_blank');
    //   }

    //   getLinkClickAnalytics(e, 'link-banner-title','Hero Image','.pw-banner', false);  


    //   if (isExternal($anchor)) {
    //     window.open($anchor, '_blank');
    //   } else {
    //     window.location.href = $anchor;
    //   }
    // });
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
