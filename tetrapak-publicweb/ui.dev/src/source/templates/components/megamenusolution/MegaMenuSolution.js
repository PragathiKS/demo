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
    $menuOpener.on('click',this.handleOpenEvent);
    $menuCloser.on('click',this.handleCloseEvent);
  }

  handleCloseEvent = () => {
    const { $bottomTeaser } = this.cache;
    $bottomTeaser.removeClass('active').addClass('hide');
  }

  handleOpenEvent = (e) => {
    e.preventDefault();
    const { $bottomTeaser } = this.cache;
    $bottomTeaser.addClass('active').removeClass('hide');
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
