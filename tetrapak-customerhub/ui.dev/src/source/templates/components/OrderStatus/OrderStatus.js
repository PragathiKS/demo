import $ from 'jquery';
import { render } from '../../../scripts/utils/render';

class OrderStatus {
  cache = {};
  initCache() {
    this.cache.api = $('#apiUrl').val();
    this.cache.authorization = $('#apiAuthorization').val();
  }
  init() {
    this.initCache();
    render.fn({
      template: 'orderStatusTable',
      url: this.cache.api,
      ajaxConfig: {
        method: 'GET',
        cache: true,
        dataType: 'json',
        contentType: 'application/json',
        beforeSend: (xhr) => {
          xhr.setRequestHeader('Authorization', this.cache.authorization);
        }
      },
      beforeRender(data) {
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
        }
        this.data = data.allorders;
      },
      target: '.js-orderstatus-table'
    });
  }
}

export default OrderStatus;
