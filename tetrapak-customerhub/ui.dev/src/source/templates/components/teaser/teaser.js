import $ from 'jquery';
import { addLinkAttr,getLinkClickAnalytics } from '../../../scripts/common/common';


class Teaser {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$teaserLink = this.root.find('.js-teaser-analytics');
    this.cache.option = {
      loop: true,
      nav: true,
      onResize: this.onResize,
      onInitialized: this.onInitialized,
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
