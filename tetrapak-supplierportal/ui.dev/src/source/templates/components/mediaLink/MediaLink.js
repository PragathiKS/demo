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
    let linkParentTitle = '';
    let trackingObj = {};
    let eventObj = {};
    // const linkType = $this.find('i.icon').is('.icon-External_Link, .icon-PDF, .icon-Download') ? 'external':'internal';
    const linkType = $this.data('link-type');
    const linkName = $this.data('link-name');
    const dwnDocName = $this.data('asset-name');
    const dwnType = 'ungated';
    const eventType = 'download';
    const downloadtype = $this.find('i.icon').hasClass('icon-PDF') ? 'download':'_download';
    const sectionTitle = $this.data('section-name');
    let linkSection = $this.data('link-section');
    if(downloadtype ==='download') {
      const extension = $this.attr('href') && $this.attr('href').split('.').pop();
      linkParentTitle = `Text Hyperlink_Download_${extension}_${sectionTitle}`;
      linkSection = 'Related links and downloads_Text Hyperlink_Download';
      trackingObj = {
        linkType,
        linkSection,
        linkParentTitle,
        linkName,
        dwnDocName,
        dwnType,
        eventType
      };

      eventObj = {
        eventType: 'downloadClick',
        event: 'Related links and downloads'
      };
      trackAnalytics(trackingObj, 'linkClick', 'downloadClick', undefined, false, eventObj);
    }

    if (downloadtype !== 'download') {
      linkParentTitle = `Text Hyperlink_${sectionTitle}`;
      trackingObj = {
        linkType,
        linkSection,
        linkParentTitle,
        linkName
      };

      eventObj = {
        eventType: 'linkClick',
        event: 'Related links and downloads'
      };
      trackAnalytics(trackingObj, 'linkClick', 'linkClick', undefined, false, eventObj);
    }
    if (linkType === 'internal') {
      if (e.metaKey || e.ctrlKey || e.keyCode === 91 || e.keyCode === 224){
        window.open($this.attr('href'), '_blank');
      }
      else {
        window.open($this.attr('href'),'_self');
      }
    }
    else {
      window.open($this.attr('href'), $this.attr('target'));
    }
  }

  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default MediaLink;
