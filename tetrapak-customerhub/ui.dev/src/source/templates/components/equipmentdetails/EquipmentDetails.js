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
      this.cache.equipmentStatuses = res2[0].data.map(item => ({ key: item.equipmentStatus, desc: item.equipmentStatusDesc }));
      this.cache.$spinner.addClass('d-none');
      this.cache.$contentWrapper.removeClass('d-none');
      this.renderEquipInfoCard({update: true});
    }).fail(() => {
      this.cache.$contentWrapper.removeClass('d-none');
      this.cache.$spinner.addClass('d-none');
    });
  });
}

/**
 * Fetch and render the Equipment Details
 */
function _renderEquipmentDetails() {
  const $this = this;
  const { id } = getUrlQueryParams(window.location.href);
  auth.getToken(({ data: authData }) => {
    render.fn({
      template: 'equipmentDetails',
      url: {
        path: `${$this.cache.detailsApi}/${id}`
      },
      target: '.js-equipment-details__content',
      ajaxConfig: {
        method: ajaxMethods.GET,
        beforeSend(jqXHR) {
          jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
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
            message: 'couldn\'t fetch equipment due to no id passed. Please provide id param in url',
            i18nKeys
          };
          data.isError = true;
        } else {
          data.equipData = data.data[0];
          data.i18nKeys = i18nKeys;
          $this.cache.id = id;
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
  const { countryData, id, equipmentStatuses, data, i18nKeys } = this.cache;

  render.fn({
    template: 'equipmentDetailsInfoCard',
    data: {
      equipmentNumber: id,
      ...data,
      statuses: equipmentStatuses,
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
  const { $modal, i18nKeys } = this.cache;

  render.fn({
    template: 'equipmentDetailsConfirm',
    data: {
      formData: {
        ...this.cache.formData,
        country: this.cache.countryData.find(country => country.key === this.cache.formData.country)?.desc,
        equipmentStatus: this.cache.equipmentStatuses.find(status => status.key === this.cache.formData.equipmentStatus)?.desc
      },
      i18nKeys: i18nKeys
    },
    target: '.js-update-modal'
  });
  $modal.modal();
}

function _bindFormChangeEvents() {
  const $this = this;
  const $form = $this.cache.$contentWrapper.find('.js-equipment-details__form');
  const $updateBtn = $form.find('.js-equipment-details__req-update');
  const initialFormData = $form.serialize();

  $('input, textarea, select', $form).each((_, item) => {
    $(item).on('blur', () => {
      if ($form.serialize() !== initialFormData && $('#position').val()) {
        $updateBtn.removeAttr('disabled');
      } else {
        $updateBtn.attr('disabled', 'disabled');
      }
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
    this.cache.countryApi = this.root.data('country-api');
    this.cache.detailsApi = this.root.data('details-api');
    this.cache.statusApi = this.root.data('status-api');
    this.cache.submitApi = this.root.data('submit-api');
    this.cache.$contentWrapper = this.root.find('.tp-equipment-details__content-wrapper');
    this.cache.$spinner = this.root.find('.tp-spinner');
    this.cache.$modal = this.root.parent().find('.js-update-modal');
    this.cache.countryData = [];
    this.cache.formData = {};
    this.cache.id;
    this.cache.equipmentStatuses = [];
    this.cache.$updateBtn = this.root.find('.js-equipment-details__req-make-update');

    try {
      this.cache.i18nKeys = JSON.parse(this.cache.configJson);
    } catch (e) {
      this.cache.i18nKeys = {};
      logger.error(e);
    }
  }

  bindEvents() {
    this.root.on('click', '.js-equipment-details__update',  () => {
      this.renderEquipInfoCardWithData();
    });

    this.root.on('click', '.js-equipment-details__cancel',  () => {
      this.renderEquipInfoCard({view: true});
    });

    this.root.on('blur', 'textarea', (e) => {
      const sanitized = e.target.value.replace(/<\/?script>|[<>]/gm, '');
      $(e.target).val(sanitized);
    });

    this.root.on('click', '.js-equipment-details__req-update',  (e) => {
      e.preventDefault();
      const data = Object.fromEntries(new FormData(e.currentTarget.form).entries());
      const { equipData } = this.cache.data;
      this.cache.formData = {
        equipmentId: this.cache.id,
        oldCountry: this.cache.countryData.find(country => country.desc === equipData.countryName)?.key,
        oldLocation: equipData.location,
        oldSiteName: equipData.siteName,
        oldLineName: equipData.lineName,
        oldEquipmentStatus: equipData.equipmentStatus,
        oldPosition: equipData.position,
        oldEquipmentTypeDesc: equipData.equipmentTypeDesc,
        ...data,
        serialNumber: this.cache.serialNumber
      };
      this.renderEquipUpdateModal();
    });

    this.root.on('click', '.js-equipment-details__req-make-update',  () => {
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
            this.cache.$spinner.addClass('d-none');
            if(![200, 201].includes(res.status)) {
              $('.js-equipment-details__error').removeClass('d-none');
            }
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
