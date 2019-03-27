import $ from 'jquery';
import 'bootstrap';

class Carousel {
  setVersions() {
    $('.pw-carousel').each(function(index){
      $(this).addClass('pw-carousel-version'+index);
    });
  }
  bindEvents() {
    /* Bind jQuery events here */
    $('body').bind('show.bs.tab', function(e){
      let parentCarousel = e.target.closest('.pw-carousel');
      if (!parentCarousel) {
        return;
      }
      let grandParentClassNamesArr = $(parentCarousel).attr('class').match(/\S+/gi);
      let specificGrandParentClassVersion = grandParentClassNamesArr[grandParentClassNamesArr.length - 1];
      const grandParentClass = '.'+specificGrandParentClassVersion;

      const $dropPills = $('.pw-carousel__mobileDropdown a.dropdown-item', grandParentClass),
        $mobileDrpdwn = $('.pw-carousel__mobileDropdown__toggle', grandParentClass),
        $tabPills = $('.pw-carousel__navPills__pill', grandParentClass);

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
    this.setVersions();
    this.bindEvents();
  }
}

export default Carousel;
