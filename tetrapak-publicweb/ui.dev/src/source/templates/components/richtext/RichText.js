import $ from 'jquery';
import { trackAnalytics } from '../../../scripts/utils/analytics';
import { getUrl, isExternal } from '../../../scripts/utils/updateLink';
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
    const firstH1 = this.cache.$attributeDivId.find('h1:first').text();
    const firstH2 = this.cache.$attributeDivId.find('h2:first').text();
    const parentTitleText = firstH1 || firstH2 || 'RTE';

    const trackingObj = {
      linkType,
      linkSection:'RTE_Text Hyperlink',
      linkParentTitle:`Text Hyperlink_${parentTitleText}`,
      linkName
    };
    const eventObj = {
      eventType: 'linkClick',
      event: 'RTE'
    };
    
    trackAnalytics(trackingObj, 'linkClick', 'linkClick', undefined, false, eventObj);
    if (e.metaKey || e.ctrlKey || e.keyCode === 91 || e.keyCode === 224) {
      window.open(getUrl($this), '_blank');
    } else {
      window.open(getUrl($this),'_self');
    }
 
  };

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default RichText;
