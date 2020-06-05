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
    this.cache.$navigationElement= this.root;
  }
  bindEvents() {
    const { $stickySectionMenu, $sectionMenuToggle } = this.cache;
    if ($stickySectionMenu.length > 0) {
      $('.body-content').addClass('body-top-padding');
    }

    /* the below function uses the bootstrap for the toggle the close and open icon */
    $('.js-pw-navigation__container').on('shown.bs.collapse', function(){
      $(this).parent().children('a').children('.without-arrow').removeClass('icon-Arrow_Right_pw').addClass('icon-Close_pw');
    }).on('hidden.bs.collapse', function(){
      $(this).parent().children('a.collapsed').children('.without-arrow').removeClass('icon-Close_pw').addClass('icon-Arrow_Right_pw');
    });

    $sectionMenuToggle.on('click', this.openMobileSectionMenu);
    this.showSelectedHeader();
  }

  openMobileSectionMenu = (e) => {
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
