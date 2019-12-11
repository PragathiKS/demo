import $ from 'jquery';
import { trackAnalytics } from '../../../scripts/utils/analytics';

/**
 * Method to set analytics parameters
 */
function _setAnalyticsParameters() {
  let tabTitle = $(this).text();
  if (typeof tabTitle === 'string') {
    tabTitle = tabTitle.trim().toLowerCase();
    trackAnalytics({
      linkType: 'internal',
      linkSection: `installed equipments-${tabTitle}`,
      linkParentTitle: `${tabTitle} tab`,
      linkName: tabTitle
    }, 'linkClick', 'linkClicked', undefined, false);
  }
}

class Tabs {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$tabs = this.root.find('.js-tabs__tab-link');
  }
  setAnalyticsParameters() {
    return _setAnalyticsParameters.apply(this, arguments);
  }
  bindEvents() {
    const { $tabs } = this.cache;
    const self = this;
    $tabs.on('click', function () {
      self.setAnalyticsParameters.apply(this);
    });
  }
  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default Tabs;
