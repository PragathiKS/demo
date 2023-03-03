import $ from 'jquery';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { ajaxMethods } from '../../../scripts/utils/constants';
import { render } from '../../../scripts/utils/render';
import { logger } from '../../../scripts/utils/logger';
import { REG_NUM } from '../../../scripts/utils/constants';
const REG_EMAIL = /^(?!\.)((?!.*\.{2})[a-zA-Z0-9\u0080-\u00FF\u0100-\u017F\u0180-\u024F\u0250-\u02AF\u0300-\u036F\u0370-\u03FF\u0400-\u04FF\u0500-\u052F\u0530-\u058F\u0590-\u05FF\u0600-\u06FF\u0700-\u074F\u0750-\u077F\u0780-\u07BF\u07C0-\u07FF\u0900-\u097F\u0980-\u09FF\u0A00-\u0A7F\u0A80-\u0AFF\u0B00-\u0B7F\u0B80-\u0BFF\u0C00-\u0C7F\u0C80-\u0CFF\u0D00-\u0D7F\u0D80-\u0DFF\u0E00-\u0E7F\u0E80-\u0EFF\u0F00-\u0FFF\u1000-\u109F\u10A0-\u10FF\u1100-\u11FF\u1200-\u137F\u1380-\u139F\u13A0-\u13FF\u1400-\u167F\u1680-\u169F\u16A0-\u16FF\u1700-\u171F\u1720-\u173F\u1740-\u175F\u1760-\u177F\u1780-\u17FF\u1800-\u18AF\u1900-\u194F\u1950-\u197F\u1980-\u19DF\u19E0-\u19FF\u1A00-\u1A1F\u1B00-\u1B7F\u1D00-\u1D7F\u1D80-\u1DBF\u1DC0-\u1DFF\u1E00-\u1EFF\u1F00-\u1FFF\u20D0-\u20FF\u2100-\u214F\u2C00-\u2C5F\u2C60-\u2C7F\u2C80-\u2CFF\u2D00-\u2D2F\u2D30-\u2D7F\u2D80-\u2DDF\u2F00-\u2FDF\u2FF0-\u2FFF\u3040-\u309F\u30A0-\u30FF\u3100-\u312F\u3130-\u318F\u3190-\u319F\u31C0-\u31EF\u31F0-\u31FF\u3200-\u32FF\u3300-\u33FF\u3400-\u4DBF\u4DC0-\u4DFF\u4E00-\u9FFF\uA000-\uA48F\uA490-\uA4CF\uA700-\uA71F\uA800-\uA82F\uA840-\uA87F\uAC00-\uD7AF\uF900-\uFAFF.!#$%&'*+-/=?^_`{|}~\-\d]+)@(?!\.)([a-zA-Z0-9\u0080-\u00FF\u0100-\u017F\u0180-\u024F\u0250-\u02AF\u0300-\u036F\u0370-\u03FF\u0400-\u04FF\u0500-\u052F\u0530-\u058F\u0590-\u05FF\u0600-\u06FF\u0700-\u074F\u0750-\u077F\u0780-\u07BF\u07C0-\u07FF\u0900-\u097F\u0980-\u09FF\u0A00-\u0A7F\u0A80-\u0AFF\u0B00-\u0B7F\u0B80-\u0BFF\u0C00-\u0C7F\u0C80-\u0CFF\u0D00-\u0D7F\u0D80-\u0DFF\u0E00-\u0E7F\u0E80-\u0EFF\u0F00-\u0FFF\u1000-\u109F\u10A0-\u10FF\u1100-\u11FF\u1200-\u137F\u1380-\u139F\u13A0-\u13FF\u1400-\u167F\u1680-\u169F\u16A0-\u16FF\u1700-\u171F\u1720-\u173F\u1740-\u175F\u1760-\u177F\u1780-\u17FF\u1800-\u18AF\u1900-\u194F\u1950-\u197F\u1980-\u19DF\u19E0-\u19FF\u1A00-\u1A1F\u1B00-\u1B7F\u1D00-\u1D7F\u1D80-\u1DBF\u1DC0-\u1DFF\u1E00-\u1EFF\u1F00-\u1FFF\u20D0-\u20FF\u2100-\u214F\u2C00-\u2C5F\u2C60-\u2C7F\u2C80-\u2CFF\u2D00-\u2D2F\u2D30-\u2D7F\u2D80-\u2DDF\u2F00-\u2FDF\u2FF0-\u2FFF\u3040-\u309F\u30A0-\u30FF\u3100-\u312F\u3130-\u318F\u3190-\u319F\u31C0-\u31EF\u31F0-\u31FF\u3200-\u32FF\u3300-\u33FF\u3400-\u4DBF\u4DC0-\u4DFF\u4E00-\u9FFF\uA000-\uA48F\uA490-\uA4CF\uA700-\uA71F\uA800-\uA82F\uA840-\uA87F\uAC00-\uD7AF\uF900-\uFAFF.\d]+)((\.([a-zA-Z\u0080-\u00FF\u0100-\u017F\u0180-\u024F\u0250-\u02AF\u0300-\u036F\u0370-\u03FF\u0400-\u04FF\u0500-\u052F\u0530-\u058F\u0590-\u05FF\u0600-\u06FF\u0700-\u074F\u0750-\u077F\u0780-\u07BF\u07C0-\u07FF\u0900-\u097F\u0980-\u09FF\u0A00-\u0A7F\u0A80-\u0AFF\u0B00-\u0B7F\u0B80-\u0BFF\u0C00-\u0C7F\u0C80-\u0CFF\u0D00-\u0D7F\u0D80-\u0DFF\u0E00-\u0E7F\u0E80-\u0EFF\u0F00-\u0FFF\u1000-\u109F\u10A0-\u10FF\u1100-\u11FF\u1200-\u137F\u1380-\u139F\u13A0-\u13FF\u1400-\u167F\u1680-\u169F\u16A0-\u16FF\u1700-\u171F\u1720-\u173F\u1740-\u175F\u1760-\u177F\u1780-\u17FF\u1800-\u18AF\u1900-\u194F\u1950-\u197F\u1980-\u19DF\u19E0-\u19FF\u1A00-\u1A1F\u1B00-\u1B7F\u1D00-\u1D7F\u1D80-\u1DBF\u1DC0-\u1DFF\u1E00-\u1EFF\u1F00-\u1FFF\u20D0-\u20FF\u2100-\u214F\u2C00-\u2C5F\u2C60-\u2C7F\u2C80-\u2CFF\u2D00-\u2D2F\u2D30-\u2D7F\u2D80-\u2DDF\u2F00-\u2FDF\u2FF0-\u2FFF\u3040-\u309F\u30A0-\u30FF\u3100-\u312F\u3130-\u318F\u3190-\u319F\u31C0-\u31EF\u31F0-\u31FF\u3200-\u32FF\u3300-\u33FF\u3400-\u4DBF\u4DC0-\u4DFF\u4E00-\u9FFF\uA000-\uA48F\uA490-\uA4CF\uA700-\uA71F\uA800-\uA82F\uA840-\uA87F\uAC00-\uD7AF\uF900-\uFAFF]){2,63})+)$/i;

function isValidPhoneNumber(phone) {
  return REG_NUM.test(phone);
}

function isEmailValid(email) {
  return REG_EMAIL.test(email);
}
/**
 * Render form
 */
function _renderLayout() {
  const $this = this;
  render.fn({
    template: 'supportRequestForm',
    target: '.js-tp-support-request__content-wrapper',
    data: { subTitle: $this.cache.subTitle, detailsSubtitle: $this.cache.detailsSubtitle, i18nKeys: $this.cache.i18nKeys }
  }, () => {
    $this.cache.$contentWrapper.removeClass('d-none');
    $this.cache.$spinner.addClass('d-none');
  });
}

/**
 * Render files after upload
 */
function _renderFiles() {
  const $this = this;
  const $fileExtensionError = $this.root.find('.js-tp-support-request__file-error');
  const obj = $this.cache.files.map(obj => ({ name: obj.name, size: `${(obj.size / (1024 * 1024)).toFixed(2)  } MB`, removeFileLabel: $this.cache.i18nKeys.removeFileLabel, isError: true }));
  render.fn({
    template: 'supportRequestFiles',
    target: '.js-tp-support-request__drag-and-drop-files-container',
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
    template: 'supportRequestSubmit',
    target: '.js-tp-support-request__content-wrapper',
    data: { i18nKeys: $this.cache.i18nKeys }
  }, () => {
    $this.cache.$contentWrapper.removeClass('d-none');
    $this.cache.$spinner.addClass('d-none');
  });
}

class SupportRequest {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.files = [];
    this.cache.firstInteract = false;
    this.cache.$contentWrapper = this.root.find('.js-tp-support-request__content-wrapper');
    this.cache.$spinner = this.root.find('.js-tp-spinner');
    this.cache.configJson = this.root.find('.js-tp-support-request__config').text();
    try {
      this.cache.i18nKeys = JSON.parse(this.cache.configJson);
    } catch (e) {
      this.cache.i18nKeys = {};
      logger.error(e);
    }
    this.cache.submitApi = this.root.data('submit-api');
    this.cache.subTitle = this.root.data('subtitle');
    this.cache.detailsSubtitle = this.root.data('details-subtitle');
  }
  bindEvents() {
    const $this = this;
    $(window.document).on('dragenter dragover drop', e => {
      $this.dragAndDropPreventDefault(e);
    });

    $this.root.on('dragenter dragleave drop', '.js-tp-support-request__drag-and-drop', e => {
      $this.dragAndDropPreventDefault(e);
      if (e.type === 'drop') {
        $this.dropFiles(e, $this, true);
      }
    });

    $this.root.on('click', '.js-tp-support-request__drag-and-drop-button', () => {
      $this.addInputTypeFile();
    });

    $this.root.on('click', '.js-tp-support-request__send-another-support-request', () => {
      window.location.assign('dashboard.html').reload();
    });

    $this.root.on('click', '.js-tp-support-request__drag-and-drop-file-remove-container', e => {
      $this.removeFile(e);
    });

    $this.root.on('click', '.js-tp-support-request__submit', e => {
      e.preventDefault();
      $this.removeAllErrorMessages();
      const formErrors = [];
      const requiredFormElements = $this.root.find('.js-tp-support-request__form-element [required]');
      const names = {};
      requiredFormElements.each(function () {
        const name = $(this).attr('name');
        const val = $(this).attr('type') === 'radio' ? $(`input[name="${name}"]:checked`).length : $(this).val();
        if(names[name] && !val) {
          return;
        }
        names[name] = 1;
        if (!val) {
          $this.addErrorMsg(this);
          formErrors.push({
            formErrorMessage: $(this).closest('.js-tp-support-request__form-element').find('.error-msg').text().trim(),
            formErrorField: $(this).attr('type') !== 'radio' ? $(this).closest('.js-tp-support-request__form-element').find('.tp-support-request__field-label').text().trim() || 'query' : 'purposeOfContact'
          });
        }
      });

      const $telephone = this.root.find('#ownPhoneNumber');
      const phoneNumber = $telephone.val();
      if (phoneNumber && !isValidPhoneNumber(phoneNumber)) {
        this.addErrorMsg($telephone);
        formErrors.push({
          formErrorMessage: $telephone.closest('.js-tp-support-request__form-element').find('.error-msg').text().trim(),
          formErrorField: $telephone.closest('.js-tp-support-request__form-element').find('.tp-support-request__field-label').text().trim()
        });
      }

      [$('#aribaAccountAdminEmail'), $('#tpContactEmail')].forEach(function(el) {
        if($(el).val() && !isEmailValid(el.val())) {
          $this.addErrorMsg(el);
          formErrors.push({
            formErrorMessage: $(el).closest('.js-tp-support-request__form-element').find('.error-msg').text().trim(),
            formErrorField: $(el).closest('.js-tp-support-request__form-element').find('.tp-support-request__field-label').text().trim()
          });
        }
      });
      
      if (!formErrors.length) {
        $this.cache.$contentWrapper.addClass('d-none');
        $this.cache.$spinner.removeClass('d-none');
        $this.submitForm(e);
      }
    });

    $this.root.on('blur', 'textarea, input', (e) => {
      const sanitized = e.target.value.replace(/<\/?script>|[<>]/gm, '');
      $(e.target).val(sanitized);
    });

    $this.root.on('focus', 'textarea, input, select', () => {
      if (!$this.cache.firstInteract) {
        $this.cache.firstInteract = true;
      }
    });

    const decodeUserName = decodeURI(this.cache.i18nKeys.userName);
    this.root.find('#name').val(decodeUserName);
  }
  submitForm(e) {
    const $this = this;
    const formData = new FormData(e.currentTarget.form);

    if ($this.cache.files && $this.cache.files.length > 0) {
      for (let i = 0; i < $this.cache.files.length; i++) {
        formData.append('files', $this.cache.files[i]);
      }
    }

    ajaxWrapper
      .getXhrObj({
        url: $this.cache.submitApi,
        method: ajaxMethods.POST,
        cache: true,
        processData: false,
        contentType: false,
        data: formData,
        showLoader: true
      }).done(() => {
        $this.renderSubmit();
      }).fail(() => {
        $this.cache.$contentWrapper.removeClass('d-none');
        $this.cache.$spinner.addClass('d-none');
      });
  }
  addErrorMsg(el) {
    const formElement = $(el).closest('.js-tp-support-request__form-element');
    formElement.addClass('tp-support-request__form-element--error');
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
    $(el).closest('.js-tp-support-request__form-element').removeClass('tp-support-request__form-element--error');
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
    const $fileExtensionError = $this.root.find('.js-tp-support-request__file-error');

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
    const fields = $this.root.find('.js-tp-support-request__toggle-mandatory');
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
  renderLayout() {
    return _renderLayout.apply(this, arguments);
  }
  renderFiles() {
    return _renderFiles.apply(this, arguments);
  }
  renderSubmit() {
    return _renderSubmit.apply(this, arguments);
  }
  init() {
    this.initCache();
    this.renderLayout();
    this.bindEvents();
  }
}

export default SupportRequest;



