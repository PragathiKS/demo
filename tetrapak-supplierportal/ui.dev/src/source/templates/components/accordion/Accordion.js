import $ from 'jquery';
import 'bootstrap';
import { logger } from '../../../scripts/utils/logger';


class Accordion {
  constructor({ el }) {
    this.root = $(el);
  }
  init() {
    logger.log('Accordion Init');
  }
}

export default Accordion;
