import { logger } from '../../../../../scripts/utils/logger';

class SampleComponent {
  constructor({ templates }) {
    this.templates = templates;
  }
  init() {
    logger.log(this.templates.testHandlebars({
      data: 'World'
    }));
    logger.log('Class based initialization successful');
  }
}

export default SampleComponent;
