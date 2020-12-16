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
    this.trackAnalytics($this);
  }

  trackAnalytics = (el) => {
    const trackingObj = {
      linkType: 'internal',
      linkSection: 'Floating Button',
      linkParentTitle: '',
      linkName: 'Contact Us'
    };
    const eventObj = {
      eventType: 'linkClick',
      event: 'Contact us form'
    };
    trackAnalytics(trackingObj, 'linkClick', 'linkClick', undefined, false, eventObj);
    if (el.metaKey || el.ctrlKey || el.keyCode === 91 || el.keyCode === 224){
      window.open(el.attr('href'),'_blank');
    }
    else {
      window.open(el.attr('href'),'_self');
    }

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
