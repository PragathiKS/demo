import $ from 'jquery';
import 'bootstrap';

class CarouselWithFilters {
  cache = {};
  initCache() {
    /* Initialize cache here */
  }
  bindEvents() {
    /* Bind jQuery events here */
    $('body').bind('show.bs.tab', function(e){
      let parentCarousel = e.target.closest('.pw-carousel');
      if (!parentCarousel) {
        return;
      }
      let grandParentId = $(parentCarousel).attr('id');
      const grandParentIdSelector = '#'+grandParentId;

      const $dropPills = $('.pw-carousel__mobileDropdown a.dropdown-item', grandParentIdSelector),
        $mobileDrpdwn = $('.pw-carousel__mobileDropdown__toggle', grandParentIdSelector),
        $tabPills = $('.pw-carousel__navPills__pill', grandParentIdSelector);

      let selectedText = e.target.innerText;
      $dropPills.each(function() {
        if ($(this).text() !== selectedText) {
          $(this).removeClass('active show');
        } else {
          $(this).addClass('active show');
        }
      });
      $tabPills.each(function() {
        if ($(this).text() !== selectedText) {
          $(this).removeClass('active show');
        } else {
          $(this).addClass('active show');
        }
      });
      $mobileDrpdwn.text(selectedText);
    });
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default CarouselWithFilters;
