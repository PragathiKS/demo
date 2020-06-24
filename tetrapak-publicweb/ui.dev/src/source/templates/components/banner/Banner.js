import $ from 'jquery';
import { isDesktopMode } from '../../../scripts/common/common';
import { trackAnalytics } from '../../../scripts/utils/analytics';

class Banner {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$itbLink = this.root.find('.js-banner-analytics');
    this.cache.$existingBanner=this.root.find('.pw-banner__content.banner-parent');
    this.cache.$siblingBanner=this.root.find('.pw-banner__content.banner-sibling');
  }
  bindEvents() {
    const { $itbLink } = this.cache;
    if (
      isDesktopMode() ) {
      const { $existingBanner } = this.cache;
      const { $siblingBanner }= this.cache;

      $(window).on('load resize',function(){
        const bannerHeight = $existingBanner.outerHeight();
        const bannerWidth = $existingBanner.outerWidth();
        $siblingBanner.css('width',bannerWidth);
        $siblingBanner.css('height',bannerHeight);
      });
    }
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

  seoChanges() {
    const titleDiv = this.root.find('.pw-banner__content__title');
    const h1tag = titleDiv.find('h1') ;
    if( h1tag.length) {
      $(h1tag).attr('class','tpatom-heading tpatom-heading--regular');
      const h2Tag = titleDiv.find('h2')[0];
      h2Tag.parentNode.removeChild(h2Tag);
    }
  }

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.seoChanges();
  }
}

export default Banner;
