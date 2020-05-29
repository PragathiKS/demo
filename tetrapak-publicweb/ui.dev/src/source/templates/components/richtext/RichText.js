import $ from 'jquery';
import { trackAnalytics } from '../../../scripts/utils/analytics';
import { isExternal } from '../../../scripts/utils/updateLink';
class RichText {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$anchorLink = this.root.find('a');
    this.cache.$attributeDivId = this.root.find('.tp-pw-richText-wrapper');
  }
  bindEvents() {
    const { $anchorLink } = this.cache;
    $anchorLink.on('click', this.trackAnalytics);
  }

  trackAnalytics = e => {
    e.preventDefault();
    const $this = $(e.target);
    const linkName = $this.text();
    const thisHref = $this.attr('href');
    const linkType =  isExternal(thisHref) ? 'external':'internal'; 

    const trackingObj = {
      linkType,
      linkSection:'RTE_Text Hyperlink',
      linkParentTitle:'Text Hyperlink_RTE',
      linkName
    };
    const eventObj = {
      eventType: 'linkClick',
      event: 'RTE'
    };
    
    trackAnalytics(trackingObj, 'linkClick', 'linkClick', undefined, false, eventObj);
    window.open($this.attr('href'), $this.attr('target'));
 
  };

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default RichText;
