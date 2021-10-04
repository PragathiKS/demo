import $ from 'jquery';
import 'bootstrap';
import { render } from '../../../scripts/utils/render';
import auth from '../../../scripts/utils/auth';
import {ajaxMethods} from '../../../scripts/utils/constants';
import {logger} from '../../../scripts/utils/logger';


/**
 * Fetch and render the Equipment Details
 */
function _renderEquipmentDetails() {
  const $this = this;
  auth.getToken(({ data: authData }) => {
    render.fn({
      template: 'equipmentDetails',
      url: {
        path: $this.cache.detailsApi + '/8000000930' // TODO: id will be passed by URL param.
      },
      target: '.js-equipment-details__content',
      ajaxConfig: {
        method: ajaxMethods.GET,
        beforeSend(jqXHR) {
          jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
          jqXHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        },
        cache: true,
        showLoader: true,
        cancellable: true
      },
      beforeRender(data) {
        const { i18nKeys } = $this.cache;

        if (!data) {
          this.data = data = {
            isError: true,
            i18nKeys
          };
        } else {
          data.equipData = data.data[0];
          data.i18nKeys = i18nKeys;
          $this.cache.data = data;
        }
      }
    }, () => {
      $this.cache.$contentWrapper.removeClass('d-none');
      $this.cache.$spinner.addClass('d-none');
    });
  });
}

class EquipmentDetails {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};

  initCache() {
    this.cache.configJson = this.root.find('.js-equipment-details__config').text();
    this.cache.detailsApi = this.root.data('details-api');
    this.cache.$contentWrapper = this.root.find('.tp-equipment-details__content-wrapper');
    this.cache.$spinner = this.root.find('.tp-spinner');

    try {
      this.cache.i18nKeys = JSON.parse(this.cache.configJson);
    } catch (e) {
      this.cache.i18nKeys = {};
      logger.error(e);
    }
  }

  renderEquipmentDetails() {
    return _renderEquipmentDetails.apply(this, arguments);
  }

  init() {
    /* Mandatory method */
    this.initCache();
    this.renderEquipmentDetails();
  }
}

export default EquipmentDetails;
