import $ from 'jquery';
import { isExternal } from '../../../scripts/utils/updateLink';


class Teaser {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$teaserLink = this.root.find('.js-teaser-analytics');
    this.cache.$teaserImage = this.root.find('.carousal-wrapper img');
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
  }

  /**
   * After Initialization of Teaser navigation
  */
  onInitialized = () => {
    this.trackArrowAnalytics();
    this.cache.$teaserLink = this.root.find('.js-teaser-analytics');
    this.cache.$teaserLink.off().on('click', this.trackAnalytics);
  }

  /**
   * ReInitialization of Teaser navigation on Resize
  */
   onResize = () => {
     const widthOnResize = window.innerWidth;
     this.cache.option['stagePaddingRight'] = widthOnResize > 1023 ? 0 : 62;
   }

   init() {
       this.initCache();
       this.bindEvents();
    }
}

export default Teaser;
