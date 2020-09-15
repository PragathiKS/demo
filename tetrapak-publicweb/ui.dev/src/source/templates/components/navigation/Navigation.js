import $ from 'jquery';
import 'bootstrap';

class Navigation {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$stickySectionMenu = this.root.closest('.sticky-section-menu');
    this.cache.$sectionMenuToggle = this.root.find('.collapse-button');
    this.cache.$stickySectionMenu = $('.sticky-section-menu');
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
    });

    $sectionMenuToggle.on('click', this.sectionMenuToggleClick);
    this.showSelectedHeader();
    this.checkForSectionMenuOverlap();
  }

  checkForSectionMenuOverlap = () => {
    const $sectionLinkAnchor = this.cache.$stickySectionMenu.find('.section-link-home');
    const $sectionLinkAnchorLabel = this.cache.$stickySectionMenu.find('.section-link-home a')[0];
    const sectionLinkAnchorRect = $sectionLinkAnchor.length > 0 && $sectionLinkAnchor[0].getBoundingClientRect();
    const sectionFirstLinkReact = this.cache.$stickySectionMenu.find('.list-section-menu-links li').first()[0].getBoundingClientRect();
    const overlap = !(sectionLinkAnchorRect.right < sectionFirstLinkReact.left ||
      sectionLinkAnchorRect.left > sectionFirstLinkReact.right);
    if(overlap) {
      this.cache.$stickySectionMenu.find('.list-section-menu-links').css('padding-left', $sectionLinkAnchorLabel.clientWidth - 15);
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
