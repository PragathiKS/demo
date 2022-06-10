import $ from 'jquery';
import { trackAnalytics } from '../../../scripts/utils/analytics';

class Image {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$imageAnchor = this.root.find('.js-tp-pw-image');
  }
  bindEvents() {
    const { $imageAnchor } = this.cache;
    $imageAnchor.on('click', this.trackAnalytics);
  }

  trackAnalytics = (e) => {
    e.preventDefault();
    const $target = $(e.target);
    const $this = $target.closest('.js-tp-pw-image');
    const linkType = $this.attr('data-linktype') === 'external' ? 'external' : 'internal';

    const trackingObj = {
      linkType,
      linkName: 'not available',
      linkSection: 'ImageClick',
      linkParentTitle: ''
    };
    const eventObj = {
      eventType: 'linkClick',
      event: 'image'
    };

    trackAnalytics(trackingObj, 'linkClick', 'linkClick', undefined, false, eventObj);
    if (linkType === 'external') {
      window.open($this.attr('href'), '_blank');
    } else {
      window.open($this.attr('href'), '_self');
    }
  }

  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default Image;
