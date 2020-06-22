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
    const compTitle = $(`#${anchorId}`).attr('title');
    const firstH1 = $(`#${anchorId}`).find('h1:first').text();
    const firstH2 = $(`#${anchorId}`).find('h2:first').text();
    const parentTitleText = firstH1 || firstH2 || compTitle;
    
    const trackingObj = {
      linkType: 'internal',
      linkSection: `Anchor Tag_${parentTitleText}`,
      linkParentTitle:'Text Hyperlink_Anchor Tag',
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
