import $ from 'jquery';
import { isDesktopMode,getLinkClickAnalytics} from '../../../scripts/common/common';
class Banner {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$itbLink = this.root.find('.js-banner-analytics');
    this.cache.bannerContainer=$('body').find('.bannercontainer');
    this.cache.$existingBanner = this.root.find('.pw-banner__content.banner-parent');
    this.cache.$siblingBanner = this.root.find('.pw-banner__content.banner-sibling');
    this.cache.componentName = this.root.find('.componentName-banner').val();
    this.cache.currentElement=0 ;

    if (this.cache.bannerContainer.length){
      this.cache.headerHeight= $('body').find('.tp-pw-header').height();
      this.cache.topElement = this.cache.headerHeight;
      this.cache.calculatedHeight=0 ;
      this.cache.eles = document.getElementsByClassName('banner-stack');
      this.cache.lastElement = this.cache.eles.length -1;
      this.cache.previousElement = this.cache.eles[this.cache.lastElement -1];
    }
  }

  bindEvents() {
    const { $itbLink } = this.cache;
    if (
      isDesktopMode()) {
      const { $existingBanner, $siblingBanner } = this.cache;
      let bannerHeight = $existingBanner.outerHeight();
      let bannerWidth = $existingBanner.outerWidth();

      $siblingBanner.css('width', bannerWidth);
      $siblingBanner.css('height', bannerHeight);

      $(window).on('resize', () => {
        bannerHeight = $existingBanner.outerHeight();
        bannerWidth = $existingBanner.outerWidth();
        $siblingBanner.css('width', bannerWidth);
        $siblingBanner.css('height', bannerHeight);
      });
    }

    $itbLink.off().on('click', this.trackAnalytics);
  }

  trackAnalytics = (e) => {
    e.preventDefault();
    getLinkClickAnalytics(e, 'link-banner-title','Hero Image','.js-banner-analytics');
  }

  trackBannerImageClick = (e) => {
    e.preventDefault();
    const $target = $(e.target);

    if($target.parents('.pw-banner').find('img.js-dynamic-media')){
      const $this = $target.closest('.pw-banner');
      const $anchor = $this.data('href');
      if (!($anchor && $anchor !== '#')) {
        return false;
      }
      if ($(e.target).closest('.pw-banner__content').length) {
        return true;
      }

      getLinkClickAnalytics(e, 'link-banner-title','Hero Image','.pw-banner', false);

      if ((e.metaKey || e.ctrlKey || e.keyCode === 91 || e.keyCode === 224)) {
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
        $bEl.find('.pw-banner__image-wrapper').css('cursor', 'pointer');
        $bEl.find('.pw-banner__contentwrapper').css('cursor', 'pointer');
      }
    });


    $bEl.off().on('click', this.trackBannerImageClick);

  }


  init() {
    /* Mandatory method */
    if(($('body').find('.tp-container-hero').length > 1 || $('body').find('.tp-container-hero-wide').length > 1) && ($('body').find('.bannercontainer').length)) {
      $('.tp-container-hero , .tp-container-hero-wide').each(function(){
        $(this).parent().addClass('banner-stack');
      });
    }
    this.initCache();
    this.bindEvents();
    this.addBannerLink();
  }
}

export default Banner;
