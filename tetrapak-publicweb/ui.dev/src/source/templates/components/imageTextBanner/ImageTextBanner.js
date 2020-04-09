import $ from 'jquery';
import { trackAnalytics } from '../../../scripts/utils/analytics';

class ImageTextBanner {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$itbLink = this.root.find('.js-banner-analytics');
  }
  bindEvents() {
    const { $itbLink } = this.cache;
    $itbLink.on('click', this.trackAnalytics);
  }

  trackAnalytics = (e) => {
    e.preventDefault();
    const $target = $(e.target);
    const $this = $target.closest('.js-banner-analytics');
    let linkParentTitle = '';
    let trackingObj = {};
    const dwnType = 'ungated';
    let eventType = 'content-load';
    const linkType = $this.data('link-type');
    const linkSection = $this.data('link-section');
    const linkName = $this.data('link-name');
    const imageText = $this.data('data-link-name');
    const buttonLinkType = $this.data('button-link-type');
    const downloadtype = $this.data('download-type');
    const dwnDocName = $this.data('asset-name');

    if(buttonLinkType==='secondary' && downloadtype ==='hyperlink'){
      linkParentTitle = `CTA-Hyperlink_${imageText}`;
    }

    if(buttonLinkType==='link' && downloadtype ==='hyperlink'){
      linkParentTitle = `Text hyperlink_${imageText}`;
    }

    if(buttonLinkType==='secondary' && downloadtype ==='download'){
      linkParentTitle = `CTA-Download-pdf_${imageText}`;
      eventType = 'download';
    }

    if(buttonLinkType==='link' && downloadtype ==='download'){
      linkParentTitle = `Text hyperlink - Download-pdf_${imageText}`;
      eventType = 'download';
    }

    if(downloadtype !=='download'){
      trackingObj = {
        linkType,
        linkSection,
        linkParentTitle,
        linkName,
        eventType
      };
    }else{
      trackingObj = {
        linkType,
        linkSection,
        linkParentTitle,
        linkName,
        dwnDocName,
        dwnType,
        eventType
      };
    }

    trackAnalytics(trackingObj, 'linkClick', 'bannerClick', undefined, false);
    window.open($this.attr('href'), $this.attr('target'));
  }
  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default ImageTextBanner;
