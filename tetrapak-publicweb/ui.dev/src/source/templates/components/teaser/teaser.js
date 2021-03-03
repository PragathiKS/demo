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
          if(!$(this).data('link-section').includes('_Download')){
            $(this).data('link-section', $(this).data('link-section') + '_Download');
          }
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
    const mainHeading= $this.data('paren-link-title');
    let linkParentTitle = '';
    let trackingObj = {};
    let eventObj = {};
    const dwnType = 'ungated';
    const eventType = 'download';
    const linkType = $this.attr('target') === '_blank' ? 'external' : 'internal';
    const linkSection = $this.data('link-section');
    const linkName = $this.data('link-name');
    const buttonLinkType = $this.data('button-link-type') || 'link';
    const downloadtype = $this.data('download-type');
    const dwnDocName = $this.data('asset-name');
    const linkTitle = $this.data('link-title');
    const teaserTitle= mainHeading + ':' + linkTitle;
    let extension = '';
    if(downloadtype === 'download'){
      extension = $this.attr('href').split('.').pop();
    }


    if (buttonLinkType === 'secondary' && downloadtype === 'download') {
      linkParentTitle = `CTA_Download_${extension}_${linkTitle}`;
    }

    if (buttonLinkType === 'link' && downloadtype === 'download') {
      linkParentTitle = `Text hyperlink_Download_${extension}_${linkTitle}`;
    }

    if (buttonLinkType === 'secondary' && downloadtype !== 'download') {
      linkParentTitle = `CTA_${linkTitle}`;
    }

    if (buttonLinkType === 'link' && downloadtype !== 'download') {
      linkParentTitle = `Text hyperlink_${linkTitle}`;
    }
    if(linkSection === 'Teaser_imageClick') {
      linkParentTitle = '' || teaserTitle;
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

      eventObj = {
        eventType: 'downloadClick',
        event: 'Teaser'
      };
      trackAnalytics(trackingObj, 'linkClick', 'downloadClick', undefined, false, eventObj);
    }

    if (downloadtype !== 'download') {
      trackingObj = {
        linkType,
        linkSection,
        linkParentTitle,
        linkName
      };
      
      eventObj = {
        eventType: 'linkClick',
        event: 'Teaser'
      };
      trackAnalytics(trackingObj, 'linkClick', 'linkClick', undefined, false, eventObj);
    }
    if (linkType === 'internal') {
      if (e.metaKey || e.ctrlKey || e.keyCode === 91 || e.keyCode === 224){ 
        window.open($this.attr('href'),'_blank');
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
    this.addLinkAttr();
  }
}

export default Teaser;