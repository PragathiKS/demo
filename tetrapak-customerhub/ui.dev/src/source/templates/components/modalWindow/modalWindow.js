import $ from 'jquery';
import 'bootstrap';
import 'slick-carousel';

class modalWindow {
  cache = {};
  initCache() {
    /* Initialize cache here */
  }
  bindEvents() {
    /* Bind jQuery events here */
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    $('#myModal').modal();
    $('.single-item').slick({
      dots: true,
      appendDots: $('.slider-dots'),
      prevArrow: false,
      nextArrow: false,
      customPaging: function () {
        return '<button class="tp-dot"></button>'; // Remove button, customize content of "li"
      }
    });
    $('.slick-next').on('click', function () {
      $('.single-item').slick('slickNext');
    });
    $('.icon-Close').on('click', function () {
      $('#myModal').modal('toggle');
    });
  }
}

export default modalWindow;
