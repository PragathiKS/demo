import $ from 'jquery';
import 'bootstrap';
import 'slick-carousel';
import { storageUtil, getI18n } from '../../../scripts/common/common';

class introscreen {
  constructor({ templates }) {
    this.templates = templates;
  }
  cache = {};
  initCache() {
    /* Initialize cache here */
    this.cache.$introScreenModal = $('.js-intro-modal');
    this.cache.$introScreenCarousel = $('.js-intro-slider');
    this.cache.$carouselNextBtn = $('.js-slick-next');
    this.cache.$carouselNextBtnTxt = $('.js-slick-next .tp-next-btn__text');
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
        this.cache.$carouselNextBtnTxt.text(getI18n($('#getStartedBtnI18n').val()));
      } else {
        this.cache.$carouselNextBtn.removeClass('js-get-started-btn');
        this.cache.$carouselNextBtnTxt.text(getI18n($('#nextBtnI18n').val()));
      }
    });

    $('.js-close-btn').on('click', () => {
      this.closeCarousel();
    });
  }

  init() {
    let introScreen = storageUtil.get('introScreen');

    /* Mandatory method */
    if (!introScreen) {
      this.initCache();
      this.bindEvents();

      this.cache.$introScreenModal.modal();

      this.cache.$introScreenCarousel.slick({
        dots: true,
        infinite: false,
        appendDots: $('.slider-dots'),
        prevArrow: false,
        nextArrow: false,
        customPaging: () => this.templates.cuhuDot() // Remove button, customize content of "li"
      });
    }
  }

  closeCarousel() {
    this.cache.$introScreenModal.modal('hide');
    storageUtil.set('introScreen', true);
  }
}

export default introscreen;
