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
        $this.cache.trainingsData = _processTrainingsData(res.data[0]);
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
    this.cache.$contentWrapper = this.root.find('.js-aip-trainings__wrapper');
    this.cache.$spinner = this.root.find('.js-tp-spinner');
    this.cache.$tabPaneAvailableTrainings = this.root.find('.js-aip-trainings__accordion');
  }

  renderTrainings() {
    return _renderTrainings.apply(this, arguments);
  }

  getTrainingsData() {
    return _getTrainingsData.apply(this, arguments);
  }

  init() {
    this.initCache();
    this.getTrainingsData();
  }
}

export default PlantMasterTrainings;
