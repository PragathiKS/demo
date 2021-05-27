import $ from 'jquery';
import 'bootstrap';
import keyDownSearch from '../../../scripts/utils/searchDropDown';
import { subscriptionAnalytics } from './subscriptionform.analytics.js';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { ajaxMethods, REG_EMAIL } from '../../../scripts/utils/constants';
import { validateFieldsForTags } from '../../../scripts/common/common';
class Subscriptionform {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    /* Initialize selector cache here */
    this.cache.$modal = this.root.parent().find('.js-subscription-modal');
    this.cache.$componentName = this.root.find('input[type="hidden"][name="ComponentNameSubscribe"]').val();
    this.cache.$parentComponent = this.root.find('input[type="hidden"][name="parentComponentSubscribe"]').val();
    this.cache.businessformapi = this.root.find(`form.pw-form-subscriptionForm-${this.cache.$componentName}`);
    this.cache.$submitBtn = this.root.find('button[type="submit"]');
    this.cache.communicationTypes = this.root.find('.communication-type');
    this.cache.communicationError = this.root.find('.communication-error');
    this.cache.$dropItem = $('.pw-form__dropdown a.dropdown-item', this.root);
    this.cache.$dropdownButton = $('.dropdown-menu, .dropdown-toggle', this.root);
    this.cache.countryList = [];
    this.cache.requestPayload = {
      'consent' :'',
      'country':'',
      'pageurl': window.location.href
    };
    this.cache.requestPayload[`pardot_extra_field_${this.cache.$componentName}`]='';
    this.cache.requestPayload[`firstName-${this.cache.$componentName}`]='';
    this.cache.requestPayload[`lastName-${this.cache.$componentName}`]='';
    this.cache.requestPayload[`email-${this.cache.$componentName}`]='';
  }

  validEmail(email) {
    return REG_EMAIL.test(email);
  }
  
  /**
   * key down search in case of country field
   * @param {object} event event
   * @param {Array} options options
  */
  onKeydown = (event, options) => {
    const { $dropdownButton } = this.cache;
    if ($dropdownButton.hasClass('show')) {
      keyDownSearch.call(this, event, options);
    }
  };

  /**
   * function to enable auto suggest
  */
  getCountryList() {
    const { $dropItem,$dropdownButton } = this.cache;
    const self = this;
    $dropItem.map(function () {
      const datael = $(this)[0];
      self.cache.countryList.push($(datael).data('countrytitle'));
    });
    $dropdownButton.keydown(e => this.onKeydown(e, this.cache.countryList));
  }

  submitForm = () => {
    const servletPath = this.cache.businessformapi.data('sf-api-servlet');
    const pardotURL = this.cache.businessformapi.data('sf-pardot-url');
    const countryCode = this.cache.businessformapi.data('sf-countrycode');
    const marketSiteSubscribed = this.cache.businessformapi.data('sf-marketsitesubscribed');
    const langCode = this.cache.businessformapi.data('sf-langcode');

    const dataObj = {};
    if($('input[name="consent"]').is(':checked')) {
      dataObj['marketingConsent'] = $('input[name="consent"]').is(':checked');
    }
    dataObj['email'] = this.cache.requestPayload[`email-${this.cache.$componentName}`];
    dataObj['firstName'] = this.cache.requestPayload[`firstName-${this.cache.$componentName}`];
    dataObj['lastName'] = this.cache.requestPayload[`lastName-${this.cache.$componentName}`];
    dataObj['pardot_extra_field'] = this.cache.requestPayload[`pardot_extra_field_${this.cache.$componentName}`];
    dataObj['language'] = langCode;
    dataObj['site'] = countryCode;
    dataObj['marketSiteSubscribed'] = marketSiteSubscribed;
    dataObj['country'] = this.cache.requestPayload['country'];
    dataObj['pageurl'] = this.cache.requestPayload['pageurl'];
    dataObj['pardotUrl'] = pardotURL;
    subscriptionAnalytics(this.mainHead, { ...this.restObj,'country':dataObj.country, 'Marketing Consent': dataObj.marketingConsent ? 'Checked':'Unchecked' }, 'formcomplete', 'formload', 'Step 1', 'Subscribe', []);

    ajaxWrapper.getXhrObj({
      url: servletPath,
      method: ajaxMethods.POST,
      data: dataObj
    }).done(
      () => {
        $('.pw-subscription__modalTitle').hide();
        $('.pw-subscription__thankYouTitle').show();
        $('.sf-tab-pane', this.root).removeClass('active');
        $('#sf-step-final', this.root).addClass('active');
        $('.serviceError').removeClass('d-block');
      }
    ).fail(() => {
      $('.serviceError').addClass('d-block');
    });
  }


  bindEvents() {
    const { requestPayload, $submitBtn, $dropItem } = this.cache;
    this.root.on('click', '.js-close-btn', this.hidePopUp)
      .on('showSubscription-pw', this.showPopup);
    
    const self = this;
    $submitBtn.click(function (e) {
      e.preventDefault();
      e.stopPropagation();

      let isvalid = true;
      const errObj = [];
      const tab = $(this).closest('.tab-content-steps');
      const input = tab.find('input');
      
      if (!$(this).hasClass('previousbtn') && (input.length > 0)) {
        $('input:not(.communication-type), textarea', tab).each(function () {
          const fieldName = $(this).attr('name');
          const newSafeValues = $(this).attr('type') !== 'hidden' ? validateFieldsForTags($(this).val()) : $(this).val();
          $('div.'+fieldName).text($(this).val());
          if (fieldName in self.cache.requestPayload) {
            requestPayload[fieldName] = newSafeValues;
          }
          if($(this).attr('type') === 'checkbox' && $(this).attr('name') === 'consent'){
            requestPayload[fieldName] = $('input[name="consent"]:checked').length > 0;
          }
          if (($(this).prop('required') && $(this).val() === '') || (fieldName === `email-${self.cache.$componentName}` && !self.validEmail($(this).val())) || ((fieldName === 'consent') && !$(this).prop('checked'))) {
            isvalid = false;
            const errmsg = $(this).closest('.form-group, .formfield').find('.errorMsg').text().trim();
            const erLbl = $(`#sf-step-1 label`)[0].textContent;
            errObj.push({
              formErrorMessage: errmsg,
              formErrorField: erLbl
            });
            e.preventDefault();
            e.stopPropagation();
            $(this).closest('.form-group, .formfield').addClass('field-error');
          } else {
            $(this).closest('.form-group, .formfield').removeClass('field-error');
          }
        });
      }
      if (isvalid) {
        tab.find('.form-group, .formfield').removeClass('field-error');
        self.submitForm();
      }else {
        subscriptionAnalytics(self.mainHead, {}, 'formerror', 'formclick', 'Step 1', 'Subscribe', errObj);
      }
    });

    $dropItem.click(function (e) {
      e.preventDefault();
      const countryTitle = $(this).data('countrytitle');
      const parentDrop = $(this).closest('.dropdown');
      $('.dropdown-toggle span', parentDrop).text(countryTitle);
      $('input', parentDrop).val(countryTitle);
      requestPayload['country'] = countryTitle;
    });

  }

  showPopup = () => {
    const $this = this;
    const { $modal } = $this.cache;
    this.resetModal();
    $modal.modal();
  }

  hidePopUp = () => {
    const $this = this;
    $this.root.modal('hide');
    this.resetModal();
  }

  resetModal = () => {
    /* Reset modal */
    $('.pw-subscription__modalTitle').show();
    $('.pw-subscription__thankYouTitle').hide();
    $('.sf-tab-pane', this.root).addClass('active');
    $('#sf-step-final', this.root).removeClass('active');
  }

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.restObj = {};
    this.mainHead = $($('#sf-step-1 .main-heading').find('h2')[0]).text().trim();
    $('#sf-step-1 label').slice(0,1).each((i, v) => this.restObj[$(v).text()] = 'NA');
    subscriptionAnalytics(this.mainHead, {}, 'formstart', 'formload', '', '', []);
    this.getCountryList();
  }
}



export default Subscriptionform;