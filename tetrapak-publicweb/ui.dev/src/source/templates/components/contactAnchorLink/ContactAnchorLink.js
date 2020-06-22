import $ from 'jquery';
import { trackAnalytics } from '../../../scripts/utils/analytics';

class ContactAnchorLink {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$contactAnchor = this.root.find('.js-pw-contactAnchorLink');
  }
  goToContactForm = (e) => {
    e.preventDefault();
    const $target = $(e.target);
    const $this = $target.closest('.js-pw-contactAnchorLink');
    this.trackAnalytics();
    window.open($this.attr('href'),'_self');
  }

  trackAnalytics = () => {
    const trackingObj = {
      linkType: 'internal',
      linkSection: 'FloatingButton',
      linkParentTitle: '',
      linkName: 'Contact Us'
    };
    const eventObj = {
      eventType: 'linkClick',
      event: 'Contact us envelop'
    };
    trackAnalytics(trackingObj, 'linkClick', 'linkClick', undefined, false, eventObj);
  }

  bindEvents() {
    const { $contactAnchor } = this.cache;
    $contactAnchor.on('click', this.goToContactForm);
  }
  init() {
    this.initCache();
    this.bindEvents();
  }
}
export default ContactAnchorLink;
