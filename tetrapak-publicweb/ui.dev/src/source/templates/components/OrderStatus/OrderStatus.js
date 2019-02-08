import { ajaxWrapper } from '../../../scripts/utils/ajax';
import $ from 'jquery';

class OrderStatus {
  constructor({ templates }) {
    this.templates = templates;
  }
  cache = {};
  initCache() {
    this.cache.api = $('#apiUrl').val();
    this.cache.authorization = $('#apiAuthorization').val();
  }
  init() {
    this.initCache();
    ajaxWrapper.getXhrObj({
      url: this.cache.api,
      method: 'GET',
      cache: true,
      dataType: 'json',
      contentType: 'application/json',
      beforeSend: (xhr) => {
        xhr.setRequestHeader('Authorization', this.cache.authorization);
      }
    }).done((data) => {
      if (
        data
        && data.allorders
        && data.allorders.length
      ) {
        data.allorders = data.allorders.slice(0, 10);
        data.allorders.forEach(item => {
          const contactDetails = item.contactDetails;
          if (contactDetails) {
            const contactSummaryList = Object.keys(contactDetails).map(key => {
              if (key === 'mobile') {
                return `<a href="tel:${contactDetails[key]}">${contactDetails[key]}</a>`;
              }
              if (key === 'email') {
                return `<a href="mailTo:${contactDetails[key]}">${contactDetails[key]}</a>`;
              }
            });
            item.contactSummary = contactSummaryList.join(' ');
          }
        });
        $('.js-orderstatus-table').html(this.templates.orderStatusTable(data.allorders));
      }
    });
  }
}

export default OrderStatus;
