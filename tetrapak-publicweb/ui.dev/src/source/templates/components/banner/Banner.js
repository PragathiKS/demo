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
    this.cache.bannerContainer=$('body').find('.bannercontainer');
    this.cache.$existingBanner = this.root.find('.pw-banner__content.banner-parent');
    this.cache.$siblingBanner = this.root.find('.pw-banner__content.banner-sibling');
    this.cache.$sideSection = this.root.find('.pw-banner__sideSection.left');
    this.cache.$sideSectionright = this.root.find('.pw-banner__sideSection.right');
    this.cache.componentName = this.root.find('.componentName-banner').val();
    this.cache.currentElement=0 ;
    this.cache.stickyHeight= $('body').find('.pw-navigation.sticky').css('top');

    if (this.cache.bannerContainer.length){
      if($('.pw-navigation.sticky').length &&  isDesktopMode()){
        this.cache.headerHeight= $('body').find('.tp-pw-header').height() + parseInt(this.cache.stickyHeight ,10);
        this.cache.topElement = this.cache.headerHeight - 20;
      }
      else if ($('.pw-navigation.sticky').length &&  !(isDesktopMode())){
        this.cache.headerHeight= $('body').find('.tp-pw-header').height() + parseInt(this.cache.stickyHeight ,10);
        this.cache.topElement = this.cache.headerHeight - 8;
      }
      else {
        this.cache.headerHeight= $('body').find('.tp-pw-header').height();
        this.cache.topElement = this.cache.headerHeight;
      }
      this.cache.calculatedHeight=0 ;
      this.cache.eles = document.getElementsByClassName('banner-stack');
      this.cache.lastElement = this.cache.eles.length -1;
      this.cache.previousElement = this.cache.eles[this.cache.lastElement -1];
    }
  }

  setSideSection = () => {
    const { $existingBanner, $sideSection, $sideSectionright, $pwBanner } = this.cache;
    const bannerWidth = $existingBanner.outerWidth();
    const zoomLevel = (( window.outerWidth) / window.innerWidth) * 100;
    const bannerOffset = $existingBanner.offset();
    const pwBannerContainerOffset = $pwBanner.offset();
    const windowWidth = $('body').outerWidth();

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
        const bannerRightOffset = bannerOffset.left + bannerWidth;
        $sideSectionright.css('width', `${(pwContainerRightOffset - bannerRightOffset)}px`);
      }
      if($('.pw-banner-herowrapper').length) {
        $('.pw-banner-herowrapper').css('visibility','visible');
      }
    }
  }

  bindEvents() {
    const { $itbLink } = this.cache;
    if (
      isDesktopMode()) {
      const { $sideSection, $sideSectionright, $pwBanner } = this.cache;

      if($sideSection.length || $sideSectionright.length) {
        $pwBanner.css({'max-width':window.screen.availWidth,'margin-left':'auto','margin-right':'auto'});
      }

      this.setSideSection();

      $(window).on('resize', () => {
        this.setSideSection();
      });
    }

    if(this.cache.eles && this.cache.bannerContainer){
      if(this.cache.eles.length > 1) {
        window.addEventListener('scroll', this.onScroll, false);
      }
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
  onScroll = () =>{
    const scrollBarPosition = window.pageYOffset || document.body.scrollTop;
    this.cache.calculatedHeight = this.cache.eles[this.cache.currentElement].offsetTop - this.cache.currentElement *16;
    if(scrollBarPosition > 0 && scrollBarPosition >= this.cache.calculatedHeight) {
      if(this.cache.currentElement < this.cache.lastElement) {
        $(this.cache.eles[this.cache.currentElement]).css('top', this.cache.topElement +'px');   
        this.cache.topElement=this.cache.topElement + 16;
        $(this.cache.eles[this.cache.currentElement]).addClass('fixed');
        this.cache.currentElement++;
      }
      else if(this.cache.currentElement === this.cache.lastElement){
        $(this.cache.eles[this.cache.currentElement]).css('top', this.cache.topElement +'px');   
        $(this.cache.eles[this.cache.lastElement]).addClass('fixed');
        $(this.cache.eles[this.cache.lastElement]).css('top', this.cache.topElement +'px'); 
      }
    }
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
    addLinkAttr('.js-banner-analytics');
    this.addBannerLink();
  }
}

export default Banner;