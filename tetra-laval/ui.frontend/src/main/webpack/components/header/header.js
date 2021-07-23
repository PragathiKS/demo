import $ from 'jquery';
import 'bootstrap';
import { trackAnalytics } from 'tpPublicScripts/utils/analytics';
import { isDesktop } from 'tpPublicScripts/common/common';

class Header {
  constructor({ el }) {
    this.root = $(el);
  }

  cache = {};

  initCache() {
    this.cache.$headerLogoPlaceholder = this.root.find('.js-tp-pw-header-logo-digital-data');
    this.cache.$headerLogoTracker = this.root.find('.tp-pw-header__logo-placeholder');

    this.cache.$headerItem = this.root.find('.js-tp-pw-header-item');
    this.cache.$headerItemLink = this.root.find('.js-main-menu-link-hover');

    // show search component
    this.cache.$searchButton = this.root.find('.js-tp_pw-header__nav-options-search');
    // open page contactUs
    this.cache.$envelopeButton = this.root.find('.js-tp_pw-header__nav-options-envelope');
    
  }

  bindEvents() {
    const { 
      $headerLogoPlaceholder, 
      $headerItem, 
      $headerLogoTracker, 
      $searchButton
    } = this.cache;

    $headerLogoPlaceholder.on('click', this.trackAnalytics);
    $headerItem.on('click', this.trackNavigationAnalytics);
    $headerLogoTracker.on('click', this.trackBrandLogo);
    $('.js-tp-pw-header-item:not(.js-click-menu-link)').on('click', this.handleMainNavClick);
    this.root.find('.js-header__selected-lang-pw').on('click', (e) => {
      this.root.find('.js-lang-modal').trigger('showlanuagepreferencepopup-pw');
      this.trackLanguageSelector(e);
    });
    $searchButton.on('click', this.searchButtonClick);

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

  searchButtonClick = () => {
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

  handleMainNavClick =(e) => {
    e.preventDefault();
    const $target = $(e.target);
    const url = $target.attr('href');
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
    debugger
    this.initCache();
    this.bindEvents();
  }
}

export default Header;