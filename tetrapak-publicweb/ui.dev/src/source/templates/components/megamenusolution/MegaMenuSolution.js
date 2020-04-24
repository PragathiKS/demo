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
  }
  bindEvents() {
    /* Bind jQuery events here */
    const { $menuOpener, $menuCloser } = this.cache;
    this.isMobileMode() && $menuOpener.on('click',this.handleOpenEvent);
    this.isMobileMode() && $menuCloser.on('click',this.handleCloseEvent);
  }

  handleOpenEvent = () => {
    const { $bottomTeaser } = this.cache;
    $bottomTeaser.addClass('active').removeClass('hide');
  }

  handleCloseEvent = () => {
    const { $bottomTeaser } = this.cache;
    $bottomTeaser.removeClass('active').addClass('hide');
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
