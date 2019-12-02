import $ from 'jquery';
import { trackAnalytics } from '../../../scripts/utils/analytics';

/**
 * TextVideo class definition
 */
class TextVideo {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  /**
   * Init cache
   */
  initCache() {
    this.cache.linkParentTitle = $.trim(this.root.find('.js-text-video__heading').text());
  }
  /**
   * Init component
   */
  init() {
    this.initCache();
    const self = this;
    this.root.on('click', '.js-text-video__description-link', function () {
      self.trackVideoTextLinkAnalytics.apply(self, [this]);
    });
  }
  /**
   * Method to track link click analytics tracking for text video
   * @param {object} el Current element
   */
  trackVideoTextLinkAnalytics = (el) => {
    const { linkParentTitle } = this.cache;
    trackAnalytics({
      linkType: 'internal',
      linkSection: 'dashboard',
      linkParentTitle,
      linkName: $.trim($(el).text())
    }, 'linkClick', 'linkClicked', undefined, false);
  };
}

export default TextVideo;
