import $ from 'jquery';

class LeftNavigation {
  constructor({ el }) {
    this.root = $(el);
  }
  init() {
    this.root.find('.js-close-btn').on('click', () => this.closeSideNav());
    this.root.find('.tp-left-nav-container').on('click', (e) => {
      if (e.target.className === 'tp-left-nav-container') {
        this.closeSideNav();
      }
    });
  }
  closeSideNav() {
    this.root.find('.tp-left-nav-container').addClass('slideIn');
    this.root.find('.tp-sticky').css('display', 'none');

  }
}
export default LeftNavigation;
