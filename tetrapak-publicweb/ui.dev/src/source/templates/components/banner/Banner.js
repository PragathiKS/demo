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
    this.cache.$pwBanner = this.root.find('.js-pw-banner');
    this.cache.$existingBanner = this.root.find('.pw-banner__content.banner-parent');
    this.cache.$siblingBanner = this.root.find('.pw-banner__content.banner-sibling');
    this.cache.$sideSection = this.root.find('.pw-banner__sideSection.left');
    this.cache.$sideSectionright = this.root.find('.pw-banner__sideSection.right');
    this.cache.componentName = this.root.find('.componentName-banner').val();
  }

  bindEvents() {
    const { $itbLink } = this.cache;
    if (
      isDesktopMode()) {
      const { $existingBanner } = this.cache;
      const { $siblingBanner } = this.cache;
      const { $sideSection } = this.cache;
      const { $sideSectionright } = this.cache;
      const { $pwBanner } = this.cache;
      if($sideSection.length || $sideSectionright.length) {
        $pwBanner.css({'max-width':window.screen.availWidth,'margin-left':'auto','margin-right':'auto'});
      }
      $(window).on('load resize', function () {
        const zoomLevel = (( window.outerWidth) / window.innerWidth) * 100;
        const bannerHeight = $existingBanner.outerHeight();
        const bannerWidth = $existingBanner.outerWidth();
        const bannerOffset = $existingBanner.offset();
        const pwBannerContainerOffset = $pwBanner.offset();
        const windowWidth = $('body').outerWidth();
        $siblingBanner.css('width', bannerWidth);
        $siblingBanner.css('height', bannerHeight);
        if ($sideSection.length) {
          if(zoomLevel === 100) {
            $sideSection.css('width', bannerOffset.left +'px');
          } else {
            $sideSection.css('width', (bannerOffset.left - pwBannerContainerOffset.left)  +'px');
          }
          if($('.pw-banner-herowrapper').length) {
            $('.pw-banner-herowrapper').css('visibility','visible');
          }
        }
        if ($sideSectionright.length) {
          if(zoomLevel === 100) {
            const finalWidth = windowWidth - bannerOffset.left -  bannerWidth - 48;
            $sideSectionright.css('width', finalWidth +'px');
          } else {
            const pwContainerRightOffset = pwBannerContainerOffset.left + $pwBanner.outerWidth();
            const bannerRightOffset = bannerOffset.left + bannerWidth - 48;
            $sideSectionright.css('width', `${(pwContainerRightOffset - bannerRightOffset)}px`);
          }
          if($('.pw-banner-herowrapper').length) {
            $('.pw-banner-herowrapper').css('visibility','visible');
          }
        }
      });
    }
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

      if (isDownloable($anchor)) {
        $this.attr('target', '_blank');
      }

      if (isExternal($anchor)) {
        $this.attr('target', '_blank');
      }

      getLinkClickAnalytics(e, 'link-banner-title','Hero Image','.pw-banner', false);

      if (isExternal($anchor) || isDownloable || (e.metaKey || e.ctrlKey || e.keyCode === 91 || e.keyCode === 224)) {
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
    this.initCache();
    this.bindEvents();
    addLinkAttr('.js-banner-analytics');
    this.addBannerLink();
  }
}

export default Banner;