import $ from 'jquery';
import 'bootstrap';
import { logger } from '../../../scripts/utils/logger';

class Accordion {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    /* Initialize selector cache here */
    /**
     * Use "this.root" to find elements within current component
     * Example:
     * this.cache.$submitBtn = this.root.find('.js-submit-btn');
     */
  }
  bindEvents() {
    /* Bind jQuery events here */
    /**
     * Example:
     * const { $submitBtn } = this.cache;
     * $submitBtn.on('click', () => { ... });
     */
  }
  init() {
    /* Mandatory method */
    logger.log('Accordion Init');
  }
}

export default Accordion;
