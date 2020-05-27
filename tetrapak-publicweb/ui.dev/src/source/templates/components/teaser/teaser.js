import $ from 'jquery';
import { trackAnalytics } from '../../../scripts/utils/analytics';
import { isExternal } from '../../../scripts/utils/updateLink';


class Teaser {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$teaserLink = this.root.find('.js-teaser-analytics');
  }
  bindEvents() {
    this.cache.$teaserLink.on('click', this.trackAnalytics);
  }
  addLinkAttr() {
    $('.js-teaser-analytics').each(function () {
      const thisHref = $(this).attr('href');
      if (thisHref) {
        if (isExternal(thisHref)) {
          $(this).attr('target', '_blank');
          $(this).data('download-type', 'download');
          $(this).data('link-section', $(this).data('link-section') + '_Download');
        } else {
          $(this).data('download-type', 'hyperlink');
        }
      }
    });
  }



  trackAnalytics = (e) => {
    e.preventDefault();
    const $target = $(e.target);
    const $this = $target.closest('.js-teaser-analytics');
    let linkParentTitle = '';
    let trackingObj = {};
    const dwnType = 'ungated';
    const eventType = 'download';
    const linkType = $this.attr('target') === '_blank' ? 'external' : 'internal';
    const linkSection = $this.data('link-section');
    const linkName = $this.data('link-name');
    const buttonLinkType = $this.data('button-link-type');
    const downloadtype = $this.data('download-type');
    const dwnDocName = $this.data('asset-name');
    const linkTitle = $this.data('link-title');


    if (buttonLinkType === 'secondary' && downloadtype === 'download') {
      linkParentTitle = `CTA_Download_pdf_${linkTitle}`;
    }

    if (buttonLinkType === 'link' && downloadtype === 'download') {
      linkParentTitle = `Text hyperlink_Download_pdf_${linkTitle}`;
    }

    if (downloadtype === 'download') {
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
    
    window.open($this.attr('href'), $this.attr('target'));
  }

  init() {
    this.initCache();
    this.bindEvents();
    this.addLinkAttr();
  }
}

export default Teaser;
