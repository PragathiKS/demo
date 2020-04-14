import $ from 'jquery';
import 'bootstrap';
import { trackAnalytics } from '../../../scripts/utils/analytics';
import { pauseVideosByReference, initializeYoutubePlayer, removeYTReferences, ytPromise, initializeDAMPlayer } from '../../../scripts/utils/videoAnalytics';


function _renderFirstTab() {
  const { componentId } = this.cache;
  const $tabSection = this.root.find('.js-tablist__events-sidesection');
  $tabSection.html($(`#tab_${componentId}_0`).html());
  $tabSection.find('.js-yt-player, .js-dam-player').removeClass('video-init');
  ytPromise.then(() => {
    initializeYoutubePlayer();
  });
  initializeDAMPlayer();
}
/**
 * Fire analytics on click of
 * any tab list
 */
// function _trackAnalytics(type, el) {
//   const { heading } = this.cache;
//   const currentEl = $(el);

//   const analyticsData = {
//     linkType: 'internal',
//     linkSection: 'tablist'
//   };

//   switch (type) {
//   case 'tabclick': {
//     analyticsData.linkParentTitle = $.trim(heading.toLowerCase());
//     analyticsData.linkName = $.trim(currentEl.text());
//     analyticsData.linkListPos = currentEl.data('index') + 1;
//     break;
//   }
//   case 'descriptionLink': {
//     const activeTab = currentEl.parents('.js-tablist').find('.js-tablist__event.active');
//     analyticsData.linkType = currentEl.attr('target') === '_blank' ? 'external' : 'internal';
//     analyticsData.contentName = $.trim(activeTab.text());
//     analyticsData.linkParentTitle = $.trim(activeTab.next('.show').find('.js-tablist__event-detail-title').text());
//     analyticsData.linkName = $.trim(currentEl.text());
//     break;
//   }
//   default:
//     break;
//   }

//   trackAnalytics(analyticsData, 'linkClick', 'linkClicked', undefined, false);
// }

class TabsList {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.selectedTabIndex = 0;
    this.cache.componentId = this.root.find('#tabComponentId').val();
    this.cache.activeTheme = this.root.find('#activeTheme').val();
    this.cache.heading = $.trim(this.root.find('.js-tablist__heading').text());
  }
  bindEvents() {
    const $this = this;
    const { activeTheme } = this.cache;
    this.root
      .on('click', '.js-tablist__event', function () {
        const self = $(this);
        if (!self.hasClass(`active--${activeTheme}`)) {
          $this.showTabDetail(self.data('target'));
          // $this.trackAnalytics('tabclick', this);
        }
        $this.root.find('.js-tablist__event').removeClass(`active--${activeTheme}`);
        self.addClass(`active--${activeTheme}`).toggleClass('m-active');
      })
      .on('click', '.js-tablist__event-detail-description-link', this.trackAnalytics)
      .on('hidden.bs.collapse', '.collapse', this.pauseVideoIfExists);
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
  // trackAnalytics = (type, el) => _trackAnalytics.apply(this, [type, el]);
  trackAnalytics = (e) => {
    e.preventDefault();
    const $target = $(e.target);
    const $this = $target.closest('.js-tablist__event-detail-description-link');
    let linkParentTitle = '';
    let trackingObj = {};
    const dwnType = 'ungated';
    let eventType = 'content-load';
    const linkType = $this.attr('target') === '_blank'?'external':'internal';
    const linkSection = $this.data('link-section');
    const linkName = $this.data('link-name');
    // const videoName = $this.data('video-name');
    const buttonLinkType = $this.data('button-link-type');
    const downloadtype = $this.data('download-type');
    const dwnDocName = $this.data('asset-name');
    const tabType = $this.data('tab-type') === 'textVideo'?'TextVideoClick':'TextImageClick';

    if(buttonLinkType==='secondary' && downloadtype ==='download'){
      linkParentTitle = `CTA-Download-pdf_${dwnDocName}`;
      eventType = 'download';
    }

    if(buttonLinkType==='link' && downloadtype ==='download'){
      linkParentTitle = `Text hyperlink - Download-pdf_${dwnDocName}`;
      eventType = 'download';
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
      trackAnalytics(trackingObj, 'linkClick', tabType, undefined, false);
    }

    if($this.attr('target')==='_blank'){
      window._satellite.track('linkClick');
    }

    window.open($this.attr('href'), $this.attr('target'));
  }

  init() {
    this.initCache();
    this.bindEvents();
    this.renderFirstTab();
  }
}

export default TabsList;
