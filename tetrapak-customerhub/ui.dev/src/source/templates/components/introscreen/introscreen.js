import $ from 'jquery';
import 'bootstrap';
import 'slick-carousel';
import { logger } from '../../../scripts/utils/logger';
import { storageUtil, getI18n } from '../../../scripts/common/common';
import { trackAnalytics } from '../../../scripts/utils/analytics';


/**
 * Fire analytics on Packaging, Processing
 * mail/contact link click
 */
function _trackAnalytics(title, name) {
  const analyticsData = {
    linkType: 'internal',
    linkSection: 'intro modal'
  };

  // creating linkParentTitle and linkName as per the title received
  analyticsData.linkParentTitle = title;
  analyticsData.linkName = name;

  trackAnalytics(analyticsData, 'linkClick', 'linkClicked', undefined, false);
}

class introscreen {
  constructor({ templates, el }) {
    this.templates = templates;
    this.root = $(el);
  }
  cache = {};
  initCache() {
    /* Initialize cache here */
    this.cache.$introScreenCarousel = this.root.find('.js-intro-slider');
    this.cache.$carouselNextBtn = this.root.find('.js-slick-next');
    this.cache.$carouselNextBtnTxt = this.root.find('.js-slick-next .tp-next-btn__text');
  }

  bindEvents() {
    /* Bind jQuery events here */
    this.cache.$carouselNextBtn.on('click', () => {
      if (this.cache.$carouselNextBtn.hasClass('js-get-started-btn')) {
        this.closeCarousel();
      }

      this.cache.$introScreenCarousel.slick('slickNext');
    });

    this.cache.$introScreenCarousel.on('beforeChange', (event, slick, currentSlide, nextSlide) => {
      if (slick.$slides.length === nextSlide + 1) {
        this.cache.$carouselNextBtn.addClass('js-get-started-btn');
        this.cache.$carouselNextBtnTxt.text(getI18n(this.root.find('#getStartedBtnI18n').val()));
      } else {
        this.cache.$carouselNextBtn.removeClass('js-get-started-btn');
        this.cache.$carouselNextBtnTxt.text(getI18n(this.root.find('#nextBtnI18n').val()));
      }
    });

    this.root.find('.js-close-btn')
      .on('click', () => {
        const sliderTitle = this.root.find('.slick-active .intro-slider__title').text();

        logger.log('on close icon click', sliderTitle);
        this.trackAnalytics(sliderTitle, 'close');
        this.closeCarousel();
      });
  }

  init() {
    let introScreen = storageUtil.get('introScreen');

    /* Mandatory method */
    if (!introScreen) {
      this.initCache();
      this.bindEvents();

      this.root.modal();

      this.cache.$introScreenCarousel.slick({
        dots: true,
        infinite: false,
        appendDots: this.root.find('.slider-dots'),
        prevArrow: false,
        nextArrow: false,
        customPaging: () => this.templates.cuhuDot() // Remove button, customize content of "li"
      });
    }
  }

  closeCarousel() {
    this.root.modal('hide');
    storageUtil.set('introScreen', true);
  }

  trackAnalytics = (title, name) => _trackAnalytics.call(this, title, name);
}

export default introscreen;
