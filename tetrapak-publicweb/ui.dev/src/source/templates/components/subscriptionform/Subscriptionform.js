import $ from 'jquery';
import 'bootstrap';
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
    this.cache.businessformapi = this.root.find('form.pw-form-subscriptionForm');
    this.cache.$submitBtn = $('form.pw-form-subscriptionForm button[type="submit"]', this.root);
    this.cache.$inputEmail = $('form.pw-form-subscriptionForm  input[type="email"]', this.root);
    this.cache.communicationTypes = this.root.find('.communication-type');
    this.cache.communicationError = this.root.find('.communication-error');
    this.cache.$dropItem = $('.pw-form__dropdown a.dropdown-item', this.root);

    this.cache.requestPayload = {
      'emailSubscription': '',
      'consent' :'',
      'types-communication':[],
      'interestArea':['Processing','End To End - Solutions','Services','Sustainability','Packaging','Innovation']
    };

  }

  validEmail(email) {
    return REG_EMAIL.test(email);
  }

  selectCommunicationHandler = () => {
    const {requestPayload,communicationTypes} = this.cache;
    requestPayload['types-communication'] = [];
    communicationTypes.each(function(){
      if($(this).is(':checked')){
        requestPayload['types-communication'].push($(this).val());
      }
    });
  }

  submitForm = () => {
    const servletPath = this.cache.businessformapi.data('sf-api-servlet');
    const countryCode = this.cache.businessformapi.data('sf-countrycode');
    const langCode = this.cache.businessformapi.data('sf-langcode');
    const pardot_extra_field = $('#pardot_extra_field_sf').val();

    const dataObj = {};
    if($('input[name="consent"]').is(':checked')) {
      dataObj['marketingConsent'] = $('input[name="consent"]').is(':checked');
    }
    dataObj['email'] = this.cache.requestPayload.emailSubscription;
    dataObj['pardot_extra_field'] = pardot_extra_field;
    dataObj['language'] = langCode;
    dataObj['site'] = countryCode;
    dataObj['types-communication'] = this.cache.requestPayload['types-communication'];
    dataObj['interestArea'] = this.cache.requestPayload['interestArea'];

    subscriptionAnalytics(this.mainHead, { ...this.restObj, 'Marketing Consent': dataObj.marketingConsent ? 'Checked':'Unchecked' }, 'formcomplete', 'formload', 'Step 1', 'Subscribe', []);


    ajaxWrapper.getXhrObj({
      url: servletPath,
      method: ajaxMethods.POST,
      data: $.param(dataObj,true)
    }).done(
      (response) => {
        if (response.statusCode === '200') {
          $('.sf-tab-pane', this.root).removeClass('active');
          $('#sf-step-final', this.root).addClass('active');
          $('.serviceError').removeClass('d-block');
          $('#sf-step-final', this.root)[0].scrollIntoView({block:'center'});
          $('html, body').animate({
            scrollTop: $('#sf-step-final').offset().top - 150
          });
        } else {
          $('.serviceError').addClass('d-block');
        }
      }
    );
  }


  bindEvents() {
    const { requestPayload, $submitBtn, $dropItem } = this.cache;
    const self = this;
    $submitBtn.click(function (e) {
      e.preventDefault();
      e.stopPropagation();
      self.selectCommunicationHandler();
      let isvalid = true;
      const errObj = [];
      const target = $(this).data('target');
      const tab = $(this).closest('.tab-content-steps');
      const input = tab.find('input');
      if(requestPayload['types-communication'].length === 0){
        isvalid = false;
        self.cache.communicationError.addClass('field-error-communication');
      } else {
        isvalid = true;
        self.cache.communicationError.removeClass('field-error-communication');
      }
      if (!$(this).hasClass('previousbtn') && (input.length > 0)) {
        $('input:not(.communication-type), textarea', tab).each(function () {
          const fieldName = $(this).attr('name');
          const newSafeValues = $(this).attr('type') !== 'hidden' ? validateFieldsForTags($(this).val()) : $(this).val();
          $('div.' + fieldName).text($(this).val());
          if (fieldName in self.cache.requestPayload) {
            requestPayload[fieldName] = newSafeValues;
          }
          if($(this).attr('type') === 'checkbox' && $(this).attr('name') === 'consent'){
            requestPayload[fieldName] = $('input[name="consent"]:checked').length > 0;
          }
          if (($(this).attr('type') === 'checkbox' && $(this).attr('name') === 'consent' && !$(this).is(':checked')) || ($(this).prop('required') && $(this).val() === '') || (fieldName === 'emailSubscription' && !self.validEmail($(this).val()))) {
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
        if (target) {
          $('.sf-tab-pane').removeClass('active');
          $(target).addClass('active');
        }
      }
      if (isvalid) {
        self.submitForm();
      }else{
        subscriptionAnalytics(self.mainHead, {}, 'formerror', 'formclick', 'Step 1', 'Subscribe', errObj);
      }
    });

    $dropItem.click(function (e) {
      e.preventDefault();
      const countryTitle = $(this).data('countrytitle');
      const country = $(this).data('country');
      const parentDrop = $(this).closest('.dropdown');
      $('.dropdown-toggle span', parentDrop).text(countryTitle);
      $('input', parentDrop).val(countryTitle);
      requestPayload['country'] = country;
      requestPayload['countryTitle'] = countryTitle;
      $dropItem.removeClass('active');
      $(this).addClass('active');
    });

  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.restObj = {};
    this.mainHead = $($('#sf-step-1 .main-heading').find('h2')[0]).text().trim();
    $('#sf-step-1 label').slice(0,1).each((i, v) => this.restObj[$(v).text()] = 'NA');
    subscriptionAnalytics(this.mainHead, {}, 'formstart', 'formload', '', '', []);
  }
}



export default Subscriptionform;
