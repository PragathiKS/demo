import $ from 'jquery';
import MegaMenuDescription from '../megamenudescription/MegaMenuDescription';

class MegaMenuTeaser {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};

  initCache() {
    this.cache = {
      descriptionTargetClass: '.tp-pw-megamenuteaser__description'
    };
  }

  bindEvents() {
    const megaMenuDescription = new MegaMenuDescription({
      el: '.tp-pw-megamenuteaser',
      targetClass: this.cache.descriptionTargetClass
    });
    megaMenuDescription.init();
  }

  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default MegaMenuTeaser;
