import $ from 'jquery';
import auth from '../../../scripts/utils/auth';
import moment from 'moment';
import {ajaxWrapper} from '../../../scripts/utils/ajax';
import {REG_NUM, ajaxMethods} from '../../../scripts/utils/constants';
import {logger} from '../../../scripts/utils/logger';
import {render} from '../../../scripts/utils/render';
import {sanitize} from '../../../scripts/common/common';
import {_trackAccordionClick, _trackFormError, _trackFormComplete, _trackFormStart} from './PlantMasterTrainings.analytics';

function _processTrainingsData(data,pingUserGroup) {
  data = data ? data : [];
  const processedDataArr = [];
  const trainingUserGroup = pingUserGroup ? pingUserGroup : [];

  data.forEach((item) => {
    if(trainingUserGroup.length > 0){
      if(trainingUserGroup.includes(item.learningItemDetail.itemId)){
        const {learningItemDetail, name} = item;
        const {itemId} = learningItemDetail;
        const {itemGoals, audience, duration, maximumEnrollments, comments} = learningItemDetail;

        processedDataArr.push({
          name,
          id: itemId,
          description: item.descriptions[0].body,
          itemGoals,
          audience,
          duration,
          comments,
          maximumEnrollments
        });
      }
    }
  });
  return processedDataArr;
}

function _processLearningHistoryData(data) {
  data = data ? data : [];

  const learningHistoryObj = {
    diploma: [],
    accredited: [],
    authenticated: []
  };

  data.forEach((learningItem) => {
    const { labTags } = learningItem;
    const { completionDateTime } = learningItem;
    learningItem['completionDateTime'] = moment(completionDateTime).format('YYYY-MM-DD');

    if (labTags) {
      labTags.forEach(tag => {
        switch (tag) {
          case 'Training':
            learningHistoryObj['diploma'].push(learningItem);
            break;
          case 'L1 Certification':
            learningHistoryObj['accredited'].push(learningItem);
            break;
          case 'L2 Certification':
            learningHistoryObj['authenticated'].push(learningItem);
            break;
          default:
            break;
        }
      });
    }
  });

  return learningHistoryObj;
}

function _addErrorMsg(el, errorMsgSelector) {
  const $el = $(el);
  const $this = this;
  const formErrorMessage = $el.closest('.js-aip__form-element').find(errorMsgSelector).text();
  const formErrorField = $el.attr('id').split('-')[0];

  $el
    .closest('.js-aip__form-element')
    .addClass('tp-aip__form-element--error')
    .find(errorMsgSelector)
    .addClass('error-msg--active');

  $this.cache.formError.push({
    formErrorMessage,
    formErrorField
  });
}

function _removeAllErrorMessages($form) {
  const $this = this;
  $form.find('.error-msg--active').removeClass('error-msg--active');
  $form.find('.tp-aip__form-element--error').removeClass('tp-aip__form-element--error');
  $this.cache.formError = [];
}

function _handleFormSubmit(formEl) {
  const $form = $(formEl);
  const $this = this;
  let isFormValid = true;
  const $requiredFormElements = $form.find('input[required], :checkbox');

  this.removeAllErrorMessages($form);

  $requiredFormElements.each((idx, el) => {
    const $el = $(el);

    if (['text', 'number'].includes($el.attr('type')) && !$.trim($el.val())) {
      isFormValid = false;
      $this.addErrorMsg(el, '.js-aip__error-msg-required');
    }

    const $numberField = this.root.find(':input[type="number"]:visible');
    $numberField.each((idx, ele) => {
      const numberFieldVal = $(ele).val();
      if (numberFieldVal && (!this.isNumeric(numberFieldVal) || (numberFieldVal < 1))) {
        isFormValid = false;
        this.addErrorMsg(ele, '.js-aip__error-msg-invalid-number');
      }
    });

    if ($el.hasClass('js-aip-trainings__date-input') && $.trim($el.val())) {
      const date = moment($el.val(), 'YYYY-MM-DD', true);
      if (!date.isValid()) {
        isFormValid = false;
        $this.addErrorMsg(el, '.js-aip__error-msg-format');
      }
    }

    if ($el.attr('type') === 'checkbox' && !$el.is(':checked')) {
      isFormValid = false;
      $this.addErrorMsg(el, '.js-aip__error-msg-required');
    }
  });

  if (isFormValid) {
    const formData = new FormData(formEl);
    const $formWrapper = $form.parent();
    const $trainingBody = $formWrapper.prev();
    const $confirmationTxt = $formWrapper.next();
    const { username, userEmailAddress } = this.cache;
    const $consentInput = $formWrapper.find('.js-aip-trainings__consent-cb input');
    const processedFormData = new FormData();

    $this.cache.$contentWrapper.addClass('d-none');
    $this.cache.$spinner.removeClass('d-none');

    // due to multiple forms on page, input fields are suffixed with index that is removed before submitting
    for (const [key, value] of formData) {
      const updatedKey = key.split('-')[0];
      processedFormData.set(updatedKey, sanitize(value));
    }

    processedFormData.set('name', username);
    processedFormData.set('emailAddress', userEmailAddress);
    processedFormData.set('consent', $consentInput.is(':checked'));

    ajaxWrapper
      .getXhrObj({
        url: this.cache.submitApi,
        method: ajaxMethods.POST,
        cache: true,
        processData: false,
        contentType: false,
        data: processedFormData,
        showLoader: true
      }).done(() => {
        const trainingName = $formWrapper.parents('.tp-aip__acc-item').find('.btn-link').text().trim();
        $this.cache.$spinner.addClass('d-none');
        $formWrapper.addClass('d-none');
        $trainingBody.addClass('d-none');
        $this.cache.$contentWrapper.removeClass('d-none');
        $confirmationTxt.removeClass('d-none');
        $this.trackFormComplete(trainingName, processedFormData);
      }).fail(() => {
        $this.cache.$spinner.addClass('d-none');
        $this.cache.$contentWrapper.removeClass('d-none');
      });
  } else {
    const $formWrapper = $form.parent();
    const { formError } = $this.cache;
    const trainingName = $formWrapper.parents('.tp-aip__acc-item').find('.btn-link').text().trim();
    $this.trackFormError(trainingName, formError);
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
        $this.cache.trainingsData = $this.processTrainingsData(res.data,this.cache.trainingUserGroup);
        $this.renderTrainings();
        $this.cache.$contentWrapper.removeClass('d-none');
        $this.cache.$spinner.addClass('d-none');
      }).fail((e) => {
        logger.error(e);
      });
  });
}

/**
 * Fetch ping user data
 */

function _getUserGroup() {
  ajaxWrapper.getXhrObj({
    url: this.cache.usergroupurl,
    method: ajaxMethods.GET,
    cache: true,
    async: false,
    dataType: 'json',
    contentType: 'application/json',
    beforeSend(jqXHR) {
      jqXHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    },
    showLoader: true
  }).done(res => {
    const userGroup = res.groups;
    const learningPingGroup = 'cuhu_tppm_access';

    if (userGroup) {
      userGroup.forEach(group => {
        if (group.trainingId === '' || group.trainingId !== undefined) {
          this.cache.trainingUserGroup.push(group.trainingId);
        }

        // if user has access to Learning History tab, get data
        if (group.groupName === learningPingGroup) {
          this.getLearningHistoryData();
        }
      });
    }

    this.getTrainingsData();
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

/**
 * Fetch the Learning History data
 */
function _getLearningHistoryData() {
  const $this = this;
  auth.getToken(({ data: authData }) => {
    ajaxWrapper
      .getXhrObj({
        url: $this.cache.learningHistoryApi,
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
        $this.cache.learningHistoryData = $this.processLearningHistoryData(res.data);
        $this.renderLearningHistory();
      }).fail((e) => {
        logger.error(e);
      });
  });
}

function _renderLearningHistory() {
  const $this = this;
  const { learningHistoryData } = $this.cache;

  render.fn({
    template: 'plantmasterTrainingsLearningTab',
    target: '.js-aip-trainings__accordion-learning',
    data: { i18nKeys: $this.cache.i18nKeys, learningHistoryData: learningHistoryData}
  });

  $('#nav-learning-tab').removeClass('d-none');
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
    this.cache.learningHistoryApi = this.root.data('learning-history-api');
    this.cache.submitApi = this.root.data('submit-api');
    this.cache.username = decodeURI(this.root.data('username'));
    this.cache.userEmailAddress = this.root.data('email');
    this.cache.$contentWrapper = this.root.find('.js-aip-trainings__wrapper');
    this.cache.$spinner = this.root.find('.js-tp-spinner');
    this.cache.$formSpinner = this.root.find('.js-aip-trainings__form-spinner');
    this.cache.$formWrapper = this.root.find('.js-aip-trainings__form-content');
    this.cache.$tabPaneAvailableTrainings = this.root.find('.js-aip-trainings__accordion');
    this.cache.usergroupurl = this.root.data('group-servlet-url');
    this.cache.trainingUserGroup = [];
    this.cache.formError = [];
  }

  bindEvents() {
    this.root.on('submit', '.js-aip-trainings__form', e => {
      e.preventDefault();
      this.handleFormSubmit(e.target);
    });

    // track Accordion click analytics
    this.root.on('click', '.js-aip-trainings__accordion .btn-link', e => {
      const $btn = $(e.currentTarget);
      const text = $btn.find('span').text();
      const $this = this;
      const linkSection = $btn.parents('#tp-aip-trainings__accordion').length ? 'Available Automation Engineering Trainings' : 'Learning History';

      if ($btn.attr('aria-expanded') === 'true') {
        $this.trackAccordionClick(text, false, linkSection);
      } else {
        $this.trackAccordionClick(text, true, linkSection);
      }
    });

    // track Form Start analytics
    this.root.on('input change', '.tpatom-input-box__input, .tpatom-textarea-box__input, .tpatom-checkbox__input', e => {
      const $this = this;
      const $formWrapper = $(e.currentTarget).parents('.js-aip-trainings__form');
      const trainingName = $formWrapper.parents('.tp-aip__acc-item').find('.btn-link').text().trim();

      if (!$formWrapper.data('form-touched')) {
        $this.trackFormStart(trainingName);
        $formWrapper.attr('data-form-touched', true);
      }
    });
  }

  isNumeric(number) {
    return REG_NUM.test(number);
  }

  processTrainingsData() {
    return _processTrainingsData.apply(this, arguments);
  }

  processLearningHistoryData() {
    return _processLearningHistoryData.apply(this, arguments);
  }

  renderTrainings() {
    return _renderTrainings.apply(this, arguments);
  }

  getTrainingsData() {
    return _getTrainingsData.apply(this, arguments);
  }

  getUserGroup() {
    return _getUserGroup.apply(this, arguments);
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

  getLearningHistoryData() {
    return _getLearningHistoryData.apply(this, arguments);
  }

  renderLearningHistory() {
    return _renderLearningHistory.apply(this, arguments);
  }

  trackAccordionClick() {
    return _trackAccordionClick.apply(this, arguments);
  }

  trackFormError() {
    return _trackFormError.apply(this, arguments);
  }

  trackFormComplete() {
    return _trackFormComplete.apply(this, arguments);
  }

  trackFormStart() {
    return _trackFormStart.apply(this, arguments);
  }

  init() {
    this.initCache();
    this.getUserGroup();
    this.bindEvents();
  }
}

export default PlantMasterTrainings;