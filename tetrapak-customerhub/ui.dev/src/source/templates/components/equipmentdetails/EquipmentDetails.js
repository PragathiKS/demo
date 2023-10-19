import $ from 'jquery';
import 'bootstrap';
import { render } from '../../../scripts/utils/render';
import auth from '../../../scripts/utils/auth';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { ajaxMethods } from '../../../scripts/utils/constants';
import { logger } from '../../../scripts/utils/logger';
import { trackFormStart, trackFormStepComplete, trackFormComplete, trackFormCancel, trackFormError, trackLinkClick, trackBreadcrumbLinkClick } from './EquipmentDetails.analytics';

export const getUrlQueryParams = (url) => {
  const params = {};
  url.replace(/[?&]+([^=&]+)=([^&]*)/gi, function (_, key, value) {
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
      this.cache.countryData = res1[0].data.map(({ countryCode, countryName }) => ({ key: countryCode, desc: countryName, selected: countryCode === this.cache.data.countryCode }));
      this.cache.equipmentStatuses = res2[0].data.map(({ equipmentStatus, equipmentStatusDesc }) => ({ key: equipmentStatus, desc: equipmentStatusDesc, selected: equipmentStatus === this.cache.data.equipmentStatus }));
      this.cache.$spinner.addClass('d-none');
      this.cache.$content.removeClass('d-none');
      this.renderEquipInfoCard({ update: true });
    }).fail(() => {
      this.cache.$content.removeClass('d-none');
      this.cache.$spinner.addClass('d-none');
    });
  });
}

function _getTechPubListURL(baseURL, equipData) {
  let finalURL = baseURL;

  Object.keys(equipData).forEach((key) => {
    const value = equipData[key];

    switch (key) {
      case 'countryCode': {
        finalURL += `?country=${value}`;
        break;
      }
      case 'lineCode': {
        finalURL += `&line=${value}`;
        break;
      }
      case 'customerNumber': {
        finalURL += `&customer=${value}`;
        break;
      }
      case 'material': {
        finalURL += `&material=${value}`;
        break;
      }
      case 'manufacturerSerialNumber': {
        finalURL += `&manufacturerSerialNumber=${value}`;
        break;
      }
      default: {
        break;
      }
    }
  });

  return finalURL;
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
      target: $this.cache.$content,
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
        const { i18nKeys, techPubListURL } = $this.cache;

        if (!data) {
          this.data = data = {
            isError: true,
            message: 'couldn\'t fetch equipment due to no id passed. Please provide id param in url',
            i18nKeys
          };
          data.isError = true;
        } else {
          const equipData = data.data[0];

          data.equipData = equipData;
          data.i18nKeys = i18nKeys;
          data.techPubListURL = _getTechPubListURL(techPubListURL, equipData);
          $this.cache.data = { id, ...data.equipData };
        }
      }
    }, () => {
      $this.cache.$content.removeClass('d-none');
      $this.cache.$spinner.addClass('d-none');
    });
  });
}

function _renderEquipInfoCard(view) {
  const $this = this;
  const { countryData, equipmentStatuses, data, i18nKeys } = this.cache;

  render.fn({
    template: 'equipmentDetailsInfoCard',
    data: {
      equipmentNumber: this.cache.data.id,
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

function _renderEquipUpdateModal() {
  const { $modal, i18nKeys } = this.cache;

  render.fn({
    template: 'equipmentDetailsConfirm',
    data: {
      formData: {
        ...this.cache.formData,
        country: this.cache.formData.country,
        equipmentStatus: this.cache.formData.equipmentStatus
      },
      i18nKeys: i18nKeys
    },
    target: '.js-update-modal'
  });
  $modal.modal();
}

// clear all text inputs when country dropdown value changes
function _clearFieldsOnCountryChange($form) {
  $form.find('input:not([id="equipmentTypeDesc"]), textarea').val('');
  if ($form.find('#equipmentStatus option[value="EXPO"]').length > 0) {
    $form.find('select[id="equipmentStatus"]').val('EXPO');
  }
}

function _bindFormChangeEvents() {
  const $this = this;
  const $form = $this.cache.$content.find('.js-equipment-details__form');
  const $updateBtn = $form.find('.js-equipment-details__req-update');
  const initialFormData = $form.serialize();

  $('input, textarea, select', $form).each((_, item) => {
    $(item).on('input change', () => {
      if ($form.serialize() !== initialFormData) {
        $updateBtn.removeAttr('disabled');
        if (item.id === 'country') {
          _clearFieldsOnCountryChange($form);
        }
      } else {
        $updateBtn.attr('disabled', 'disabled');
      }
    });

    if (['INPUT', 'TEXTAREA'].includes(item.nodeName)) {
      $(item).on('blur', () => {
        if ($(item).val()) {
          const sanitized = item.value.replace(/<\/?script>|[<>]/gm, '');
          $(item).val(sanitized);
          this.removeErrorMsg(item);
        }
      });
    }
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
    this.cache.equipmetListURL = this.root.data('equipment-list-url');
    this.cache.techPubListURL = this.root.data('techpub-list-url');
    this.cache.$contentWrapper = this.root.find('.tp-equipment-details__content-wrapper');
    this.cache.$content = this.root.find('.js-equipment-details__content');
    this.cache.$spinner = this.root.find('.tp-spinner');
    this.cache.$modal = this.root.parent().find('.js-update-modal');
    this.cache.formName = '';
    this.cache.countryData = [];
    this.cache.formData = {};
    this.cache.isFormValid = true;
    this.cache.data = {};
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

    this.root.on('click', '.js-equipment-details__back-btn', () => {
      window.location.href = this.cache.equipmetListURL;
    });

    this.root.on('click', '.js-equipment-details__update', () => {
      this.renderEquipInfoCardWithData();
      this.trackFormStart();
    });

    this.root.on('click', '.js-equipment-details__cancel', () => {
      this.trackFormCancel(this.cache.formName, 'Step 1', `${this.cache.data.equipmentName} - ${this.cache.data.serialNumber}`);
      this.renderEquipInfoCard({ view: true });
    });

    this.root.on('click', '.tp-equipment-details__back-btn', () => {
      const $linkName = $(this).text().trim();
      trackBreadcrumbLinkClick($linkName);
    });

    this.root.on('click', '.js-equipment-details__req-update', (e) => {
      let isFormValid = true;
      const requiredFormElements = this.root.find('input.js-equipment-details__input[required]');
      const formErrors = [];
      this.removeAllErrorMessages();
      requiredFormElements.each((_, item) => {
        if (!$(item).val()) {
          isFormValid = false;
          this.addErrorMsg(item);
          formErrors.push({
            formErrorMessage: $(item).closest('.js-equipment-details__form-element').find('.error-msg').text().trim(),
            formErrorField: $(item).closest('.js-equipment-details__form-element').find('.tp-equipment-details__info-cell-1').text().trim()
          });
        }
      });
      if (!isFormValid) {
        this.trackFormError(this.cache.formName, 'Step 1', `${this.cache.data.equipmentName} - ${this.cache.data.serialNumber}`, formErrors);
        return;
      }
      this.removeAllErrorMessages();
      const { data: equipData } = this.cache;
      const data = Object.fromEntries(new FormData(e.currentTarget.form).entries());
      this.cache.formData = {
        equipmentId: this.cache.data.id,
        oldCountry: equipData.countryName,
        oldLocation: equipData.location,
        oldSiteName: equipData.site,
        oldLineCode: equipData.lineCode,
        oldFunctionalLocationDesc: equipData.functionalLocationDesc,
        oldEquipmentStatus: equipData.equipmentStatusDesc,
        oldPosition: equipData.position,
        oldEquipmentTypeDesc: equipData.equipmentName,
        comments: data.comments,
        country: this.cache.countryData.find(country => country.key === data.country)?.desc || equipData.countryName,
        location: data.location,
        siteName: data.siteName,
        lineCode: data.lineCode,
        functionalLocationDesc: data.functionalLocationDesc,
        equipmentStatus: this.cache.equipmentStatuses.find(status => status.key === data.equipmentStatus)?.desc || equipData.equipmentStatusDesc,
        position: data.position,
        equipmentTypeDesc: data.equipmentTypeDesc,
        serialNumber: this.cache.data.serialNumber
      };

      const fields = Object.keys(this.cache.formData).filter(key => !key.startsWith('old'));
      this.cache.formFields = fields.map(key => ({ [key]: this.cache.formData[key] }));
      const trackingFormData = this.getFormFieldsArr(this.cache.formFields);
      this.trackFormStepComplete(this.cache.formName, 'Step 1', `${this.cache.data.equipmentName} - ${this.cache.data.serialNumber}`, trackingFormData);
      this.renderEquipUpdateModal();
    });

    this.root.on('click', '.js-equipment-details__req-make-update', () => {
      this.cache.$spinner.removeClass('d-none');
      const submitApi = this.cache.submitApi;
      this.showDisabledButton();

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
            if (![200, 201].includes(res.status)) {
              $('.js-equipment-details__error').removeClass('d-none');
              return;
            }
            this.cache.$content.removeClass('d-none');
            this.cache.$modal.modal('hide');
            const $heading = $('.js-update-modal').find('.tp-equipment-details__modal-header').find('h2').text().trim();
            const trackingFormData = this.getFormFieldsArr(this.cache.formFields);
            this.trackFormComplete($heading, 'Step 2', `${this.cache.data.equipmentName} - ${this.cache.data.serialNumber}`, trackingFormData);
            this.removeDisabledButton();
            this.renderEquipInfoCard({ confirmed: true });
          }).fail(() => {
            this.removeDisabledButton();
            this.cache.$content.removeClass('d-none');
            this.cache.$spinner.addClass('d-none');
          });
      });
    });

    this.root.on('click', '.js-close-btn, .js-equipment-details__conf-cancel', () => {
      const $heading = $('.js-update-modal').find('.tp-equipment-details__modal-header').find('h2').text().trim();
      this.trackFormCancel($heading, 'Step 2', `${this.cache.data.equipmentName} - ${this.cache.data.serialNumber}`);
      this.cache.$modal.modal('hide');
    });

    this.root.on('click', 'button', (e) => {
      this.trackLinkClick(this.cache.formName, e.target.textContent);
    });
  }

  // Get Analytics Tracking Form Fields
  getFormFieldsArr() {
    const $form = this.cache.$content.find('.js-equipment-details__form');
    const $formFields = [];
    $('input, textarea, select', $form).each((_, item) => {
      const closestEle = $(item).closest('.js-equipment-details__form-element');
      const fieldLabel = $(closestEle).find('.tp-equipment-details__info-cell-1');
      const formfield = fieldLabel.length > 0 ? $(fieldLabel).text().trim() : $(closestEle).find('label').text().trim();
      const fieldValue = $(item).is('select') ? $(item).find('option:selected').text() : $(item).val();
      $formFields.push({
        formFieldName: formfield,
        formFieldValue: fieldValue
      });
    });

    return $formFields;
  }

  trackFormStart() {
    const $this = this;
    const { data } = $this.cache;
    setTimeout(function () {
      $this.cache.formName = $('.js-equipment-details__info-card').find('h3').text().trim();
      trackFormStart($this.cache.formName, 'Step 1', `${data.equipmentName} - ${data.serialNumber}`);
    }, 700);
  }

  trackFormStepComplete(formName, step, heading, formFields) {
    trackFormStepComplete(formName, step, heading, formFields);
  }

  trackFormComplete(formName, step, heading, formFields) {
    trackFormComplete(formName, step, heading, formFields);
  }

  trackLinkClick(formName, link) {
    trackLinkClick(formName, link);
  }

  trackFormCancel(formName, step, heading) {
    trackFormCancel(formName, step, heading);
  }

  trackFormError(formName, step, equipment, formErrors) {
    trackFormError(formName, step, equipment, formErrors);
  }

  showDisabledButton() {
    const buttonName = this.root.find('.js-equipment-details__req-make-update');
    buttonName.attr('disabled', 'disabled');
    buttonName.append('<i class="icon icon-Loader"></i>');
  }

  removeDisabledButton() {
    this.root.find('.js-equipment-details__req-make-update').removeAttr('disabled');
    this.root.find('.js-equipment-details__req-make-update i').remove();
  }

  addErrorMsg(el) {
    $(el).closest('.js-equipment-details__form-element').addClass('tp-equipment-details__form-element--error');
  }

  removeAllErrorMessages() {
    const requiredFormElements = this.root.find('.js-equipment-details__form-element');
    requiredFormElements.each((_, item) => {
      this.removeErrorMsg(item);
    });
  }

  removeErrorMsg(el) {
    $(el).closest('.js-equipment-details__form-element').removeClass('tp-equipment-details__form-element--error');
  }

  renderEquipInfoCardWithData() {
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
