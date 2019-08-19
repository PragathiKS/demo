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
function _trackAnalytics(type, el) {
  const { heading } = this.cache;
  const analyticsData = {
    linkType: 'internal',
    linkSection: 'tablist'
  };

  switch (type) {
    case 'tabclick': {
      analyticsData.linkParentTitle = heading.toLowerCase();
      analyticsData.linkName = $.trim($(el).text());
      analyticsData.linkListPos = $(el).data('index') + 1;
      break;
    }
    case 'descriptionLink': {
      analyticsData.linkType = $(el).attr('target') === '_blank' ? 'external' : 'internal';
      analyticsData.contentName = $.trim($(el).parents('.js-tablist').find('.js-tablist__event:not(.collapsed)').text());
      analyticsData.linkParentTitle = $.trim($(el).parents('.js-tablist').find('.collapse.show').find('.tp-tablist__event-detail-title').text());
      analyticsData.linkName = $.trim($(el).text());
      break;
    }
    default:
      break;
  }

  trackAnalytics(analyticsData, 'linkClick', 'linkClicked', undefined, false);
}

class TabsList {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.selectedTabIndex = 0;
    this.cache.componentId = this.root.find('#tabComponentId').val();
    this.cache.heading = $.trim(this.root.find('.js-tablist__heading').text());
  }
  bindEvents() {
    const $this = this;
    this.root
      .on('click', '.js-tablist__event', function () {
        const self = $(this);
        if (!self.hasClass('active')) {
          $this.showTabDetail(self.data('target'));
          $this.trackAnalytics('tabclick', this);
        }
        $this.root.find('.js-tablist__event').removeClass('active');
        self.addClass('active').toggleClass('m-active');
      })
      .on('click', '.js-tablist__event-detail-description-link', function () {
        $this.trackAnalytics('descriptionLink', this);
      })
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
  trackAnalytics = (type, el) => _trackAnalytics.apply(this, [type, el]);
  init() {
    this.initCache();
    this.bindEvents();
    this.renderFirstTab();
  }
}

export default TabsList;
