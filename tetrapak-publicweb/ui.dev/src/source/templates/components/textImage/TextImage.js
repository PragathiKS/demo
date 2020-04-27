import $ from 'jquery';
import { trackAnalytics } from '../../../scripts/utils/analytics';


class TextImage {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$textImageLink = this.root.find('.js-textImage-analytics');
  }
  bindEvents() {
    this.cache.$textImageLink.on('click', this.trackAnalytics);
  }



  trackAnalytics = (e) => {
    e.preventDefault();
    const $target = $(e.target);
    const $this = $target.closest('.js-textImage-analytics');
    
    let linkParentTitle = '';
    let trackingObj = {};
    const dwnType = 'ungated';
    const eventType = 'download';
    const linkType = $this.attr('target') === '_blank'?'external':'internal';
    const linkSection = $this.data('link-section');
    const linkName = $this.data('link-name');
    const buttonLinkType = $this.data('button-link-type');
    const downloadtype = $this.data('download-type');
    const dwnDocName = $this.data('asset-name');
    const imageTitle = $this.data('image-title');

    if(buttonLinkType==='secondary' && downloadtype ==='download'){
      linkParentTitle = `CTA-Download-pdf_${imageTitle}`;
    }

    if(buttonLinkType==='link' && downloadtype ==='download'){
      linkParentTitle = `Text Hyperlink-Download-pdf_${imageTitle}`;
    }
   
    if(downloadtype ==='download'){
      trackingObj = {
        linkType,
        linkSection,
        linkParentTitle,
        linkName,
        dwnDocName,
        dwnType,
        eventType
      };
      trackAnalytics(trackingObj, 'linkClick', 'downloadClick', undefined, false);
    }

    if(downloadtype!=='download' && $this.attr('target')==='_blank'){
      window._satellite.track('linkClick');
    }

    window.open($this.attr('href'), $this.attr('target'));
  }

  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default TextImage;
