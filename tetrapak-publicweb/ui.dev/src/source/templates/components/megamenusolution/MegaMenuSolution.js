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
    // this.cache.$headingBottom = this.root.find('.heading-bottom-cta');
  }
  bindEvents() {
    /* Bind jQuery events here */
    const { $menuOpener, $menuCloser, $navigationLink } = this.cache;
    $menuOpener.on('click', this.handleOpenEvent);
    $menuCloser.on('click', this.handleCloseEvent);
    $navigationLink.on('click', this.trackAnalytics);
  }

  handleCloseEvent = () => {
    const { $bottomTeaser, $megamenuBottom } = this.cache;
    $megamenuBottom.css({ height: 'auto' });
    $bottomTeaser.removeClass('active').addClass('hide');
  };

  trackAnalytics = e => {
    const $target = $(e.target);
    const $this = $target.closest('.js-navigation-Link');
    const linkSection = $this.data('link-section');
    const linkName = $this.data('link-name');

    const trackingObj = {
      linkName,
      linkSection
    };

    if ($this.find('i.icon').length) {
      trackingObj['linkType'] = $this.find('i.icon').hasClass('icon-Union')
        ? 'external'
        : 'internal';
    }

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
      eventObj
    );
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
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default MegaMenuSolution;
