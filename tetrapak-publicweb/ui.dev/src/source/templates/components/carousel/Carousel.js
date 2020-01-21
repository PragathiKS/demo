import $ from 'jquery';
import 'bootstrap';

class Carousel {
  constructor({ el }) {
    this.root = $(el);
  }
  bindEvents() {
    this.root.on('show.bs.tab', this.showTab.bind(this));
  }
  showTab(e) {
    const $currentTarget = $(e.target);
    const parentCarousel = $currentTarget.closest('.js-pw-carousel');
    if (!parentCarousel.length) {
      return;
    }
    const $grandParentIdSelector = $(`#${parentCarousel.attr('id')}`);
    const $dropPills = $grandParentIdSelector.find('.pw-carousels__mobileDropdown a.dropdown-item'),
      $mobileDrpdwn = $grandParentIdSelector.find('.pw-carousel__mobileDropdown__toggle'),
      $tabPills = $grandParentIdSelector.find('.pw-carousel__navPills__pill');
    const selectedText = $currentTarget.text();
    const self = this;
    $dropPills.each(function () {
      self.activateSection.apply(this, [selectedText]);
    });
    $tabPills.each(function () {
      self.activateSection.apply(this, [selectedText]);
    });
    $mobileDrpdwn.text(selectedText);
  }
  activateSection(selectedText) {
    const $this = $(this);
    $this[($this.text() !== selectedText) ? 'removeClass' : 'addClass']('active show');
  }
  init() {
    this.bindEvents();
  }
}

export default Carousel;
