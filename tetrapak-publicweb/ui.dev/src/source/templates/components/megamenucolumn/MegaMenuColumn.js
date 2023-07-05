import $ from 'jquery';

class MegaMenuColumn {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  
  initCache() {
    this.cache = {
      $elements: {
        header: this.root.find('.tp-pw-megamenuheading'),
        teaserHeader: this.root.find('.tp-pw-megamenuteaser__header-wrapper')
      }
    };
  }

  bindSetMarginIfNoHeaderEvent = () => {
    const header = this.cache.$elements.header.get(0);
    const teaserHeader = this.cache.$elements.teaserHeader.get(0);

    if (!header && !teaserHeader) {
      this.root.css('padding-top', '74px');
    }
  }

  bindEvents() {
    this.bindSetMarginIfNoHeaderEvent();
  }
  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default MegaMenuColumn;
