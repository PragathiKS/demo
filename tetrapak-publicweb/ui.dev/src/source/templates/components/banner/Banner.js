import $ from 'jquery';
import { trackAnalytics } from '../../../scripts/utils/analytics';

class Banner {
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
    const eventType = 'download';
    const linkType = $this.attr('target') === '_blank'?'external':'internal';
    const linkSection = $this.data('link-section');
    const linkName = $this.data('link-name');
    const bannerTitle = $this.data('link-banner-title');
    const buttonLinkType = $this.data('button-link-type');
    const downloadtype = $this.data('download-type');
    const dwnDocName = $this.data('asset-name');

    if(buttonLinkType==='secondary' && downloadtype ==='download'){
      linkParentTitle = `CTA_Download_pdf_${bannerTitle}`;
    }

    if(buttonLinkType==='link' && downloadtype ==='download'){
      linkParentTitle = `Text hyperlink_Download_pdf_${bannerTitle}`;
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
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default Banner;
