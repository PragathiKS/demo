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

function  _renderEquipInfoCard(view) {
  const $this = this;
  const { data, i18nKeys } = $this.cache;

  render.fn({
    template: 'equipmentDetailsInfoCard',
    data: {
      ...data,
      i18nKeys: i18nKeys,
      isEquipUpdate: view && view.update,
      isEquipView: view && view.view,
      isConfirmation: view && view.confirmed
    },
    target: '.js-equipment-details__info-card'
  });

  if (view && view.update) {
    $this.bindFormChangeEvents();
  }
}

function  _renderEquipUpdateModal() {
  const $this = this;
  const { $modal, i18nKeys } = $this.cache;
  const $form = $this.cache.$contentWrapper.find('.js-equipment-details__form');
  const $formInputs = $form.find('input');
  let formData = {};

  $formInputs.each((index, item) => {
    const inputName = $(item).attr('name');
    const inputVal = $(item).val();
    formData[inputName] = inputVal;
  });

  render.fn({
    template: 'equipmentDetailsConfirm',
    data: {
      formData: formData,
      i18nKeys: i18nKeys
    },
    target: '.js-update-modal'
  });
  $modal.modal();
}

function _bindFormChangeEvents() {
  const $this = this;
  const $form = $this.cache.$contentWrapper.find('.js-equipment-details__form');
  const $formInputs = $form.find('input, textarea');
  const $updateBtn = $form.find('.js-equipment-details__req-update');
  let initialFormData = $form.serialize();
  let formData = '';

  $formInputs.each((index, item) => {
    const $input = $(item);
    $input.on('change input', () => {
      formData = $form.serialize();
      if (formData !== initialFormData) {
        $updateBtn.removeAttr('disabled');
      } else {
        $updateBtn.attr('disabled', 'disabled');
      }
    });
  });

  console.log($form.serialize());
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
    this.cache.$modal = this.root.parent().find('.js-update-modal');

    try {
      this.cache.i18nKeys = JSON.parse(this.cache.configJson);
    } catch (e) {
      this.cache.i18nKeys = {};
      logger.error(e);
    }
  }

  bindEvents() {
    this.root.on('click', '.js-equipment-details__update',  (e) => {
      this.renderEquipInfoCard({update: true});
    });

    this.root.on('click', '.js-equipment-details__cancel',  (e) => {
      this.renderEquipInfoCard({view: true});
    });

    this.root.on('click', '.js-equipment-details__thanks',  (e) => {
      this.renderEquipInfoCard({confirmed: true});
    });

    this.root.on('click', '.js-equipment-details__req-update',  (e) => {
      this.renderEquipUpdateModal();
    });

    this.root.on('click', '.js-close-btn, .js-equipment-details__conf-cancel',  () => {
      this.cache.$modal.modal('hide');
    });
  }

  renderEquipmentDetails() {
    return _renderEquipmentDetails.apply(this, arguments);
  }

  renderEquipInfoCard(view) {
    return _renderEquipInfoCard.call(this, view);
  }

  renderEquipUpdateModal() {
    return _renderEquipUpdateModal.call(this);
  }

  bindFormChangeEvents() {
    return _bindFormChangeEvents.apply(this, arguments);
  }

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.renderEquipmentDetails();
  }
}

export default EquipmentDetails;
