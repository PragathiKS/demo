import $ from 'jquery';
import { trackAnalytics } from '../../../scripts/utils/analytics';
import { saveAs } from 'file-saver';


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
    const linkType = $this.find('i.icon').hasClass('icon-Union') ? 'external':'internal';
    const linkName = $this.data('link-name');
    const dwnDocName = $this.data('asset-name');
    const dwnType = 'ungated';
    const eventType = 'download';
    const downloadtype = $this.find('i.icon').hasClass('icon-Download') ? 'download':'_download';
    const sectionTitle = $this.data('section-name');
    let linkSection = $this.data('link-section');
    if(downloadtype ==='download') {
      const extension = $this.attr('href').split('.').pop();
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
      saveAs($this.attr('href'), dwnDocName);
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
      window.open($this.attr('href'), $this.attr('target'));
    }

  }

  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default MediaLink;
