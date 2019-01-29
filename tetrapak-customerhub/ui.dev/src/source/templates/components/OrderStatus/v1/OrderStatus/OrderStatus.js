import { ajaxWrapper } from '../../../../../scripts/utils/ajax';
import $ from 'jquery';

class OrderStatus {
  constructor({ templates }) {
    this.templates = templates;
  }
  init() {
    ajaxWrapper.getXhrObj({
      url: '/etc/designs/tp-customerhub/jsonData/OrderStatus.json',
      method: 'GET'
    }).done((data) => {
      if (data && data.length) {
        data.forEach(item => {
          const contactSummaryList = Object.keys(item.contact).map(key => {
            if (key === 'mobile') {
              return `<a href="tel:${item.contact[key]}">${item.contact[key]}</a>`;
            }
            if (key === 'email') {
              return `<a href="mailTo:${item.contact[key]}">${item.contact[key]}</a>`;
            }
          });
          item.contactSummary = contactSummaryList.join(' ');
        });
      }
      $('.js-orderstatus-table').html(this.templates.orderStatusTable(data));
    });
  }
}

export default OrderStatus;
