import $ from 'jquery';
import { isMobileMode } from '../../../scripts/common/common';

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
    // this.cache.$headingBottom = this.root.find('.heading-bottom-cta');
  }
  bindEvents() {
    /* Bind jQuery events here */
    const { $menuOpener, $menuCloser } = this.cache;
    $menuOpener.on('click',this.handleOpenEvent);
    $menuCloser.on('click',this.handleCloseEvent);
  }

  handleCloseEvent = () => {
    const { $bottomTeaser, $megamenuBottom } = this.cache;
    $megamenuBottom.css({'height': 'auto'});
    $bottomTeaser.removeClass('active').addClass('hide');
  }

  handleOpenEvent = (e) => {
    e.preventDefault();
    const { $bottomTeaser, $megamenuBottom } = this.cache;
    $bottomTeaser.addClass('active').removeClass('hide');
    $megamenuBottom.css({'height': '0px'});
  }

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
