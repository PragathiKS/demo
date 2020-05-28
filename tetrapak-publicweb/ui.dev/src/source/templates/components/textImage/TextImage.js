import $ from 'jquery';
import { trackAnalytics } from '../../../scripts/utils/analytics';
import { isExternal, isDownloable } from '../../../scripts/utils/updateLink';
import { getDocType } from '../../../scripts/common/common';

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

  addLinkAttr() {
    $('.js-textImage-analytics').each(function() {
      const thisHref = $(this).attr('href');
      if (thisHref) {
        if (isExternal(thisHref)) {
          $(this).attr('target', '_blank');
        }
        if (isDownloable(thisHref)) {
          $(this).attr('target', '_blank');
          $(this).data('download-type', 'download');
        }
      }
    });
  }

  trackAnalytics = e => {
    e.preventDefault();
    const $target = $(e.target);
    const $this = $target.closest('.js-textImage-analytics');
    const downloadtype = $this.data('download-type');
    let trackingKey = 'linkClick';
    let linkParentTitle = `${$this.data('anchor-type')}_${$this.data(
      'image-title'
    )}`;
    let linkSection = $this.data('link-section');
    const linkName = $this.data('link-name');
    const linkType =
      $this.attr('target') === '_blank' ? 'external' : 'internal';

    const trackingObj = {};
    let eventObject = null;

    if (downloadtype === 'download') {
      trackingObj['dwnDocName'] = $this.data('asset-name');
      linkSection = `${$this.data('anchor-type')}_Download`;
      linkParentTitle = `${$this.data('anchor-type')}_Download_${getDocType(
        $this.attr('href')
      )}_${$this.data('image-title')}`;
      trackingObj['eventType'] = 'download';
      trackingObj['dwnType'] = 'ungated';
      trackingKey = 'downloadClick';
    }

    if (downloadtype !== 'download') {
      eventObject = {
        eventType: 'linkClick',
        event: 'Text & Image'
      };
    }

    trackingObj['linkSection'] = linkSection;
    trackingObj['linkName'] = linkName;
    trackingObj['linkParentTitle'] = linkParentTitle;
    trackingObj['linkType'] = linkType;

    trackAnalytics(
      trackingObj,
      'linkClick',
      trackingKey,
      undefined,
      false,
      eventObject
    );

    window.open($this.attr('href'), $this.attr('target'));
  };

  init() {
    this.initCache();
    this.bindEvents();
    this.addLinkAttr();
  }
}

export default TextImage;
