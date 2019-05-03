import $ from 'jquery';
import { $html } from '../../../scripts/utils/commonSelectors';
import { trackAnalytics } from '../../../scripts/utils/analytics';

/**
 * Method to set analytics parameters
 */
function _setAnalyticsParameters() {
  let { currentPageTitle } = this.cache;
  if (typeof currentPageTitle === 'string') {
    currentPageTitle = currentPageTitle.trim().toLowerCase();
    trackAnalytics({
      linkType: 'internal',
      linkSection: `installed equipments-${currentPageTitle}`,
      linkParentTitle: `${currentPageTitle} tab`,
      linkName: currentPageTitle
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
    this.cache.currentPageTitle = $.trim($html.find('title').text());
  }
  setAnalyticsParameters() {
    return _setAnalyticsParameters.apply(this, arguments);
  }
  bindEvents() {
    const { $tabs } = this.cache;
    $tabs.on('click', () => {
      this.setAnalyticsParameters();
    });
  }
  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default Tabs;
