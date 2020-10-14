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
  }

  loadMegaMenuCol = () => {
    const { $megaMenuNavCol } = this.cache;
    if($megaMenuNavCol.length <= 3 && (window.innerWidth >= 1200 && window.innerWidth <= 1439)) {
      $('.pw-megamenu__top .pw-mega-menu-col').each(function(){
        $(this).css({'width':249,'max-width':249});
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
    const $this = $target.closest('.js-navigation-Link');
    const navigationSection = $this.data('link-section');
    const navigationLinkName = $this.data('link-name');
    const linkName = $this.data('link-title');

    let linkClickObject = null;

    const trackingObj = {
      navigationLinkName,
      navigationSection
    };

    const linkType = ($this.find('i.icon') && $this.find('i.icon').hasClass('icon-Union')) ? 'external' : 'internal';

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
    window.open($this.attr('href'), '_self');
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
      $('.pw-megamenu .pw-megamenu__top ul').css('margin', '0 auto');
      $('.pw-megamenu__top .col:not(.col-side-links)').css('flex-basis', 'auto');
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
