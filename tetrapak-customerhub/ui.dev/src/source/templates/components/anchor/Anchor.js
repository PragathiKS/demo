import $ from 'jquery';
import { scrollToElement } from '../../../scripts/common/common';
import { trackAnalytics } from '../../../scripts/utils/analytics';

class Anchor {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$anchorLink = this.root.find('a');
  }
  bindEvents() {
    const { $anchorLink } = this.cache;
    $anchorLink.on('click', this.scrollToSection);
  }

  scrollToSection = e => {
    e.preventDefault();
    const $this = $(e.target);
    const anchorId = $this.data('link-section');
    const linkName = $this.data('link-name');

    const trackingObj = {
      linkType: 'internal',
      linkSection: `Hyperlink click`,
      linkParentTitle: '',
      linkName
    };

    const eventObj = {
      eventType: 'linkClick',
      event: 'Anchor Tag'
    };
    trackAnalytics(trackingObj, 'linkClick', 'linkClick', undefined, false, eventObj);

    //end of analytics call
    location.hash = anchorId;
    scrollToElement(null,`#${anchorId}`);
  };

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default Anchor;
