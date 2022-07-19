import $ from 'jquery';
import auth from '../../../scripts/utils/auth';
import moment from 'moment';
import {ajaxWrapper} from '../../../scripts/utils/ajax';
import {REG_NUM, ajaxMethods} from '../../../scripts/utils/constants';
import {logger} from '../../../scripts/utils/logger';
import {render} from '../../../scripts/utils/render';
import {sanitize} from '../../../scripts/common/common';

function _processTrainingsData(data,pingUserGroup) {
  data = data ? data : [];
  const processedDataArr = [];
  const trainingUserGroup = pingUserGroup ? pingUserGroup : [];

  data.forEach((item) => {
    if(trainingUserGroup.length > 0){
      if(trainingUserGroup.includes(item.extRef.material.number)){
        const {learningItemDetail, name, extRef} = item;
        const {material} = extRef;
        const {number} = material;
        const {itemGoals, audience, duration, maximumEnrollments, comments} = learningItemDetail;

        processedDataArr.push({
          name,
          id: number,
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
        $this.cache.$spinner.addClass('d-none');
        $formWrapper.addClass('d-none');
        $trainingBody.addClass('d-none');
        $this.cache.$contentWrapper.removeClass('d-none');
        $confirmationTxt.removeClass('d-none');
      }).fail(() => {
        $this.cache.$spinner.addClass('d-none');
        $this.cache.$contentWrapper.removeClass('d-none');
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
  }

  bindEvents() {
    this.root.on('submit', '.js-aip-trainings__form', e => {
      e.preventDefault();
      this.handleFormSubmit(e.target);
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

  init() {
    this.initCache();
    this.getUserGroup();
    this.bindEvents();
  }
}

export default PlantMasterTrainings;
