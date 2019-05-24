import $ from 'jquery';
import { trackAnalytics } from '../../../scripts/utils/analytics';

function _trackAnalytics() {
  let obj = {
    linkType: 'internal',
    linkSection: 'dashboard',
    linkParentTitle: 'contacts',
    linkName: 'view contatcs'
  };
  trackAnalytics(obj, 'linkClick', 'linkClicked', undefined, false);
}
class ContactCard {
  constructor({ el }) {
    this.root = $(el);
  }

  bindEvents() {
    this.root.find('.js-view-contact-card-btn').on('click', this.trackAnalytics);
  }
  trackAnalytics() {
    _trackAnalytics.call(this);
  }
  init() {
    this.bindEvents();
  }
}

export default ContactCard;
