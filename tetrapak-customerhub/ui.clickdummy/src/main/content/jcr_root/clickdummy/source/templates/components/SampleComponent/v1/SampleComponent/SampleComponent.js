import { logger } from '../../../../../scripts/utils/logger';
import { someMethod } from '../../../../../scripts/common/common';

class SampleComponent {
  constructor({ templates }) {
    this.templates = templates;
  }
  init() {
    logger.log(this.templates.testHandlebars({
      data: 'World'
    }));
    someMethod();
    logger.log('Class based initialization successful');
  }
}

export default SampleComponent;
