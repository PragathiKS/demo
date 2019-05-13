import $ from 'jquery';
import 'bootstrap';
import 'slick-carousel';
import { storageUtil, getI18n } from '../../../scripts/common/common';
import { trackAnalytics } from '../../../scripts/utils/analytics';


/**
 * Fire analytics on close, next and slider
 * button click
 */
function _trackAnalytics(title, name) {
  const analyticsData = {
    linkType: 'internal',
    linkSection: 'intro modal'
  };

  // creating linkParentTitle/linkName as per the title/name received
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
      this.sliderTitle = this.root.find('.slick-active .js-intro-slider__title').text();
      this.sliderIndex = this.root.find('.slick-active').data('slickIndex') + 1;
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
        this.trackAnalytics(this.sliderTitle, 'close');
        this.closeCarousel();
      });

    this.root.find('.js-slick-next')
      .on('click', () => {
        const nextButton = this.root.find('.js-slick-next .tp-next-btn__text').text();
        this.trackAnalytics(this.sliderTitle, nextButton + this.sliderIndex);
      });

    this.root.find('.js-slider-dots')
      .on('click', () => {
        this.sliderTitle = this.root.find('.slick-active .js-intro-slider__title').text();
        this.sliderIndex = this.root.find('.slick-active').data('slickIndex') + 1;
        this.trackAnalytics(this.sliderTitle, 'slider' + this.sliderIndex);
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
        speed: 500,
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
