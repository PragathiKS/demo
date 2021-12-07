import $ from 'jquery';
import { logger } from '../../../scripts/utils/logger';
import { render } from '../../../scripts/utils/render';
import { REG_EMAIL, REG_NUM } from '../../../scripts/utils/constants';

function _renderTypeOfQueryForm() {
  render.fn(
    {
      template: 'cotsSupportTypeOfQueryForm',
      target: this.cache.$contentWrapper,
      data: {
        i18nKeys: this.cache.i18nKeys,
        // TODO remove hardcoded dictionary when api will be available
        urgencyDictionary: [
          { key: 'high', desc: 'High' },
          { key: 'medium', desc: 'Medium' },
          { key: 'low', desc: 'Low' }
        ]
      }
    },
    this.showContent
  );
}

function _renderConfirmationDetailsForm() {
  render.fn(
    {
      template: 'cotsSupportConfirmDetailsForm',
      target: this.cache.$contentWrapper,
      data: { i18nKeys: this.cache.i18nKeys }
    },
    this.showContent
  );
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

  initCache() {
    this.cache.$contentWrapper = this.root.find(
      '.js-tp-cots-support__content-wrapper'
    );
    this.cache.$spinner = this.root.find('.js-tp-spinner');
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

  submitForm = (e, onSuccess) => {
    e.preventDefault();
    let isFormValid = true;
    this.removeAllErrorMessages();

    const $requiredFormElements = this.root.find(
      ':text[required]:visible, textarea[required]:visible, select[required]:visible'
    );
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
      const { form } = e.currentTarget;
      const formData = Object.fromEntries(new FormData(form).entries());
      this.showSpinner();
      // TODO create submit request when api will be available
      // eslint-disable-next-line
      console.log(formData);
      onSuccess();
    }
  };

  submitTypeOfQueryForm = (e) => {
    this.submitForm(e, () => this.renderConfirmationDetailsForm());
  };

  submitConfirmationDetails = (e) => {
    this.submitForm(e, () => this.renderSuccessMessage());
  };

  handleTypeOfQueryChange = () => {
    const logQueryType = this.root.find('[name=logQueryType]:checked').val();

    this.root
      .find('[data-for-log-query=technicalIssues]')
      .toggleClass('d-none', logQueryType !== 'technicalIssues');

    this.root.find('[data-for-log-query=both]').removeClass('d-none');

    this.root.find('.js-tp-cots-support__submit-type-of-query').removeClass('d-none');
  };

  bindEvents() {
    this.root.on('change', '[name=logQueryType]', this.handleTypeOfQueryChange);
    this.root.on('click', '.js-tp-cots-support__submit-type-of-query', this.submitTypeOfQueryForm);
    this.root.on('click', '.js-tp-cots-support__submit-confirmation-details', this.submitConfirmationDetails);
  }

  renderTypeOfQueryForm() {
    return _renderTypeOfQueryForm.apply(this, arguments);
  }

  renderConfirmationDetailsForm() {
    return _renderConfirmationDetailsForm.apply(this, arguments);
  }

  renderSuccessMessage() {
    return _renderSuccessMessage.apply(this, arguments);
  }

  init() {
    this.initCache();
    this.renderTypeOfQueryForm();
    this.bindEvents();
  }
}

export default CotsSupport;
