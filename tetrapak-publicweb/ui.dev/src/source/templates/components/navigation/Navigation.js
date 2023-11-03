import $ from 'jquery';
import 'bootstrap';
import { isDesktop } from '../../../scripts/common/common';

class Navigation {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$stickySectionMenu = this.root.closest('.sticky-section-menu');
    this.cache.$sectionMenuToggle = this.root.find('.collapse-button');
    this.cache.$navigationElement= this.root;
  }
  bindEvents() {
    const { $stickySectionMenu, $sectionMenuToggle } = this.cache;
    if ($stickySectionMenu.length > 0) {
      $('.body-content').addClass('body-top-padding');
    }
    /* the below function uses the bootstrap for the toggle the close and open icon */
    $('.js-pw-navigation__container').on('show.bs.collapse', function(event){
      if(event.target === event.currentTarget){
        $(this).parent().children('a').children('.without-arrow').removeClass('icon-Navigation_Right_pw').addClass('icon-Close_pw');
        $(this).find('.js-section-menu-navigation-Link').each(function(){
          $(this).children('.collapse.show:not(.active-accordion)').removeClass('show');
          $(this).children('.section-menu-item-link:not(.active)').addClass('collapsed').attr('aria-expanded','false');

          /* show the active L2 */
          $(this).children('.section-menu-item-link.active').removeClass('collapsed').attr('aria-expanded','true');
          $(this).children('.collapse.active-accordion').addClass('show');
        });
      }
    }).on('hidden.bs.collapse', function(event){
      if(event.target === event.currentTarget){
        $(this).parent().children('a.collapse-button').children('.without-arrow').removeClass('icon-Close_pw').addClass('icon-Navigation_Right_pw');
      }
    }).on('shown.bs.collapse', function(){
      $('.js-pw-navigation__container').each(function() {
        const headerHeightMobile = 62; // mobile sticky header height
        const sectionMenuHeightMobile = 56; // mobile sticky section hight
        if(!isDesktop()){
          $( this ).css('height',window.innerHeight - (headerHeightMobile + sectionMenuHeightMobile));
        }
      });
    });

    $sectionMenuToggle.on('click', this.sectionMenuToggleClick);
    this.showSelectedHeader();
    this.checkForSectionMenuOverlap();
  }

  checkForSectionMenuOverlap = () => {
    const $sectionLinkAnchorLabel = this.cache.$stickySectionMenu.find('.section-link-home');
    const sectionListSize = this.cache.$stickySectionMenu.find('.list-section-menu-links > li').length;
    if($sectionLinkAnchorLabel.length > 0 && sectionListSize >= 5) {
      this.cache.$stickySectionMenu.find('.list-section-menu-links').addClass('align-right');
    }
  }

  sectionMenuToggleClick = (e) => {
    e.preventDefault();
    const $target = $(e.target);
    const $this = $target.closest('.collapse-button');
    if($this.hasClass('collapsed')){
      $('body').css('overflow','hidden');
    } else {
      $('body').css('overflow','auto');
    }
  }

  showSelectedHeader = () => {
    const { $navigationElement } = this.cache;
    $('.js-tp-pw-header-item-desktop').each(function() {
      if ($(this).data('link-name') === $navigationElement.data('selected-header')) {
        $(this).addClass('active');
      }
    });
  };
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default Navigation;