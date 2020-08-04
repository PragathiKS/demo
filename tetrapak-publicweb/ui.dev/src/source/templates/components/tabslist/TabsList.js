import $ from 'jquery';
import 'bootstrap';
import { trackAnalytics } from '../../../scripts/utils/analytics';
import { isExternal } from '../../../scripts/utils/updateLink';
import { pauseVideosByReference, initializeYoutubePlayer, removeYTReferences, ytPromise, initializeDAMPlayer } from '../../../scripts/utils/videoAnalytics';
import { logger } from '../../../scripts/utils/logger';

function _renderFirstTab() {
  const { componentId } = this.cache;
  const $tabSection = this.root.find('.js-tablist__events-sidesection');
  $tabSection.html($(`#tab_${componentId}_0`).html());
  $tabSection.find('.js-yt-player, .js-dam-player').removeClass('video-init');
  ytPromise.then(() => {
    initializeYoutubePlayer();
  }).catch( err => {
    logger.log('err in Tablist>>>',err);
  });
  initializeDAMPlayer();
}

class TabsList {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.selectedTabIndex = 0;
    this.cache.componentId = this.root.find('#tabComponentId').val();
    this.cache.activeTheme = this.root.find('#activeTheme').val();
    this.cache.tabButton = this.root.find('.js-pw-tablist__event');
    this.cache.heading = $.trim(this.root.find('.js-tablist__heading').text());
  }
  bindEvents() {
    const $this = this;
    const { activeTheme, tabButton } = this.cache;
    this.root
      .on('click', '.js-tablist__event', function () {
        const self = $(this);
        if (!self.hasClass(`active--${activeTheme}`)) {
          $this.showTabDetail(self.data('target'));
        }
        $this.root.find('.js-tablist__event').removeClass(`active--${activeTheme}`);
        self.addClass(`active--${activeTheme}`).toggleClass('m-active');
      })
      .on('click', '.js-tablist__event-detail-description-link', this.trackAnalytics)
      .on('hidden.bs.collapse', '.collapse', this.pauseVideoIfExists);

    tabButton.on('click', this.trackAnalyticsTabs);
  }
  showTabDetail = (el) => {
    const $tabSection = this.root.find('.js-tablist__events-sidesection');
    removeYTReferences($tabSection.find('.js-yt-player'));
    $tabSection.html($(el).html());
    $tabSection.find('.js-yt-player, .js-dam-player').removeClass('video-init');
    ytPromise.then(() => {
      initializeYoutubePlayer();
    });
    initializeDAMPlayer();
  }
  pauseVideoIfExists() {
    pauseVideosByReference($(this).find('.is-playing'));
  }
  renderFirstTab = () => _renderFirstTab.call(this);

  trackAnalyticsTabs = (e) => {
    const $target = $(e.target);
    const $this = $target.closest('.js-pw-tablist__event');
    const trackingObj = {
      linkType:'internal',
      linkSection:$this.data('link-section'),
      linkParentTitle:$this.data('parent-title'),
      linkName: $this.data('link-name')
    };

    const eventObj = {
      eventType: 'linkClick',
      event: 'tablist'
    };
    trackAnalytics(trackingObj, 'linkClick', 'linkClick', undefined, false, eventObj);
    return true;
  }
  trackAnalytics = (e) => {
    e.preventDefault();
    const $target = $(e.target);
    const $this = $target.closest('.js-tablist__event-detail-description-link');
    let linkParentTitle = '';
    let trackingObj = {};
    let eventObj = {};
    const dwnType = 'ungated';
    let eventType = 'content-load';
    const linkType = $this.attr('target') === '_blank' ? 'external' : 'internal';
    const linkSection = $this.data('link-section');
    const linkName = $this.data('link-name');
    const buttonLinkType = $this.data('button-link-type');
    const downloadtype = $this.data('download-type');
    const dwnDocName = $this.data('asset-name');
    const tabTitle = $this.data('tab-title');
    const componentName = $this.data('component-name');
    let extension = '';
    if(downloadtype === 'download'){
      extension = $this.attr('href').split('.').pop();
    }

    if (buttonLinkType === 'secondary' && downloadtype === 'download') {
      linkParentTitle = `CTA_Download_${extension}_${tabTitle}`;
      eventType = 'download';
    }

    if (buttonLinkType === 'link' && downloadtype === 'download') {
      linkParentTitle = `Text hyperlink_Download_${extension}_${tabTitle}`;
      eventType = 'download';
    }

    if (buttonLinkType === 'secondary' && downloadtype !== 'download') {
      linkParentTitle = `CTA_${tabTitle}`;
    }

    if (buttonLinkType === 'link' && downloadtype !== 'download') {
      linkParentTitle = `Text hyperlink_${tabTitle}`;
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
        event: componentName
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
        event: componentName
      };
      trackAnalytics(trackingObj, 'linkClick', 'linkClick', undefined, false, eventObj);
    }

    window.open($this.attr('href'), $this.attr('target'));
  }

  addLinkAttr() {
    $('.js-tablist__event-detail-description-link').each(function () {
      const thisHref = $(this).attr('href');
      if (thisHref) {
        if (isExternal(thisHref)) {
          $(this).attr('target', '_blank');
          $(this).data('download-type', 'download');
          //$(this).data('download-type', 'download');
          $(this).data('link-section', $(this).data('link-section') + '_Download');
          $(this).attr('rel', 'noopener noreferrer');
        } else {
          $(this).data('download-type', 'hyperlink');
        }
      }
    });
  }

  init() {
    this.initCache();
    this.bindEvents();
    this.renderFirstTab();
    this.addLinkAttr();
  }
}

export default TabsList;
