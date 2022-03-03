import $ from 'jquery';
import auth from '../../../scripts/utils/auth';
import {ajaxWrapper} from '../../../scripts/utils/ajax';
import {ajaxMethods} from '../../../scripts/utils/constants';
import {logger} from '../../../scripts/utils/logger';
import {render} from '../../../scripts/utils/render';

function _processTrainingsData(data) {
  const processedDataArr = [];

  data.forEach((item) => {
    const {learningItemDetail, name, id} = item;
    const {itemGoals, audience, duration, maximumEnrollments} = learningItemDetail;

    processedDataArr.push({
      name,
      id,
      description: item.descriptions[0].body,
      itemGoals,
      audience,
      duration,
      maximumEnrollments
    });
  });

  return processedDataArr;
}

function _addErrorMsg(el, errorMsgSelector) {
  $(el)
    .closest('.js-aip__form-element')
    .addClass('tp-aip__form-element--error')
    .find(errorMsgSelector)
    .addClass('error-msg--active');
}

function _removeAllErrorMessages($form) {
  $form.find('.error-msg--active').removeClass('error-msg--active');
  $form.find('.tp-aip__form-element--error').removeClass('tp-aip__form-element--error');
}

function _handleFormSubmit(formEl) {
  const $form = $(formEl);
  const $this = this;
  let isFormValid = true;
  const $requiredFormElements = $form.find('input[required], :checkbox');

  this.removeAllErrorMessages($form);

  $requiredFormElements.each((idx, el) => {
    const $el = $(el);

    if ((['text', 'number', 'date'].includes($el.attr('type')) && !$.trim($el.val())) || $el.attr('type') === 'checkbox' && !$el.is(':checked')) {
      isFormValid = false;
      this.addErrorMsg(el, '.js-aip__error-msg-required');
    }
  });

  if (isFormValid) {
    const formData = new FormData(formEl);
    $this.cache.$spinner.removeClass('d-none');

    ajaxWrapper
      .getXhrObj({
        url: this.cache.submitApi,
        method: ajaxMethods.POST,
        cache: true,
        processData: false,
        contentType: false,
        data: formData,
        showLoader: true
      }).done(() => {
        $this.cache.$spinner.removeClass('d-none');
      }).fail(() => {
        $this.cache.$spinner.addClass('d-none');
      });
  }
}

/**
 * Fetch and process the Trainings data
 */
function _getTrainingsData() {
  const $this = this;
  auth.getToken(({ data: authData }) => {
    ajaxWrapper
      .getXhrObj({
        url: this.cache.trainingsApi,
        method: ajaxMethods.GET,
        cache: true,
        dataType: 'json',
        contentType: 'application/json',
        beforeSend(jqXHR) {
          jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
          jqXHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        },
        showLoader: true
      }).done(res => {
        $this.cache.trainingsData = _processTrainingsData(res.data);
        $this.renderTrainings();
        $this.cache.$contentWrapper.removeClass('d-none');
        $this.cache.$spinner.addClass('d-none');
      }).fail((e) => {
        logger.error(e);
      });
  });
}

function _renderTrainings() {
  const $this = this;
  const {trainingsData} = $this.cache;

  render.fn({
    template: 'plantmasterTrainingsTab',
    target: '.js-aip-trainings__accordion',
    data: { i18nKeys: $this.cache.i18nKeys, trainingsDataArr: trainingsData}
  });
}

class PlantMasterTrainings {
  constructor({ el }) {
    this.root = $(el);
  }

  cache = {};

  initCache() {
    this.cache.configJson = this.root.find('.js-aip-trainings__config').text();
    try {
      this.cache.i18nKeys = JSON.parse(this.cache.configJson);
    } catch (e) {
      this.cache.i18nKeys = {};
      logger.error(e);
    }
    this.cache.trainingsApi = this.root.data('trainings-api');
    this.cache.submitApi = this.root.data('submit-api');
    this.cache.$contentWrapper = this.root.find('.js-aip-trainings__wrapper');
    this.cache.$spinner = this.root.find('.js-tp-spinner');
    this.cache.$formSpinner = this.root.find('.js-aip-trainings__form-spinner');
    this.cache.$formWrapper = this.root.find('.js-aip-trainings__form-content');
    this.cache.$tabPaneAvailableTrainings = this.root.find('.js-aip-trainings__accordion');
  }

  bindEvents() {
    this.root.on('submit', '.js-aip-trainings__form', e => {
      e.preventDefault();
      this.handleFormSubmit(e.target);
    });
  }

  renderTrainings() {
    return _renderTrainings.apply(this, arguments);
  }

  getTrainingsData() {
    return _getTrainingsData.apply(this, arguments);
  }

  handleFormSubmit() {
    return _handleFormSubmit.apply(this, arguments);
  }

  addErrorMsg() {
    return _addErrorMsg.apply(this, arguments);
  }

  removeAllErrorMessages() {
    return _removeAllErrorMessages.apply(this, arguments);
  }

  init() {
    this.initCache();
    this.getTrainingsData();
    this.bindEvents();
  }
}

export default PlantMasterTrainings;
