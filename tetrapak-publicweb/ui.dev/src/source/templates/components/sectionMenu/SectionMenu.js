import $ from 'jquery';
class SectionMenu {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$sectionMenuHeaderItem = this.root.find('.js-section-menu-navigation-Link');
  }

  bindEvents() {
    const { $sectionMenuHeaderItem } = this.cache;
    $sectionMenuHeaderItem.on('mouseover', this.handleHeaderItemMouseOver);
    $sectionMenuHeaderItem.on('mouseout', this.handleHeaderItemMouseOut);
    $('.js-section-menu-item-link').on('click', this.handleSectionMenuClick);
    $('.js-sub-menu-navigation-link-item').on('click', this.handleSubSectionMenuClick);
  }

  handleSectionMenuClick =(e) => {
    e.preventDefault();
    const $target = $(e.target);
    const $this = $target.closest('.js-section-menu-item-link');
    window.open($this.attr('href'), $this.attr('target'));
  }

  handleSubSectionMenuClick =(e) => {
    e.preventDefault();
    const $target = $(e.target);
    const $this = $target.closest('.js-sub-menu-navigation-link-item');
    window.open($this.attr('href'), $this.attr('target'));
  }

  handleHeaderItemMouseOver = (e) => {
    const $target = $(e.target);
    const $this = $target.closest('.js-section-menu-navigation-Link');
    const $sectionMenuItemAnchor = $this.children('a');
    $this.children('.js-sub-menu-navigation-Link').addClass('show').attr('aria-hidden','false').attr('aria-expanded','true');
    $sectionMenuItemAnchor.children('.with-arrow').addClass('icon-Chevron_Up').removeClass('icon-Chevron_Down');

    /* check modal view port position */
    const modalPoints =$this.children('.js-sub-menu-navigation-Link')[0] && $this.children('.js-sub-menu-navigation-Link')[0].getBoundingClientRect();
    if(modalPoints && modalPoints.left < 0){
      $this.children('.js-sub-menu-navigation-Link').addClass('show-modal-from-left');
    } else if(modalPoints && modalPoints.right > (window.innerWidth || document.documentElement.clientWidth)){
      $this.children('.js-sub-menu-navigation-Link').addClass('show-modal-from-right');
    }
  }

  handleHeaderItemMouseOut = (e) => {
    const $target = $(e.target);
    const $this = $target.closest('.js-section-menu-navigation-Link');
    const $sectionMenuItemAnchor = $this.children('a');
    $this.children('.js-sub-menu-navigation-Link').removeClass('show').attr('aria-hidden', 'true').attr('aria-expanded','false');
    $sectionMenuItemAnchor.children('.with-arrow').addClass('icon-Chevron_Down').removeClass('icon-Chevron_Up');
  }

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default SectionMenu;
