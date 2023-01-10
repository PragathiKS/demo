import $ from 'jquery';
import { render } from '../../../scripts/utils/render';
import { logger } from '../../../scripts/utils/logger';
import auth from '../../../scripts/utils/auth';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { ajaxMethods } from '../../../scripts/utils/constants';
import { getI18n } from '../../../scripts/common/common';
import { trackFormStart, trackFormComplete, trackFormError, trackLinkClick } from './AddEquipment.analytics.js';
/**
 * Render form
 */
function _renderLayout($this) {
  render.fn({
    template: 'addEquipmentForm',
    target: '.js-tp-add-equipment__content-wrapper',
    data: { subTitle: $this.cache.subTitle, detailsSubtitle: $this.cache.detailsSubtitle, i18nKeys: $this.cache.i18nKeys, country: $this.cache.countryData, equipmentStatus: $this.cache.statusData, allCountries:  $this.cache.allCountriesData}
  }, () => {
    $this.cache.$contentWrapper.removeClass('d-none');
    $this.cache.$spinner.addClass('d-none');
  });
}

/**
 * Render sites dropdown
 */
function _renderSites($this, isDisabled) {
  render.fn({
    template: 'addEquipmentSites',
    target: '.js-tp-add-equipment__site-wrapper',
    data: { i18nKeys: $this.cache.i18nKeys, site: $this.cache.siteData, isDisabled: isDisabled ? true : false}
  });
}

/**
 * Render lines dropdown
 */
function _renderLines($this, isDisabled) {
  render.fn({
    template: 'addEquipmentLines',
    target: '.js-tp-add-equipment__line-wrapper',
    data: { i18nKeys: $this.cache.i18nKeys, line: $this.cache.lineData, isDisabled: isDisabled ? true : false}
  });
}

/**
 * Fetch dropdown options
 */
function _getDropdownData($this) {
  auth.getToken(({ data: authData }) => {
    $.when(ajaxWrapper
      .getXhrObj({
        url: $this.cache.countryApi,
        method: ajaxMethods.GET,
        cache: true,
        dataType: 'json',
        contentType: 'application/json',
        beforeSend(jqXHR) {
          jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
          jqXHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        },
        showLoader: true
      }), ajaxWrapper
      .getXhrObj({
        url: $this.cache.statusApi,
        method: ajaxMethods.GET,
        cache: true,
        dataType: 'json',
        contentType: 'application/json',
        beforeSend(jqXHR) {
          jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
          jqXHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        },
        showLoader: true
      }), ajaxWrapper
      .getXhrObj({
        url: $this.cache.allCountriesApi,
        method: ajaxMethods.GET,
        cache: true,
        dataType: 'json',
        contentType: 'application/json',
        beforeSend(jqXHR) {
          jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
          jqXHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        },
        showLoader: true
      }), ajaxWrapper
    ).then((resCountry, resStatus, resAllCountries) => {
      $this.cache.countryData = resCountry[0].data.map(({ countryCode, countryName }) => ({ key: countryName, desc: countryName, code: countryCode }));
      $this.cache.statusData = resStatus[0].data.map(item => ({ key: item.equipmentStatus, desc: item.equipmentStatusDesc }));
      $this.cache.allCountriesData = resAllCountries[0].data.map(({ countryCode, countryName }) => ({ key: countryName, desc: countryName, code: countryCode }));

      _renderLayout($this);
    }).fail(e => {
      logger.error(e);
      _renderLayout($this);
    });
  });
}

/**
 * Gets country code based on country name
 */
function _getCountryCode($this, countryName) {
  const countryItem = $this.cache.countryData.find(country => country.key === countryName);

  return countryItem.code;
}

/**
 * Fetch Site dropdown options
 */
function _getSitesDropdownData($this, countryName) {
  const siteApi = $this.cache.siteApi;
  const countryCode = _getCountryCode($this, countryName);
  auth.getToken(({ data: authData }) => {
    ajaxWrapper
      .getXhrObj({
        url: `${siteApi}?countrycodes=${countryCode}`,
        method: ajaxMethods.GET,
        cache: true,
        dataType: 'json',
        contentType: 'application/json',
        beforeSend(jqXHR) {
          jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
          jqXHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        },
        showLoader: true
      }).done(resSite => {
        $this.cache.siteData = resSite.data.map(item => ({ key: item.id, desc: item.siteName }));
        _renderSites($this, false);
      }).fail((e) => {
        logger.error(e);
        _renderSites($this, false);
      });
  });
}

/**
 * Fetch Line dropdown options
 */
function _getLinesDropdownData($this, siteCode) {
  const lineApi = $this.cache.lineApi;
  const countryCode = $this.cache.countryCode;
  auth.getToken(({ data: authData }) => {
    ajaxWrapper
      .getXhrObj({
        url: `${lineApi}?countrycodes=${countryCode}&sites=${siteCode}`,
        method: ajaxMethods.GET,
        cache: true,
        dataType: 'json',
        contentType: 'application/json',
        beforeSend(jqXHR) {
          jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
          jqXHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        },
        showLoader: true
      }).done(resLine => {
        $this.cache.lineData = resLine.data.map(item => ({ key: item.id, desc: item.lineCode }));
        const NewLineKey  = `${getI18n($this.cache.i18nKeys['newLineOption'])}`;
        const newLine = {key : NewLineKey , desc: NewLineKey};
        $this.cache.lineData.push(newLine);
        _renderLines($this, false);
      }).fail((e) => {
        logger.error(e);
        _renderLines($this, false);
      });
  });
}

/**
 * Render form or fetch data for dropdowns
 */
function _renderForm() {
  const $this = this;
  if ($this.cache.countryData && $this.cache.countryData.length &&
      $this.cache.statusData && $this.cache.statusData.length) {
    _renderLayout($this);
  } else {
    _getDropdownData($this);
  }
}
/**
 * Render files after upload
 */
function _renderFiles() {
  const $this = this;
  const $fileExtensionError = $this.root.find('.js-tp-add-equipment__file-error');
  const obj = $this.cache.files.map(obj => ({ name: obj.name, size: `${(obj.size / (1024 * 1024)).toFixed(2)  } MB`, removeFileLabel: $this.cache.i18nKeys.dragAndDropRemoveFileLabel, isError: true }));
  render.fn({
    template: 'addEquipmentFiles',
    target: '.js-tp-add-equipment__drag-and-drop-files-container',
    data: obj
  });

  $fileExtensionError.attr('hidden', 'hidden');
}
/**
 * Render submit form
 */
function _renderSubmit() {
  const $this = this;
  render.fn({
    template: 'addEquipmentSubmit',
    target: '.js-tp-add-equipment__content-wrapper',
    data: { i18nKeys: $this.cache.i18nKeys }
  }, () => {
    $this.cache.$contentWrapper.removeClass('d-none');
    $this.cache.$spinner.addClass('d-none');
  });
}

class AddEquipment {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.files = [];
    this.cache.firstInteract = false;
    this.cache.$contentWrapper = this.root.find('.js-tp-add-equipment__content-wrapper');
    this.cache.$spinner = this.root.find('.js-tp-spinner');
    this.cache.configJson = this.root.find('.js-tp-add-equipment__config').text();
    try {
      this.cache.i18nKeys = JSON.parse(this.cache.configJson);
    } catch (e) {
      this.cache.i18nKeys = {};
      logger.error(e);
    }
    this.cache.submitApi = this.root.data('submit-api');
    this.cache.countryApi = this.root.data('country-api');
    this.cache.allCountriesApi = this.root.data('all-countries-api');
    this.cache.countryCode = null;
    this.cache.statusApi = this.root.data('status-api');
    this.cache.siteApi = this.root.data('site-api');
    this.cache.lineApi = this.root.data('line-api');
    this.cache.subTitle = this.root.data('subtitle');
    this.cache.detailsSubtitle = this.root.data('details-subtitle');
    this.cache.countryData = [];
    this.cache.allCountriesData = [];
    this.cache.statusData = [];
    this.cache.siteData = [];
    this.cache.lineData = [];
  }
  bindEvents() {
    const $this = this;
    $(window.document).on('dragenter dragover drop', e => {
      $this.dragAndDropPreventDefault(e);
    });

    $this.root.on('dragenter dragleave drop', '.js-tp-add-equipment__drag-and-drop', e => {
      $this.dragAndDropPreventDefault(e);
      if (e.type === 'drop') {
        $this.dropFiles(e, $this, true);
      }
    });

    $this.root.on('click', '.js-tp-add-equipment__drag-and-drop-button', () => {
      $this.addInputTypeFile();
    });

    $this.root.on('click', '.js-tp-add-equipment__drag-and-drop-file-remove-container', e => {
      $this.removeFile(e);
    });

    $this.root.on('click', '.js-tp-add-equipment__submit', e => {
      e.preventDefault();
      let isFormValid = true;
      $this.removeAllErrorMessages();
      const formErrors = [];
      const requiredFrmElements = $this.root.find('.js-tp-add-equipment__form-element [required]');
      requiredFrmElements.each(function () {
        if (!$(this).val()) {
          isFormValid = false;
          $this.addErrorMsg(this);
          formErrors.push({
            formErrorMessage: $(this).closest('.js-tp-add-equipment__form-element').find('.error-msg').text().trim(),
            formErrorField: ($(this).is('input') || $(this).is('textarea')) ? $(this).closest('.js-tp-add-equipment__form-element').find('.tp-add-equipment__field-label').text().trim() : $(this).closest('.js-tp-add-equipment__form-element').find('.tpatom-dropdown__text').text().trim()
          });
        }
      });
      if (isFormValid) {
        $this.cache.$contentWrapper.addClass('d-none');
        $this.cache.$spinner.removeClass('d-none');
        $this.submitForm(e);
      } else {
        const formName = this.root.find('.js-tp-add-equipment__title').text().trim();
        trackFormError(formName, formErrors);
      }
    });

    $this.root.on('click', '.js-tp-add-equipment__add-another-equipment', (e) => {
      $this.trackLinkClick(e);
      $this.cache.$contentWrapper.addClass('d-none');
      $this.cache.$spinner.removeClass('d-none');
      $this.cache.files = [];
      $this.cache.firstInteract = false;
      $this.renderForm();
    });

    $this.root.on('blur', 'textarea, input', (e) => {
      const sanitized = e.target.value.replace(/<\/?script>|[<>]/gm, '');
      $(e.target).val(sanitized);
    });

    $this.root.on('focus', 'textarea, input, select', () => {
      if (!$this.cache.firstInteract) {
        $this.cache.firstInteract = true;
        $this.trackFormStart();
      }
    });

    $this.root.on('change', '#addEquipmentCountry', (e) => {
      const countryName = $(e.target).val();
      this.cache.countryCode = _getCountryCode($this, countryName);

      // disabled Sites and Lines when changing country
      _renderSites($this, true);
      _renderLines($this, true);

      _getSitesDropdownData($this, countryName);
    });

    $this.root.on('change', '#addEquipmentSite', (e) => {
      const siteCode = $(e.target).val();
      _getLinesDropdownData($this, siteCode);
    });
  }
  trackLinkClick(e) {
    const formName = this.root.find('.js-tp-add-equipment__title').text().trim();
    const linkName = $(e.currentTarget).text().trim();
    trackLinkClick(formName, linkName);
  }
  trackFormStart() {
    const formName = this.root.find('.js-tp-add-equipment__title').text().trim();
    trackFormStart(formName);
  }
  submitForm(e) {
    const $this = this;
    const formData = new FormData(e.currentTarget.form);

    if ($this.cache.files && $this.cache.files.length > 0) {
      for (let i = 0; i < $this.cache.files.length; i++) {
        formData.append('files', $this.cache.files[i]);
      }
    }

    const statusId = formData.get('addEquipmentEquipmentStatus');
    const statusDesc = $this.cache.statusData.find(el => statusId === el.key)?.desc;
    formData.set('addEquipmentEquipmentStatus', statusDesc);

    auth.getToken(({ data: authData }) => {
      ajaxWrapper
        .getXhrObj({
          url: $this.cache.submitApi,
          method: ajaxMethods.POST,
          cache: true,
          processData: false,
          contentType: false,
          data: formData,
          beforeSend(jqXHR) {
            jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
          },
          showLoader: true
        }).done(() => {
          const formFields = [];
          for(const data of formData.entries()) {
            const $el = $(`#${  data[0]}`);
            formFields.push({
              formFieldName: ($el.is('input') || $el.is('textarea')) ? $el.closest('.js-tp-add-equipment__form-element').find('.tp-add-equipment__field-label').text().trim() : $el.closest('.js-tp-add-equipment__form-element').find('.tpatom-dropdown__text').text().trim(),
              formFieldValue: data[1]
            });
          }
          const formName = this.root.find('.js-tp-add-equipment__title').text().trim();
          trackFormComplete(formName, formFields);
          $this.renderSubmit();
        }).fail(() => {
          $this.cache.$contentWrapper.removeClass('d-none');
          $this.cache.$spinner.addClass('d-none');
        });
    });
  }
  addErrorMsg(el) {
    const formElement = $(el).closest('.js-tp-add-equipment__form-element');
    formElement.addClass('tp-add-equipment__form-element--error');
    formElement.find('.error-msg').addClass('error-msg--active');
  }
  removeAllErrorMessages() {
    const $this = this;
    const requiredFrmElements = $this.root.find('.error-msg--active');
    requiredFrmElements.each(function () {
      $this.removeErrorMsg(this);
    });
  }
  removeErrorMsg(el) {
    $(el).removeClass('error-msg--active');
    $(el).closest('.js-tp-add-equipment__form-element').removeClass('tp-add-equipment__form-element--error');
  }
  removeFile(e) {
    const index = $(e.currentTarget).data('index');
    this.cache.files.splice(index, 1);
    this.renderFiles();
    this.setFieldsMandatory();
  }
  addInputTypeFile() {
    const input = document.createElement('input');
    input.style.cssText = 'display: none;';
    input.type = 'file';
    input.multiple = true;
    document.body.appendChild(input);
    input.addEventListener('change', (event) => this.dropFiles(event, this), false);
    $(input).trigger('click');
    document.body.removeChild(input);
  }
  dropFiles(e, $this, dropEvent) {
    let files;
    const currentFilesCount = $this.cache.files.length;
    if (dropEvent) {
      files = e.originalEvent.dataTransfer.files;
    } else {
      files = e.target.files;
    }
    for (let i = 0; i < files.length; i++) {
      if ($this.filterFiles(files[i], $this)) {
        $this.cache.files.push(files[i]);
      }
    }

    if ($this.cache.files.length > currentFilesCount) {
      $this.renderFiles();
      $this.setFieldsMandatory();
    }
  }
  filterFiles(file, $this) {
    const maxFileSize = 10 * 1024 * 1024;
    const blockedExtensions = /(\.exe|\.zip)$/i;
    const filePath = file.name;
    const $fileExtensionError = $this.root.find('.js-tp-add-equipment__file-error');

    if (blockedExtensions.exec(filePath) || (file.size > maxFileSize)) {
      $fileExtensionError.removeAttr('hidden');
      return false;
    }

    return file.size < maxFileSize;
  }
  dragAndDropPreventDefault(e) {
    e.stopPropagation();
    e.preventDefault();
  }
  setFieldsMandatory() {
    const $this = this;
    const fields = $this.root.find('.js-tp-add-equipment__toggle-mandatory');
    const isFileUploaded = $this.cache.files.length ? 1 : 0;
    fields.each(function () {
      if (isFileUploaded) {
        $(this).find('input, select, textarea').removeAttr('required');
        $this.removeErrorMsg($(this).find('.error-msg'));
      } else {
        $(this).find('input, select, textarea').attr('required', true);
      }
    });
  }
  renderForm() {
    return _renderForm.apply(this, arguments);
  }
  renderFiles() {
    return _renderFiles.apply(this, arguments);
  }
  renderSubmit() {
    return _renderSubmit.apply(this, arguments);
  }
  init() {
    this.initCache();
    this.renderForm();
    this.bindEvents();
  }
}

export default AddEquipment;
