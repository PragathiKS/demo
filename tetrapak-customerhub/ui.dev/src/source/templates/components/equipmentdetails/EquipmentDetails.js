import $ from 'jquery';
import 'bootstrap';
import { render } from '../../../scripts/utils/render';
import auth from '../../../scripts/utils/auth';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import {ajaxMethods} from '../../../scripts/utils/constants';
import {logger} from '../../../scripts/utils/logger';

export const getUrlQueryParams = (url) => {
  const params = {};
  url.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(_, key, value) {
    return params[key] = value;
  });
  return params;
};

/* eslint-disable */

/**
 * Fetch and render countries and statuses
 */

 function _renderEquipInfoCardWithData() {
  this.cache.$spinner.removeClass('d-none');
  auth.getToken(({ data: authData }) => {
    $.when(ajaxWrapper
      .getXhrObj({
        url: this.cache.countryApi,
        method: ajaxMethods.GET,
        cache: true,
        dataType: 'json',
        contentType: 'application/json',
        beforeSend(jqXHR) {
          jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
          jqXHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        },
        showLoader: true
      }), ajaxWrapper.getXhrObj({
        url: this.cache.statusApi,
        method: ajaxMethods.GET,
        cache: true,
        dataType: 'json',
        contentType: 'application/json',
        beforeSend(jqXHR) {
          jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
          jqXHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        },
        showLoader: true
      })).then((res1, res2) => {
        this.cache.countryData = res1[0].data.map(({ countryCode, countryName }) => ({ key: countryCode, desc: countryName }));
        this.cache.statuses = res2[0].data.map(item => ({ key: item.equipmentStatus, desc: item.equipmentStatusDesc }));
        this.cache.$spinner.addClass('d-none');
        this.cache.$contentWrapper.removeClass('d-none');
        this.renderEquipInfoCard({update: true});
      }).fail(() => {
        this.cache.$contentWrapper.removeClass('d-none');
        this.cache.$spinner.addClass('d-none');
      });
  });
  return params;
};

/**
 * Fetch and render the Equipment Details
 */
function _renderEquipmentDetails() {
  const $this = this;
  const { id } = getUrlQueryParams(window.location.href);
  const equipId = id || this.cache.id;
  auth.getToken(({ data: authData }) => {
    render.fn({
      template: 'equipmentDetails',
      url: {
        path: `${$this.cache.detailsApi}/${equipId}`
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
          $this.cache.id = equipId;
          $this.cache.serialNumber = data.equipData.manufacturerSerialNumber;
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
  const { countryData, id, statuses, data, i18nKeys } = this.cache;

  render.fn({
    template: 'equipmentDetailsInfoCard',
    data: {
      equipmentNumber: id,
      ...data,
      statuses: statuses,
      countries: countryData,
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

  render.fn({
    template: 'equipmentDetailsConfirm',
    data: {
      formData: this.cache.formData,
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
    this.cache.countryApi = this.root.data('country-api');
    this.cache.detailsApi = this.root.data('details-api');
    this.cache.statusApi = this.root.data('status-api');
    this.cache.submitApi = this.root.data('submit-api');
    this.cache.$contentWrapper = this.root.find('.tp-equipment-details__content-wrapper');
    this.cache.$spinner = this.root.find('.tp-spinner');
    this.cache.$modal = this.root.parent().find('.js-update-modal');
    this.cache.countryData = [];
    this.cache.formData = {};
    this.cache.id = '8000000930';
    this.cache.statuses = [];
    this.cache.$updateBtn = this.root.find('.js-equipment-details__req-make-update');

    try {
      this.cache.i18nKeys = JSON.parse(this.cache.configJson);
    } catch (e) {
      this.cache.i18nKeys = {};
      logger.error(e);
    }
  }

  bindEvents() {
    this.root.on('click', '.js-equipment-details__update',  (e) => {
      this.renderEquipInfoCardWithData();
    });

    this.root.on('click', '.js-equipment-details__cancel',  (e) => {
      this.renderEquipInfoCard({view: true});
    });

    // this.root.on('click', '.js-equipment-details__thanks',  (e) => {
    //   this.renderEquipInfoCard({confirmed: true});
    // });

    this.root.on('blur', 'textarea', (e) => {
      console.log('blur textarea: ', e);
      let sanitized = e.target.value.replace(/<\/?script>|[<>]/gm, "");
      $(e.target).val(sanitized);
    })

    this.root.on('click', '.js-equipment-details__req-update',  (e) => {
      e.preventDefault();
      const data = Object.fromEntries(new FormData(e.currentTarget.form).entries());
      const { equipData } = this.cache.data;
      this.cache.formData = {
        equipmentId: this.cache.id,
        oldCountry: equipData.countryName,
        oldLocation: equipData.location,
        oldSiteName: equipData.siteName,
        oldLineName: equipData.lineName,
        oldEquipmentStatus: equipData.equipmentStatus,
        oldPosition: equipData.position,
        oldEquipmentTypeDesc: equipData.equipmentTypeDesc,
        ...data,
        serialNumber: this.cache.serialNumber
      }
      this.renderEquipUpdateModal();
    });

    this.root.on('click', '.js-equipment-details__req-make-update',  (e) => {
      this.cache.$spinner.removeClass('d-none');
      const submitApi = this.cache.submitApi;
      auth.getToken(({ data: authData }) => {
        ajaxWrapper
          .getXhrObj({
            url: submitApi,
            method: ajaxMethods.POST,
            cache: true,
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify(this.cache.formData),
            beforeSend(jqXHR) {
              jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
            },
            showLoader: true
          }).done(res => {
            console.log('res: ', res);
            this.cache.$spinner.addClass('d-none');
            this.cache.$contentWrapper.removeClass('d-none');
            this.cache.$modal.modal('hide');
            this.renderEquipInfoCard({confirmed: true});
          }).fail(() => {
            this.cache.$contentWrapper.removeClass('d-none');
            this.cache.$spinner.addClass('d-none');
          });
      });
    });

    this.root.on('click', '.js-close-btn, .js-equipment-details__conf-cancel',  () => {
      this.cache.$modal.modal('hide');
    });
  }

  renderEquipInfoCardWithData(){
    return _renderEquipInfoCardWithData.apply(this, arguments);
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
