import $ from 'jquery';
import { logger } from '../../../scripts/utils/logger';
import { render } from '../../../scripts/utils/render';
import { REG_EMAIL, REG_NUM } from '../../../scripts/utils/constants';

function _renderCotsSupportForm() {
  render.fn(
    {
      template: 'cotsSupportForm',
      target: this.cache.$contentWrapper,
      data: {
        i18nKeys: this.cache.i18nKeys,
        // TODO remove hardcoded dictionary when api will be available
        affectedSystemsDictionary: [
          { key: 'production-control', desc: 'Production Control' },
          { key: 'mes', desc: 'MES' }
        ]
      }
    },
    this.showContent
  );
}

function _renderFiles() {
  const $this = this;
  const obj = $this.cache.files.map(obj => ({ name: obj.name, size: `${(obj.size / (1024 * 1024)).toFixed(2)  } MB`, removeFileLabel: $this.cache.i18nKeys.dragAndDropRemoveFileLabel }));
  render.fn({
    template: 'cotsSupportFiles',
    target: '.js-tp-cots-support__drag-and-drop-files-container',
    data: obj
  });
}


function _renderSuccessMessage() {
  render.fn(
    {
      template: 'cotsSupportSuccessMessage',
      target: this.cache.$contentWrapper,
      data: { i18nKeys: this.cache.i18nKeys }
    },
    this.showContent
  );
}

class CotsSupport {
  constructor({ el }) {
    this.root = $(el);
  }

  cache = {};
  // TODO remove hardcoded dictionary when api will be available
  productInvolved = {
    productionControlDictionary: [
      { key: 'TIA-portal', desc: 'TIA Portal' },
      { key: 'control-logix', desc: 'Control Logix' },
      { key: 'orion', desc: 'Orion' },
      { key: 'archestra-system-platform', desc: 'Archestra System Platform' }
    ],
    MESDictionary: [
      { key: 'production-integrator', desc: 'Production Integrator' },
      { key: 'Aveva-MES-SI-Kit', desc: 'Aveva MES SI-Kit' }
    ]
  }

  initCache() {
    this.cache.$contentWrapper = this.root.find('.js-tp-cots-support__content-wrapper');
    this.cache.$spinner = this.root.find('.js-tp-spinner');
    this.cache.files = [];
    try {
      const configJson = this.root.find('.js-tp-cots-support__config').text();
      this.cache.i18nKeys = JSON.parse(configJson);
    } catch (e) {
      this.cache.i18nKeys = {};
      logger.error(e);
    }
  }

  isEmailValid(email) {
    return REG_EMAIL.test(email);
  }

  isPhoneValid(phone) {
    return REG_NUM.test(phone);
  }

  showContent = () => {
    this.cache.$contentWrapper.removeClass('d-none');
    this.cache.$spinner.addClass('d-none');
  };

  showSpinner = () => {
    this.cache.$contentWrapper.addClass('d-none');
    this.cache.$spinner.removeClass('d-none');
  };

  addErrorMsg(el, errorMsgSelector) {
    $(el)
      .closest('.js-tp-cots-support__form-element')
      .addClass('tp-cots-support__form-element--error')
      .find(errorMsgSelector)
      .addClass('error-msg--active');
  }

  removeAllErrorMessages() {
    this.root.find('.error-msg--active').removeClass('error-msg--active');
    this.root
      .find('.tp-cots-support__form-element--error')
      .removeClass('tp-cots-support__form-element--error');
  }

  removeFile(e) {
    const index = $(e.currentTarget).data('index');
    this.cache.files.splice(index, 1);
    this.renderFiles();
  }

  submitForm = (e, onSuccess) => {
    e.preventDefault();
    let isFormValid = true;
    this.removeAllErrorMessages();

    const $requiredFormElements = this.root.find(':text[required]:visible, textarea[required]:visible, select[required]:visible');
    $requiredFormElements.each((idx, el) => {
      if (!$.trim($(el).val())) {
        isFormValid = false;
        this.addErrorMsg(el, '.js-tp-cots-support__error-msg-required');
      }
    });

    const $emailAddress = this.root.find('#emailAddress');
    const emailAddress = $emailAddress.val();
    if (emailAddress && !this.isEmailValid(emailAddress)) {
      isFormValid = false;
      this.addErrorMsg($emailAddress, '.js-tp-cots-support__error-msg-invalid-email');
    }

    const $telephone = this.root.find('#telephone');
    const phoneNumber = $telephone.val();
    if (phoneNumber && !this.isPhoneValid(phoneNumber)) {
      isFormValid = false;
      this.addErrorMsg($telephone, '.js-tp-cots-support__error-msg-invalid-phone');
    }

    if (isFormValid) {
      const formData = new FormData(e.currentTarget.form);
      if (this.cache.files && this.cache.files.length > 0) {
        for (let i = 0; i < this.cache.files.length; i++) {
          formData.append('files', this.cache.files[i]);
        }
      }
      this.showSpinner();
      // TODO create submit request when api will be available
      // eslint-disable-next-line
      console.log(formData);
      onSuccess();
    }
  };

  submitTypeOfQueryForm = (e) => {
    this.submitForm(e, () => this.renderSuccessMessage());
  };


  handleAffectedSystemChange = () => {
    const affectedSystems = this.root.find('[name=affectedSystems]').val();
    const productionInv = this.productInvolved;
    if(affectedSystems === 'production-control'){
      const productionControl = productionInv.productionControlDictionary;
      $('#productInvolved option[value !=""]').remove();
      productionControl.forEach(product => {
        $('#productInvolved').append($('<option>').text(product['desc']).attr('value', product['key']));
      });
    }else if(affectedSystems === 'mes'){
      const mes = productionInv.MESDictionary;
      $('#productInvolved option[value !=""]').remove();
      mes.forEach(product => {
        $('#productInvolved').append($('<option>').text(product['desc']).attr('value', product['key']));
      });
    }

    this.setFieldsMandatory();
  };

  addInputTypeFile = () => {
    const $this = this;
    const input = document.createElement('input');
    input.style.cssText = 'display: none;';
    input.type = 'file';
    input.multiple = true;
    document.body.appendChild(input);
    input.addEventListener('change', (event) => $this.dropFiles(event, $this), false);
    $(input).trigger('click');
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
    const $this = this;
    const fields = $this.root.find('.js-tp-cots-support__toggle-mandatory');
    fields.each(function () {
      $(this).find('input, select, textarea').attr('required', true);
    });
  }

  bindEvents() {
    $(window.document).on('dragenter dragover drop', e => {
      this.dragAndDropPreventDefault(e);
    });

    this.root.on('dragenter dragleave drop', '.js-tp-cots-support__drag-and-drop', e => {
      this.dragAndDropPreventDefault(e);
      if (e.type === 'drop') {
        this.dropFiles(e, this, true);
      }
    });

    this.root.on('change', '[name=affectedSystems]', this.handleAffectedSystemChange);
    this.root.on('click', '.js-tp-cots-support__drag-and-drop-button', this.addInputTypeFile);
    this.root.on('click', '.js-tp-cots-support__submit-type-of-query', this.submitTypeOfQueryForm);
  }

  renderCotsSupportForm() {
    return _renderCotsSupportForm.apply(this, arguments);
  }

  renderFiles() {
    return _renderFiles.apply(this, arguments);
  }

  renderSuccessMessage() {
    return _renderSuccessMessage.apply(this, arguments);
  }

  init() {
    this.initCache();
    this.renderCotsSupportForm();
    this.bindEvents();
  }
}

export default CotsSupport;
