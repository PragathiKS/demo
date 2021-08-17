import $ from 'jquery';
import 'owlcarousel';
import { addLinkAttr,getLinkClickAnalytics } from '../../../scripts/common/common';


class Teaser {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$teaserLink = this.root.find('.js-teaser-analytics');
    this.cache.$teaserImage = this.root.find('.carousal-wrapper img');
    this.cache.$owl = this.root.find('.owl-carousel');
    this.cache.option = {
      loop: true,
      nav: true,
      onResize: this.onResize,
      onInitialized: this.onInitialized,
      navText:[
        '<i class="icon icon-Navigation_Right_pw"></i>',
        '<i class="icon icon-Navigation_Right_pw"></i>'],
      responsive: {
        0: {
          items: 1
        },
        1024: {
          items: 3
        }
      }
    };
  }
  bindEvents() {
    this.cache.$teaserLink.on('click', this.trackAnalytics);
    // Initialize carousal for teaser
    this.carousalInitialization();
  }

  /**
   * carousalInitialization of Teaser
  */
   carousalInitialization = () => {
     const widthOnResize = window.innerWidth;
     this.cache.option['stagePaddingRight'] = widthOnResize > 1023 ? 0 : 62;
     this.cache.$owl.owlCarousel(this.cache.option);
   }
  /**
   * After Initialization of Teaser navigation
  */
  onInitialized = () => {
    this.cache.$owlPrev = this.root.find('.owl-prev');
    this.cache.$owlNext = this.root.find('.owl-next');
    this.trackArrowAnalytics();
    this.cache.$teaserLink = this.root.find('.js-teaser-analytics');
    this.cache.$teaserLink.off().on('click', this.trackAnalytics);
    this.adjustNavArrow();
    $(window).on('resize orientationchange', () => {
      this.adjustNavArrow();
    });
  }

  /**
   * ReInitialization of Teaser navigation on Resize
  */
   onResize = () => {
     const widthOnResize = window.innerWidth;
     this.cache.option['stagePaddingRight'] = widthOnResize > 1023 ? 0 : 62;
     const { $owl } = this.cache;
     $owl.trigger('destroy.owl.carousel');
     $owl.html($owl.find('.owl-stage-outer').html()).removeClass('owl-loaded');
     $owl.owlCarousel(this.cache.option);
   }

  /**
   * Arrow analytics tracking
  */
  trackArrowAnalytics = () => {
    this.cache.$owlPrev = this.root.find('.owl-prev');
    this.cache.$owlNext = this.root.find('.owl-next');
    const dataObj = {
      'linkSection' : 'Teaser_hyperlink click',
      'linkType': 'internal'
    };
    const getAnalytics = (e,action) => {
      const $target = $(e.target);
      const $this = $target.closest('.owl-carousel');
      dataObj['linkParentTitle']= $this.data(
        'paren-link-title'
      ) || '';
      dataObj['linkName']= action;
      getLinkClickAnalytics(e,'paren-link-title','Teaser','.js-teaser-analytics',false,dataObj);
    };
    this.cache.$owlNext.on('click', function(e) {
      getAnalytics(e,'next');
    });
    this.cache.$owlPrev.on('click', function(e) {
      getAnalytics(e,'prev');
    });

  }

  /**
   * Align Arrows with Teaser image
  */
  adjustNavArrow = () => {
    const { $owlPrev, $owlNext,$teaserImage } = this.cache;
    let imageHeight = $teaserImage[0] && $teaserImage[0].clientWidth;
    // calculate height using 16:9 formula
    imageHeight = imageHeight/16;
    imageHeight = Math.floor(imageHeight*9);
    // 12px as half height of arrow
    $owlPrev.css('top', imageHeight/2 - 12);
    $owlNext.css('top', imageHeight/2 - 12);
  }

  trackAnalytics = (e) => {
    e.preventDefault();
    getLinkClickAnalytics(e,'paren-link-title','Teaser','.js-teaser-analytics');
  }

  init() {
    this.initCache();
    this.bindEvents();
    addLinkAttr('.js-teaser-analytics');
  }
}

export default Teaser;
