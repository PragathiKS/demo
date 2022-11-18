import $ from 'jquery';
import { logger } from '../../../scripts/utils/logger';

class Footer {
  constructor({ el }) {
    this.root = $(el);
  }

  init() {
    logger.log('Footer Init');
  }
}

export default Footer;
