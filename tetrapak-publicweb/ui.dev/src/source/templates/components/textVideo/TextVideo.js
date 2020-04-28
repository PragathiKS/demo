import $ from 'jquery';
import { trackAnalytics } from '../../../scripts/utils/analytics';
import { initializeDAMPlayer,ytPromise,initializeYoutubePlayer } from '../../../scripts/utils/videoAnalytics';
class TextVideo {
  constructor({ el }) {
    this.root = $(el);
  }

  cache = {};

  initCache() {
    this.cache.$textVideoButton = this.root.find('.js-textVideo-analytics');
    ytPromise.then(() => { initializeYoutubePlayer(); });
    initializeDAMPlayer();
  }

  bindEvents() {
    const { $textVideoButton } = this.cache;
    $textVideoButton.on('click', this.trackAnalytics);
  }


  trackAnalytics = (e) => {
    e.preventDefault();
    const $target = $(e.target);
    const $this = $target.closest('.js-textVideo-analytics');
    let linkParentTitle = '';
    let trackingObj = {};
    const dwnType = 'ungated';
    const eventType = 'download';
    const linkType = $this.attr('target') === '_blank'?'external':'internal';
    const linkSection = $this.data('link-section');
    const linkName = $this.data('link-name');
    const videoTitle = $this.data('video-title');
    const buttonLinkType = $this.data('button-link-type');
    const downloadtype = $this.data('download-type');
    const dwnDocName = $this.data('asset-name');

    if(buttonLinkType==='secondary' && downloadtype ==='download'){
      linkParentTitle = `CTA_Download_pdf_${videoTitle}`;
    }

    if(buttonLinkType==='link' && downloadtype ==='download'){
      linkParentTitle = `Text hyperlink_Download_pdf_${videoTitle}`;
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

export default TextVideo;
