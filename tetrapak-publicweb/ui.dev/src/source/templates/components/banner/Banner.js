import $ from 'jquery';
import { isExternal } from '../../../scripts/utils/updateLink';
import { isDesktopMode } from '../../../scripts/common/common';
import { trackAnalytics } from '../../../scripts/utils/analytics';

class Banner {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$itbLink = this.root.find('.js-banner-analytics');
    this.cache.$existingBanner = this.root.find('.pw-banner__content.banner-parent');
    this.cache.$siblingBanner = this.root.find('.pw-banner__content.banner-sibling');
  }
  bindEvents() {
    const { $itbLink } = this.cache;
    if (
      isDesktopMode()) {
      const { $existingBanner } = this.cache;
      const { $siblingBanner } = this.cache;

      $(window).on('load resize', function () {
        const bannerHeight = $existingBanner.outerHeight();
        const bannerWidth = $existingBanner.outerWidth();
        $siblingBanner.css('width', bannerWidth);
        $siblingBanner.css('height', bannerHeight);
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
    const linkType = $this.attr('target') === '_blank' ? 'external' : 'internal';
    const linkSection = $this.data('link-section');
    const linkName = $this.data('link-name');
    const bannerTitle = $this.data('link-banner-title');
    const buttonLinkType = $this.data('button-link-type');
    const downloadtype = $this.data('download-type');
    const dwnDocName = $this.data('asset-name');

    if (buttonLinkType === 'secondary' && downloadtype === 'download') {
      linkParentTitle = `CTA_Download_pdf_${bannerTitle}`;
    }

    if (buttonLinkType === 'link' && downloadtype === 'download') {
      linkParentTitle = `Text hyperlink_Download_pdf_${bannerTitle}`;
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

    if (downloadtype !== 'download' && $this.attr('target') === '_blank') {
      window._satellite.track('linkClick');
    }

    window.open($this.attr('href'), $this.attr('target'));
  }

  addLinkAttr() {
    this.root.find('a').each(function () {
      const thisHref = $(this).attr('href');
      if (thisHref) {
        if (isExternal(thisHref)) {
          $(this).attr('target', '_blank');
          $(this).data('download-type', 'download');
          $(this).data('link-section', $(this).data('link-section') + '_Download');
          $(this).attr('rel', 'noopener noreferrer');
          $(this).data('link-type', 'external');
        } else {
          $(this).data('link-type', 'internal');
        }
      }
    });
  }
  addBannerLink() {
    const $bEl = $('.pw-banner');
    const $anchor = $('.pw-banner').data('href');
    if ($anchor) {
      $bEl.click((e) => {
        if($(e.target).closest('.pw-banner__contentwrapper').length) {
          return true;
        }
        window.location.href = $anchor;
      });
    }
  }


  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.addLinkAttr();
    this.addBannerLink();
  }
}

export default Banner;
