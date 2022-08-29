import $ from 'jquery';
import 'bootstrap';
import { trackAnalytics } from '../../../scripts/utils/analytics';
import { checkActiveOverlay, isDesktop } from '../../../scripts/common/common';


class Header {
  constructor({ el }) {
    this.root = $(el);
    this.toggleFlag = false;
    this.toggleButtonId = '#toggle-button';
  }

  cache = {};

  initCache() {
    this.cache.$mobileMenu = this.root.find('.js-tp-pw-mobile-navigation');
    this.cache.$hamburgerToggle = this.root.find('.js-tp-pw-header__hamburger');
    this.cache.$headerLogoPlaceholder = this.root.find('.js-tp-pw-header-logo-digital-data');
    this.cache.$headerLogoTracker = this.root.find('.tp-pw-header__logo-placeholder');
    this.cache.$hoverMenuLink = this.root.find('.js-hover-menu-link');
    this.cache.$clickMenuLink = this.root.find('.js-click-menu-link');
    this.cache.$headerMobile = this.root.find('.tp-pw-header__container');
    this.cache.$headerItems = this.root.find('.tp-pw-header__container .header-items');
    this.cache.$megaMenuDesktop = this.root.find('.tp-pw-header__container .pw-megamenu');
    this.cache.$megaMenuMobile = this.root.find('.pw-megamenu');
    this.cache.$parentNavElement = this.root.find('.tp-pw-header__main-navigation.col-6');
    this.cache.$menuCloseSol = this.root.find('.js-close-menu-solution');
    this.cache.$bottomTeaserH = this.root.find('.js-bottom-teaser-list');
    this.cache.$headerItem = this.root.find('.js-tp-pw-header-item');
    this.cache.$headerItemLink = this.root.find('.js-main-menu-link-hover');
    this.cache.$overlay = $('.js-pw-overlay');
    this.cache.$searchIcon = this.root.find('.js-tp-pw-header__search-box-toggle');
  }

  //

  bindEvents() {
    const { $hamburgerToggle, $headerLogoPlaceholder, $headerItem, $headerLogoTracker,$searchIcon, $headerItems, $megaMenuDesktop} = this.cache;
    $hamburgerToggle.on('click', this.openMobileMenuBoxToggle);
    $headerLogoPlaceholder.on('click', this.trackAnalytics);
    $(window).on('resize', this.hideMobileMenuOnResize);
    this.cache.$hoverMenuLink.on('mouseover', this.handleMouseOver);
    this.cache.$hoverMenuLink.on('mouseout', this.handleMouseOut);
    this.cache.$headerItemLink.on('mouseover', this.handleHeaderItemMouseOver);
    this.cache.$headerItemLink.on('mouseout', this.handleHeaderItemMouseOut);
    this.cache.$clickMenuLink.on('click', this.handleMenuClick);
    this.cache.$menuCloseSol.on('click', this.handleCloseSolEvent);
    $headerItem.on('click', this.trackNavigationAnalytics);
    $headerLogoTracker.on('click', this.trackBrandLogo);
    $('.js-tp-pw-header-item:not(.js-click-menu-link)').on('click', this.handleMainNavClick);
    this.root.find('.js-header__selected-lang-pw').on('click', (e) => {
      this.root.find('.js-lang-modal').trigger('showlanuagepreferencepopup-pw');
      this.trackLanguageSelector(e);
    });
    $searchIcon.on('click', this.searchIconClick);

    if(isDesktop) {
      const headerWidth = $headerItems.outerWidth();
      $megaMenuDesktop.css('width', headerWidth - 96);
    }

    // bind event to close search if clicked outside the searchbar
    if(isDesktop()){
      $(document).mouseup((e) =>
      {
        var container = $('.js-pw-header-search-bar');

        // if the target of the click isn't the container nor a descendant of the container
        if (!container.is(e.target) && container.has(e.target).length === 0)
        {
          this.hideSearchbar();
        }
      });
    }
  }

  checkForSectionMenuOverlap = ($this) => {
    const $sectionLinkAnchorLabel = $this.find('.section-link-home');
    const sectionListSize = $this.find('.list-section-menu-links > li').length;
    if($sectionLinkAnchorLabel.length > 0 && sectionListSize >= 5) {
      $this.find('.list-section-menu-links').addClass('align-right');
    }
  }

  hideSearchbar = () => {
    $('.js-pw-header-search-bar').removeClass('show');
  }

  searchIconClick = () => {
    $('.js-search-bar-input').val('');
    if(isDesktop()){
      // to hide navigation if opened
      $('.js-tp-pw-mobile-navigation').css('display','none');
      this.cache.$hamburgerToggle.children(this.toggleButtonId).addClass('icon-Burger_pw');
      this.cache.$hamburgerToggle.children(this.toggleButtonId).removeClass('icon-Close_pw');
      this.toggleFlag = false;

      if(this.cache.$searchIcon.children('i').hasClass('icon-Search_pw')){
        this.cache.$searchIcon.children('i').removeClass('icon-Search_pw');
        this.cache.$searchIcon.children('i').addClass('icon-Close_pw');
        $('.js-pw-header-search-bar').addClass('show');
        $('body').css('overflow','hidden');
      } else {
        this.cache.$searchIcon.children('i').removeClass('icon-Close_pw');
        this.cache.$searchIcon.children('i').addClass('icon-Search_pw');
        $('.js-pw-header-search-bar').removeClass('show');
        const activeOverlay = ['.js-tp-pw-mobile-navigation','.js-pw-navigation__container'];
        checkActiveOverlay(activeOverlay);
      }
    } else {
      $('.js-pw-header-search-bar').addClass('show');
    }
    $('.search-bar-input').focus();

    const dataObj = {
      linkType: 'internal',
      linkSection: 'Hyperlink click',
      linkParentTitle: '',
      linkName: 'Search'
    };
    const eventObj = {
      eventType: 'linkClick',
      event: 'Search'
    };
    trackAnalytics(dataObj, 'linkClick', 'linkClick', undefined, false, eventObj);

  }

  trackLanguageSelector = () => {
    const trackingObj = {
      linkType: 'internal',
      linkSection: 'Hyperlink click',
      linkParentTitle: '',
      linkName: 'Market Selector'
    };
    const eventObj = {
      eventType: 'linkClick',
      event: 'Market Selector'
    };
    trackAnalytics(trackingObj, 'linkClick', 'linkClick', undefined, false, eventObj);
  }

  handleMouseOver = () => {
    const { $megaMenuDesktop, $overlay } = this.cache;
    $megaMenuDesktop.addClass('d-block').attr('aria-hidden','false').attr('aria-expanded','true');
    $overlay.removeClass('d-none');
  }

  handleHeaderItemMouseOver = (e) => {
    const $target = $(e.target);
    const $this = $target.closest('.js-main-menu-link-hover');
    if($this.children('.active').length > 0){
      return false;
    }
    $this.children('.pw-navigation').addClass('show').attr('aria-hidden','false').attr('aria-expanded','true');
    this.checkForSectionMenuOverlap($this);
  }

  handleHeaderItemMouseOut = (e) => {
    const $target = $(e.target);
    const $this = $target.closest('.js-main-menu-link-hover');
    $this.children('.pw-navigation').removeClass('show').attr('aria-hidden', 'true').attr('aria-expanded','false');
  }

  handleMouseOut = () => {
    const { $megaMenuDesktop, $overlay } = this.cache;
    $megaMenuDesktop.removeClass('d-block').attr('aria-hidden', 'true').attr('aria-expanded','false');
    $overlay.addClass('d-none');
  }

  handleMenuClick = (e) => {
    e.preventDefault();
    const { $megaMenuMobile } = this.cache;
    $megaMenuMobile.removeClass('is-close');
    $megaMenuMobile.addClass('is-open');
    // this.cache.$headerMobile.addClass('d-none');
  }

  handleCloseSolEvent = () => {
    const { $megaMenuMobile } = this.cache;
    $megaMenuMobile.removeClass('is-open');
    $megaMenuMobile.addClass('is-close');
    // this.cache.$headerMobile.removeClass('d-none');
  }

  hideMobileMenuOnResize = () => {
    this.cache.$mobileMenu.fadeOut(10);
    this.cache.$hamburgerToggle.children(this.toggleButtonId).removeClass('icon-Close_pw');
    this.cache.$hamburgerToggle.children(this.toggleButtonId).addClass('icon-Burger_pw');
    this.toggleFlag = false;
    const headerWidth = this.cache.$headerItems.outerWidth();
    this.cache.$megaMenuDesktop.css('width', headerWidth - 96);
  }

  openMobileMenuBoxToggle = () => {
    if(!this.toggleFlag){
      this.cache.$mobileMenu.fadeIn(300);
      this.cache.$hamburgerToggle.children(this.toggleButtonId).removeClass('icon-Burger_pw');
      this.cache.$hamburgerToggle.children(this.toggleButtonId).addClass('icon-Close_pw');

      // to reset the search icon
      this.cache.$searchIcon.children('i').addClass('icon-Search_pw');
      this.cache.$searchIcon.children('i').removeClass('icon-Close_pw');
      this.toggleFlag = true;
      $('body').css('overflow','hidden');
    }else {
      this.cache.$mobileMenu.fadeOut(300);
      this.cache.$hamburgerToggle.children(this.toggleButtonId).removeClass('icon-Close_pw');
      this.cache.$hamburgerToggle.children(this.toggleButtonId).addClass('icon-Burger_pw');
      this.toggleFlag = false;

      // check if searchbar is active
      if($('.js-pw-header-search-bar').hasClass('show')){
        // to reset the search icon
        this.cache.$searchIcon.children('i').removeClass('icon-Search_pw');
        this.cache.$searchIcon.children('i').addClass('icon-Close_pw');
      }


      //hide other navigation on close
      const { $megaMenuMobile, $bottomTeaserH } = this.cache;
      $bottomTeaserH.removeClass('active').addClass('hide');
      $megaMenuMobile.removeClass('is-open');
      $megaMenuMobile.addClass('is-close');

      // check if other overlay is active
      const activeOverlay = ['.js-pw-header-search-bar','.js-pw-navigation__container'];
      checkActiveOverlay(activeOverlay);
    }
  }

  handleMainNavClick =(e) => {
    e.preventDefault();
    const $target = $(e.target);
    let url = $target.attr('href');
    if (!isDesktop()) {
      url = $target.parent().attr('href');
    }
    const $this = $target.closest('.js-tp-pw-header-item');
    if (url) {
      if (e.metaKey || e.ctrlKey || e.keyCode === 91 || e.keyCode === 224){
        window.open($this.attr('href'),'_blank');
      }
      else {
        window.open($this.attr('href'),'_self'); 
      }
    }
  }

  trackBrandLogo = (e) => {
    e.preventDefault();
    const $target = $(e.target);
    const $this = $target.closest('.js-tp-pw-header-logo-digital-data');
    const url = $this.attr('href');
    const myDomain = 'tetrapak.com';
    const myDomainAdobe = 'adobecqms.net';

    if (url && (url.includes('http://') || url.includes('https://')) && !url.includes(myDomain) && !url.includes(myDomainAdobe)) {
      $this.attr('target','_blank');
    }else {
      $this.attr('target','_self');
    }
    const linkType = $this.attr('target') === '_blank'? 'external' :'internal';
    const trackingObj = {
      linkType,
      linkSection: 'Brand logo',
      linkParentTitle: '',
      linkName: 'TetraPak'
    };
    const eventObj = {
      eventType: 'linkClick',
      event: 'Header'
    };
    trackAnalytics(trackingObj, 'linkClick', 'linkClick', undefined, false, eventObj);

    if(url && linkType){
      if (e.metaKey || e.ctrlKey || e.keyCode === 91 || e.keyCode === 224){ 
        window.open(url,'_blank');
      }
      else {
        window.open(url,'_self');
      }
    }
  }

  trackNavigationAnalytics = (e) => {
    const $target = $(e.target);
    const $this = $target.closest('.js-tp-pw-header-item');
    const navigationLinkName = $this.text().trim();
    const linkType = $this.attr('target') === '_blank'? 'external' :'internal';

    const trackingObj = {
      navigationLinkName,
      navigationSection: 'Header Navigation'
    };

    const linkClickTrackingobj = {
      linkType,
      linkSection: 'Hyperlink Click',
      linkParentTitle:'',
      linkName: navigationLinkName
    };

    const eventObj = {
      eventType: 'navigation click',
      event: 'Header'
    };
    trackAnalytics(
      trackingObj,
      'navigation',
      'navigationClick',
      undefined,
      false,
      eventObj,
      linkClickTrackingobj
    );
  }

  trackAnalytics = (e) => {
    e.preventDefault();
    const $target = $(e.target);
    const $this = $target.closest('.js-tp-pw-header-logo-digital-data');
    const targetLink = $this.attr('target');
    const url = $this.attr('href');
    const myDomain = 'tetrapak.com';
    const myDomainAdobe = 'adobecqms.net';
    if (url && (url.includes('http://') || url.includes('https://')) && !url.includes(myDomain) && !url.includes(myDomainAdobe)) {
      $this.attr('target','_blank');
    } else if(!$this.hasClass('js-click-menu-link')) {
      $this.attr('target','_self');
    }
    const linkType = $this.attr('target') === '_blank'? 'external' :'internal';
    const linkName = $this.data('link-name');
    if(linkName==='contact us envelope') {
      const trackingObj = {
        linkType,
        linkSection: 'Hyperlink click',
        linkParentTitle: '',
        linkName: 'Contact Us'
      };
      const eventObj = {
        eventType: 'linkClick',
        event: 'Header'
      };
      trackAnalytics(trackingObj, 'linkClick', 'linkClick', undefined, false, eventObj);
    }
    else {
      const trackingObj = {
        linkType,
        linkSection: 'Hyperlink click',
        linkParentTitle: '',
        linkName: 'My TetraPak'
      };
      const eventObj = {
        eventType: 'linkClick',
        event: 'Header'
      };
      trackAnalytics(trackingObj, 'linkClick', 'linkClick', undefined, false, eventObj);
    }

    if(url && targetLink){
      if (e.metaKey || e.ctrlKey || e.keyCode === 91 || e.keyCode === 224){ 
        window.open(url,'_blank');
      }
      else {
        window.open(url,'_self');
      }
    }
  }

  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default Header;