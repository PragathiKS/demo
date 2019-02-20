import $ from 'jquery';
import 'bootstrap';
import 'slick-carousel';
import { storageUtil, getI18n } from '../../../scripts/common/common';

class modalWindow {
  cache = {};
  initCache() {
    /* Initialize cache here */
  }
  bindEvents() {
    /* Bind jQuery events here */
    $('.js-slick-next').on('click', function () {
      if ($(this).hasClass('js-get-started-btn')) {
        $('#myModal').modal('toggle');
        storageUtil.set('introScreen', true);
      }

      $('.single-item').slick('slickNext');
    });

    $('.single-item').on('beforeChange', function(event, slick, currentSlide, nextSlide){
      if (slick.$slides.length === nextSlide+1) {
        $('.js-slick-next').addClass('js-get-started-btn');
        $('.js-slick-next .tp-next-btn__text').text(getI18n($('#getStartedBtnI18n').val()));
      } else {
        $('.js-slick-next').removeClass('js-get-started-btn');
        $('.js-slick-next .tp-next-btn__text').text(getI18n($('#nextBtnI18n').val()));
      }
    });

    $('.js-close-btn').on('click', function () {
      $('#myModal').modal('toggle');
      storageUtil.set('introScreen', true);
    });
  }
  init() {
    let introScreen = storageUtil.get('introScreen');

    /* Mandatory method */
    this.initCache();

    if (!introScreen) {
      this.bindEvents();

      $('#myModal').modal();

      $('.single-item').slick({
        dots: true,
        infinite: false,
        appendDots: $('.slider-dots'),
        prevArrow: false,
        nextArrow: false,
        customPaging: function () {
          return '<button class="tp-dot"></button>'; // Remove button, customize content of "li"
        }
      });
    }
  }
}

export default modalWindow;
