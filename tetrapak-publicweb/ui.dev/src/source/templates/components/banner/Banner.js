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
        $bEl.find('.pw-banner__image-wrapper').css('cursor', 'pointer');
        $bEl.find('.pw-banner__contentwrapper').css('cursor', 'pointer');
      }
    });


    $bEl.on('click', this.trackBannerImageClick);

  }

  seoChanges() {
    const titleDiv = this.root.find('.pw-banner__content__title');
    const h1tag = titleDiv.find('h1') ;
    if( h1tag.length) {
      $(h1tag).attr('class','tpatom-heading tpatom-heading--regular');
      const h2Tag = titleDiv.find('h2')[0];
      h2Tag.parentNode.removeChild(h2Tag);
    }
  }

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.seoChanges();
    addLinkAttr('.js-banner-analytics');
    this.addBannerLink();
  }
}

export default Banner;
