import { logger } from '../../../../../scripts/utils/logger';
import { testFn } from '../../../../../scripts/common/common';

class SampleComponent {
  constructor({ templates }) {
    this.templates = templates;
  }
  init() {
    testFn();
    logger.log(this.templates.testHandlebars({
      data: 'World'
    }));
    logger.log('Class based initialization successful');
  }
}

export default SampleComponent;
