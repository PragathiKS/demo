import $ from 'jquery';
import { isMobileMode } from '../../../scripts/common/common';
import { trackAnalytics } from '../../../scripts/utils/analytics';

class MegaMenuSolution {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};

  initCache() {
    /* Initialize selector cache here */
    this.cache.$menuOpener = this.root.find('.js-open-menu');
    this.cache.$menuCloser = this.root.find('.js-close-menu');
    this.cache.$bottomTeaser = this.root.find('.js-bottom-teaser-list');
    this.cache.$megamenuBottom = this.root.find('.pw-megamenu__bottom');
    this.cache.$navigationLink = this.root.find('.js-navigation-Link');
    this.cache.$megaMenuNavCol = this.root.find('.pw-mega-menu-col');
    // this.cache.$headingBottom = this.root.find('.heading-bottom-cta');
  }
  bindEvents() {
    /* Bind jQuery events here */
    const { $menuOpener, $menuCloser, $navigationLink } = this.cache;
    $menuOpener.on('click', this.handleOpenEvent);
    $menuCloser.on('click', this.handleCloseEvent);
    $navigationLink.on('click', this.trackAnalytics);
    this.loadMegaMenuCol();
    $( window ).resize(() => {
      this.loadMegaMenuCol();
    });
  }

  loadMegaMenuCol = () => {
    const { $megaMenuNavCol } = this.cache;
    if(window.innerWidth >= 1200 && window.innerWidth <= 1439) {
      $megaMenuNavCol.each(function(){
        $(this).find('img').addClass('load-mega-menu-dynamic-media');
      });
    }
  }

  handleCloseEvent = () => {
    const { $bottomTeaser, $megamenuBottom } = this.cache;
    $megamenuBottom.css({ height: 'auto' });
    $bottomTeaser.removeClass('active').addClass('hide');
  };

  trackAnalytics = e => {
    e.preventDefault();
    const $target = $(e.target);
    const url = $target.parent().attr('href');
    const $this = $target.closest('.js-navigation-Link');
    const navigationSection = $this.data('link-section');
    const navigationLinkName = $this.data('link-name');
    const linkName = $this.data('link-title');

    let linkClickObject = null;

    const trackingObj = {
      navigationLinkName,
      navigationSection
    };

    const linkType = ($this.find('i.icon') && $this.find('i.icon').is('.icon-Union, .icon-Download')) ? 'external' : 'internal';

    linkClickObject = {
      linkType,
      linkSection: 'hyperlink click',
      linkParentTitle: '',
      linkName
    };

    const eventObj = {
      eventType: 'navigation click',
      event: 'Navigation'
    };
    trackAnalytics(
      trackingObj,
      'navigation',
      'navigationClick',
      undefined,
      false,
      eventObj,
      linkClickObject
    );
    if(url){
      if (e.metaKey || e.ctrlKey || e.keyCode === 91 || e.keyCode === 224){
        window.open($this.attr('href'), '_blank');
      }
      else {
        window.open($this.attr('href'), '_self');
      }
    }
  };

  handleOpenEvent = e => {
    e.preventDefault();
    const { $bottomTeaser, $megamenuBottom } = this.cache;
    $bottomTeaser.addClass('active').removeClass('hide');
    $megamenuBottom.css({ height: '0px' });
  };

  isMobileMode() {
    return isMobileMode(...arguments);
  }

  iEFix() {
    var ua = window.navigator.userAgent;
    var trident = ua.indexOf('Trident/');
    if (trident > 0) { // detect ie 11
      $('.pw-megamenu').css('height', 'auto');
      $('.col-side-links.col').css({'min-width':'240px'});
      $('.pw-megamenu__bottom ul').css('margin', '0 auto');
      $('.pw-megamenu__bottom ul .col').css('flex-basis', 'auto');
      $('.pw-megamenu__bottom .bottom-teaser-list').css({'display':'block','width':'80%','margin':'0 auto'});
    }
  }

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.iEFix();
  }
}

export default MegaMenuSolution;
