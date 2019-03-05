import $ from 'jquery';

class LeftNavigation {
  constructor({ el }) {
    this.root = $(el);
  }
  init() {
    this.root.find('.js-close-btn').on('click', () => this.closeSideNav());
    this.root.find('.js-left-nav__overlay').on('click', () => {
      this.closeSideNav();
    });
    this.root.find('.tpmol-list-item', 'tpatom-list-item').on('click', (e) => {
      e.stopPropagation();
    });
    $(document).on('showLeftNav', () => this.openSideNav());
    this.root.find('.js-list-item__btn').on('click', function () {
      $(this).siblings('.tpmol-list-item__sublist').slideToggle(500);
    });
  }
  closeSideNav() {
    this.root.find('.tp-left-nav__container').removeClass('translated');
    this.root.find('.tp-left-nav__overlay').removeClass('translated');
    this.root.find('.tpatom-list-item__link--sticky').removeClass('translated');
  }
  openSideNav() {
    this.root.find('.tp-left-nav__container').addClass('translated');
    this.root.find('.tp-left-nav__overlay').addClass('translated');
    this.root.find('.tpatom-list-item__link--sticky').addClass('translated');
  }
}
export default LeftNavigation;
