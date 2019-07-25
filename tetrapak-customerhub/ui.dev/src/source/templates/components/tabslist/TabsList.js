import $ from 'jquery';
import 'bootstrap';
import { trackAnalytics } from '../../../scripts/utils/analytics';
import { isDesktop } from '../../../scripts/common/common';


function _renderFirstTab() {
  this.root.find('.js-tablist__events-sidesection').html($('#Event0').html());
}
/**
 * Fire analytics on click of
 * any tab list
 */
function _trackAnalytics(type, element) {
  const analyticsData = {
    linkType: 'internal',
    linkSection: 'tablist'
  };

  switch (type) {
    case 'tabclick': {
      analyticsData.linkParentTitle = this.cache.heading.toLowerCase();
      analyticsData.linkName = $(element).text().trim();
      analyticsData.linkListPos = parseInt($(element).data('target').substr(6), 10) + 1;
      break;
    }
    case 'descriptionLink': {
      analyticsData.linkType = $(element).attr('target') === '_blank' ? 'external' : 'internal';
      analyticsData.contentName = $(element).parents().find('.js-tablist__event:not(.collapsed)').text().trim();
      analyticsData.linkParentTitle = $(element).parents().find('.collapse.show').find('.tp-tablist__event-detail-title').text();
      analyticsData.linkName = $(element).text().trim();
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
    this.cache.heading = this.root.find('.js-tablist__config').text().trim();
  }
  bindEvents() {
    const $this = this;
    this.root
      .on('click', '.js-tablist__event', function (e) {
        const index = parseInt($(this).data('target').substr(6), 10);
        if ($this.cache.selectedTabIndex === index && isDesktop) {
          e.stopImmediatePropagation();
        }
        else {
          $this.cache.selectedTabIndex = index;
          $this.showTabDetail($(this).data('target'));
          $this.trackAnalytics('tabclick', this);
        }
      })
      .on('click', '.js-tablist__event-detail-description-link', function () {
        $this.trackAnalytics('descriptionLink', this);
      });
  }
  showTabDetail = (detailTargetEle) => {
    this.root.find('.js-tablist__events-sidesection').html($(detailTargetEle).html());
  }

  renderFirstTab = () => _renderFirstTab.call(this);
  trackAnalytics = (type, element) => _trackAnalytics.call(this, type, element);
  init() {
    this.initCache();
    this.bindEvents();
    this.renderFirstTab();
  }
}

export default TabsList;
