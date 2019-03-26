import $ from 'jquery';
import 'bootstrap';

class Carousel {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    /* Initialize cache here */
    this.cache.$Carousel = $('.pw-carousel', this.root);
    this.cache.$tabsPills = $('.pw-carousel .nav-pills a', this.root);
  }
  bindEvents() {
    /* Bind jQuery events here */
    this.cache.$tabsPills.on('click', function (e) {
      e.preventDefault();
      $(this).tab('show');
    });
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default Carousel;
