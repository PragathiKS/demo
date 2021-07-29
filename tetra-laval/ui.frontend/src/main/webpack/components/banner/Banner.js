import $ from 'jquery';
import { getLinkClickAnalytics, addLinkAttr } from '../../../scripts/common/common';
import { isExternal, isDownloable  } from '../../../scripts/utils/updateLink';
class Banner {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$itbLink = this.root.find('.js-banner-analytics');
    this.cache.componentName = this.root.find('.componentName-banner').val();
  }

  bindEvents() {
    const { $itbLink } = this.cache;
    $itbLink.off().on('click', this.trackAnalytics);

    // Open SoftConversion Form
    this.root.find('.js-softconversion-pw-banner').on('click', (e) => {
      getLinkClickAnalytics(e, 'link-banner-title','Hero Image','.js-softconversion-pw-banner', false);
      $('body').find('.'+this.cache.componentName).trigger('showsoftconversion-pw');
    });

    // Open Subscription Form
    this.root.find('.js-subscription-pw-banner').on('click', (e) => {
      getLinkClickAnalytics(e, 'link-banner-title','Hero Image','.js-subscription-pw-banner', false);
      $('body').find('.'+this.cache.componentName).trigger('showSubscription-pw');
    });

  }

  trackAnalytics = (e) => {
    e.preventDefault();
    getLinkClickAnalytics(e, 'link-banner-title','Hero Image','.js-banner-analytics');
  }

  trackBannerImageClick = (e) => {
    const $target = $(e.target);
    if($target.hasClass('js-dynamic-media')){
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

      if (isExternal($anchor) || (e.metaKey || e.ctrlKey || e.keyCode === 91 || e.keyCode === 224)) {
        window.open($anchor, '_blank');
      } else {
        window.location.href = $anchor;
      }
    }
  }

  addBannerLink() {
    const $bEl = $('.pw-banner');
    $bEl.each(function () {
      const $anchor = $bEl.data('href');
      if ($anchor && $anchor !== '#') {
        $bEl.css('cursor', 'pointer');
        $bEl.find('.pw-banner__content_wrapper').css('cursor', 'pointer');
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
      h2Tag && h2Tag.parentNode.removeChild(h2Tag);
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
