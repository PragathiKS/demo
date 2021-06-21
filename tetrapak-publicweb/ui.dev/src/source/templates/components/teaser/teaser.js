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
  }
  bindEvents() {
    this.cache.$teaserLink.on('click', this.trackAnalytics);
    this.root.find('.owl-carousel').each(function(){
      $(this).owlCarousel({
        stagePaddingRight: 50,
        loop: true,
        nav: true,
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
      });
    });

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
