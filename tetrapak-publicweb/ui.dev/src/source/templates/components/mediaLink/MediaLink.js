import $ from 'jquery';
import { trackAnalytics } from '../../../scripts/utils/analytics';


class MediaLink {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$textImageLink = this.root.find('.js-medialink-analytics');
  }
  bindEvents() {
    this.cache.$textImageLink.on('click', this.trackAnalytics);
  }

  trackAnalytics = (e) => {
    e.preventDefault();
    const $target = $(e.target);
    const $this = $target.closest('.js-medialink-analytics');
    
    const linkParentTitle = '';
    let trackingObj = {};
    const linkType = $this.hasClass('icon-external') ? 'external':'internal';
    const linkName = $this.data('link-name');
    const dwnDocName = $this.data('asset-name');
    const dwnType = 'ungated';
    const eventType = 'download';
    const downloadtype = $this.hasClass('icon-download') ? 'download':'_download';
    const linkSection = `medialink-${downloadtype}`;
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
    } else {
      window._satellite.track('linkClick');
    }
    window.open($this.attr('href'), $this.attr('target'));
  }

  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default MediaLink;
