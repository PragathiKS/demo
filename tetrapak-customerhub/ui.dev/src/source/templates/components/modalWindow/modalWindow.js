import $ from 'jquery';
import 'bootstrap';
import 'slick-carousel';
import { storageUtil } from '../../../scripts/common/common';

class modalWindow {
  cache = {};
  initCache() {
    /* Initialize cache here */
  }
  bindEvents() {
    /* Bind jQuery events here */
    $('.slick-next').on('click', function () {
      $('.single-item').slick('slickNext');
    });

    $('.icon-Close, .get-started-btn').on('click', function () {
      $('#myModal').modal('toggle');
      storageUtil.set('introScreen', true);
    });

    $('.single-item').on('beforeChange', function(event, slick, currentSlide, nextSlide){
      console.log(slick.$slides.length +','+ currentSlide + ',' + nextSlide); // eslint-disable-line
      if (slick.$slides.length === nextSlide+1) {
        $('.slick-next').hide();
        $('.get-started-btn').show();
      } else {
        $('.slick-next').show();
        $('.get-started-btn').hide(); 
      }
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
