/* eslint-disable no-console */
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
    console.log('Hiren Parmar', $target);
    const $this = $target.closest('.js-tp-pw-image');
    console.log('Hiren Parmar This Item', $this);
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
    console.log('Hiren Parmar LinkType', linkType);
    if (linkType === 'external') {
      console.log('Hiren Parmar External Link');
      window.open($this.attr('href'), '_blank');
    } else {
      console.log('Hiren Parmar Internal Link');
      window.open($this.attr('href'), '_self');
    }
  }

  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default Image;
