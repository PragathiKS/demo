import $ from 'jquery';
import { render } from '../../../scripts/utils/render';
import { logger } from '../../../scripts/utils/logger';
import auth from '../../../scripts/utils/auth';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { ajaxMethods } from '../../../scripts/utils/constants';

function _renderLayout($this) {
  render.fn({
    template: 'addEquipmentForm',
    target: '.js-tp-add-equipment__content-wrapper',
    data: { i18nKeys: $this.cache.i18nKeys, country: $this.cache.countryData, line: $this.cache.lineData , site: $this.cache.siteData, equipmentStatus: $this.cache.statusData }
  }, () => {
    $this.cache.$contentWrapper.removeClass('d-none');
    $this.cache.$spinner.addClass('d-none');
  });
}
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
      })).then((res1, res2) => {
      $this.cache.countryData = res1[0].data.map(({ countryCode, countryName }) => ({ key: countryCode, desc: countryName }));
      $this.cache.statusData = res2[0].data.map(item => ({ key: item.equipmentStatus, desc: item.equipmentStatusDesc }));
      _renderLayout($this);
    }).fail(() => {
      $this.cache.$contentWrapper.removeClass('d-none');
      $this.cache.$spinner.addClass('d-none');
    });
  });
}
function _renderForm() {
  const $this = this;
  if ($this.cache.countryData && $this.cache.countryData.length && $this.cache.statusData && $this.cache.statusData.length) {
    _renderLayout($this);
  } else {
    _getDropdownData($this);
  }
}
function _renderFiles() {
  const $this = this;
  const obj = $this.cache.files.map(obj => ({ name: obj.name, size: `${(obj.size / (1024 * 1024)).toFixed(2)  } MB`, removeFileLabel: $this.cache.i18nKeys.removeFileLabel }));
  render.fn({
    template: 'addEquipmentFiles',
    target: '.js-tp-add-equipment__drag-and-drop-files-container',
    data: obj
  });
}
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
    this.cache.$contentWrapper = this.root.find('.js-tp-add-equipment__content-wrapper');
    this.cache.$spinner = this.root.find('.js-tp-spinner');
    this.cache.configJson = this.root.find('.js-tp-add-equipment__config').text();
    try {
      this.cache.i18nKeys = JSON.parse(this.cache.configJson);
    } catch (e) {
      this.cache.i18nKeys = {};
      logger.error(e);
    }
    const dummyArray  = [ { key: 'A', desc: 'A' }, { key: 'B', desc: 'B'}, { key: 'C', desc: 'C'} ];
    this.cache.countryApi = this.root.data('country-api');
    this.cache.statusApi = this.root.data('status-api');
    this.cache.countryData = [];
    this.cache.statusData = [];
    this.cache.siteData = dummyArray;
    this.cache.lineData = dummyArray;
    this.cache.equipmentStatus = dummyArray;
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

    $this.root.on('click', '.js-tp-add-equipment__drag-and-drop-file-remove-container', event => {
      $this.removeFile(event);
    });

    $this.root.on('click', '.js-tp-add-equipment__submit', () => {
      let isFormValid = true;
      $this.root.find('.error-msg--active').removeClass('error-msg--active');
      const requiredFrmElements = $this.root.find('.js-tp-add-equipment__form-element [required]');
      requiredFrmElements.each(function () {
        if (!$(this).val()) {
          isFormValid = false;
          $(this).closest('.js-tp-add-equipment__form-element').find('.error-msg').addClass('error-msg--active');
        }
      });
      // isFormValid = true;
      if (isFormValid) {
        $this.cache.$contentWrapper.addClass('d-none');
        $this.cache.$spinner.removeClass('d-none');
        setTimeout(() => {
          $this.renderSubmit();
        }, 5000);
      }
    });

    $this.root.on('click', '.js-tp-add-equipment__add-another-equipment', () => {
      $this.cache.$contentWrapper.addClass('d-none');
      $this.cache.$spinner.removeClass('d-none');
      $this.renderForm();
    });
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
    input.click();
    document.body.removeChild(input);
  }
  dropFiles(e, $this, dropEvent) {
    let files;
    if (dropEvent) {
      files = e.originalEvent.dataTransfer.files;
    } else {
      files = e.target.files;
    }
    for (let i = 0; i < files.length; i++) {
      if ($this.filterFiles(files[i])) {
        $this.cache.files.push(files[i]);
      }
    }
    $this.renderFiles();
    $this.setFieldsMandatory();
  }
  filterFiles(file) {
    const maxFileSize = 10 * 1024 * 1024;
    return file.size < maxFileSize;
  }
  dragAndDropPreventDefault(e) {
    e.stopPropagation();
    e.preventDefault();
  }
  setFieldsMandatory() {
    const fields = this.root.find('.js-tp-add-equipment__toggle-mandatory');
    const isFileUploaded = this.cache.files.length ? 1 : 0;
    fields.each(function () {
      if (isFileUploaded) {
        $(this).removeAttr('required');
        $(this).closest('.js-tp-add-equipment__form-element').find('.error-msg').removeClass('error-msg--active');
      } else {
        $(this).attr('required', true);
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
