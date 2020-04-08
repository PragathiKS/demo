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
    let eventType = 'content-load';
    const linkType = $this.data('link-type');
    const linkSection = $this.data('link-section');
    const linkName = $this.data('link-name');
    const videoName = $this.data('video-name');
    const buttonLinkType = $this.data('button-link-type');
    const downloadtype = $this.data('download-type');
    const dwnDocName = $this.data('asset-name');

    if(buttonLinkType==='secondary' && downloadtype ==='hyperlink'){
      linkParentTitle = `CTA-Hyperlink_${videoName}`;
    }

    if(buttonLinkType==='link' && downloadtype ==='hyperlink'){
      linkParentTitle = `Text hyperlink_${videoName}`;
    }

    if(buttonLinkType==='secondary' && downloadtype ==='download'){
      linkParentTitle = `CTA-Download-pdf_${videoName}`;
      eventType = 'download';
    }

    if(buttonLinkType==='link' && downloadtype ==='download'){
      linkParentTitle = `Text hyperlink - Download-pdf_${videoName}`;
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

    trackAnalytics(trackingObj, 'linkClick', 'TextVideoClick', undefined, false);
    window.open($this.attr('href'), $this.attr('target'));
  }

  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default TextVideo;
