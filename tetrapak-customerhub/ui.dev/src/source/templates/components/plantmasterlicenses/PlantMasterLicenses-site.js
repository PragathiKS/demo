/* eslint-disable */
import $ from 'jquery';
import auth from '../../../scripts/utils/auth';
import {ajaxWrapper} from '../../../scripts/utils/ajax';
import {ajaxMethods} from '../../../scripts/utils/constants';
import {logger} from '../../../scripts/utils/logger';
import {render} from '../../../scripts/utils/render';

function _processSiteLicensesData(data) {
  const processedDataArr = data.descriptions[0].body;
  return processedDataArr;
}


function _renderSiteLicensesData() {
  const $this = this;
  const {siteLicensesData} = $this.cache;

  render.fn({
    template: 'plantMasterLicenses-site',
    target: '.js-tp-aip-licenses__site-description',
    data: { i18nKeys: this.cache.i18nKeys.siteLicense, siteLicensesDataArr: siteLicensesData}
  });
}

  /**
 * Fetch and process the Site Licenses data
 */
function _getSiteLicensesData() {
  const $this = this;
  auth.getToken(({ data: authData }) => {
    ajaxWrapper
      .getXhrObj({
        url: this.cache.siteLicensesApi,
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
        $this.cache.siteLicensesData = _processSiteLicensesData(res.data[0]);
        $this.renderSiteLicensesData();
        $this.cache.$sitecontentWrapper.removeClass('d-none');
      }).fail((e) => {
        logger.error(e);
      });
  });
}

function _renderSuccessMessage() {
  render.fn(
    {
      template: 'plantMasterLicensesSuccessMessage',
      target: this.cache.$contentWrapper,
      data: { i18nKeys: this.cache.i18nKeys }
    },
    this.showContent
  );
}

class PlantMasterLicensesSite {
  constructor(el) {
    this.root = el;
  }

  cache = {};

  initCache() {
    this.cache.siteLicensesApi = this.root.prevObject.data('sitelicense-api');
    this.cache.$spinner = this.root.prevObject.find('.js-tp-spinner');
    this.cache.$sitecontentWrapper = this.root.find('.js-tp-aip-licenses__site-description');
    this.cache.$contentWrapper = this.root.prevObject.find('.js-aip-licenses__contentWrapper');
    this.cache.submitApi = this.root.prevObject.data('submit-api');
    const configJson = this.root.prevObject.find('.js-aip-licenses__config').text();
    try {
      this.cache.i18nKeys = JSON.parse(configJson);
    } catch (e) {
      this.cache.i18nKeys = {};
      logger.error(e);
    }
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
      .closest('.js-tp-aip-licenses__form-element')
      .addClass('tp-aip-licenses__form-element--error')
      .find(errorMsgSelector)
      .addClass('error-msg--active');
  }

  removeAllErrorMessages() {
    this.root.find('.error-msg--active').removeClass('error-msg--active');
    this.root
      .find('.tp-aip-licenses__form-element--error')
      .removeClass('tp-aip-licenses__form-element--error');
  }

  submitRequestForm = (e) => {
    e.preventDefault();
    let isFormValid = true;
    this.removeAllErrorMessages();

    const $requiredFormElements = this.root.find(':text[required]:visible, select[required]:visible');
    $requiredFormElements.each((idx, el) => {
      if (!$.trim($(el).val())) {
        isFormValid = false;
        this.addErrorMsg(el, '.js-tp-aip-licenses__error-msg-required');
      }
    });

    if (isFormValid) {
      const formData = new FormData(e.currentTarget.form);
      this.showSpinner();

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
          this.renderSuccessMessage();
        }).fail(() => {
          this.cache.$contentWrapper.removeClass('d-none');
          this.cache.$spinner.addClass('d-none');
        });
    }
  };

  bindEvents() {
    this.root.on('click', '.js-tp-aip-licenses-site__btn', this.submitRequestForm);
  }

  renderSiteLicensesData() {
    return _renderSiteLicensesData.apply(this, arguments);
  }

  getSiteLicensesData() {
    return _getSiteLicensesData.apply(this, arguments);
  }

  renderSuccessMessage() {
    return _renderSuccessMessage.apply(this, arguments);
  }

  showTooltip(){
    $('[data-toggle="tooltip"]').tooltip();
  }

  init() {
    this.initCache();
    this.showTooltip();
    this.getSiteLicensesData();
    this.bindEvents();
  }
}

export default PlantMasterLicensesSite;
