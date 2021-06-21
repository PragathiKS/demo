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
    this.cache.$teaserImage = this.root.find('.js-teaser-analytics img');
  }
  bindEvents() {
    this.cache.$teaserLink.on('click', this.trackAnalytics);
    const that = this;
    this.root.find('.owl-carousel').each(function(){
      $(this).owlCarousel({
        stagePaddingRight: 62,
        loop: true,
        nav: true,
        onInitialized: that.setNavArrow,
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

  setNavArrow = () => {
    this.cache.$owlPrev = this.root.find('.owl-prev');
    this.cache.$owlNext = this.root.find('.owl-next');
    this.adjustNavArrow();
    $(window).on('resize orientationchange', () => {
      this.adjustNavArrow();
    });
  }

  adjustNavArrow = () => {
    const { $owlPrev, $owlNext,$teaserImage } = this.cache;
    const imageHeight = $teaserImage[0].height;
    $owlPrev.css('top', imageHeight/2 - 14);
    $owlNext.css('top', imageHeight/2 - 14);
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
