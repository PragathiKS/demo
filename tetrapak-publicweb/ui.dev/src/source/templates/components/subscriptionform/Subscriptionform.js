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
    this.cache.businessformapi = this.root.find('form.pw-form-subscriptionForm');
    this.cache.$submitBtn = $('form.pw-form-subscriptionForm button[type="submit"]', this.root);
    this.cache.$inputEmail = $('form.pw-form-subscriptionForm  input[type="email"]', this.root);
    this.cache.communicationTypes = this.root.find('.communication-type');
    this.cache.communicationError = this.root.find('.communication-error');
    this.cache.$dropItem = $('.pw-form__dropdown a.dropdown-item', this.root);
    this.cache.$dropdownButton = $('.dropdown-menu, .dropdown-toggle', this.root);
    this.cache.countryList = [];

    this.cache.requestPayload = {
      'emailSubscription': '',
      'consent' :'',
      'types-communication':[],
      'interestArea':['Processing','End To End - Solutions','Services','Sustainability','Packaging','Innovation'],
      'country':'',
      'pageurl': window.location.href
    };

  }

  validEmail(email) {
    return REG_EMAIL.test(email);
  }

  selectCommunicationHandler = () => {
    const self= this;
    const communicationObj={};
    const {requestPayload,communicationTypes} = this.cache;
    requestPayload['types-communication'] = [];
    communicationTypes.each(function(){
      if($(this).is(':checked')){
        const val=$(this).val();
        requestPayload['types-communication'].push($(this).val());
        communicationObj[val]= 'Checked';
      }
      self.restObj =Object.assign(self.restObj,communicationObj);
    });
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
    const countryCode = this.cache.businessformapi.data('sf-countrycode');
    const marketSiteSubscribed = this.cache.businessformapi.data('sf-marketsitesubscribed');
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
    dataObj['marketSiteSubscribed'] = marketSiteSubscribed;
    dataObj['types-communication'] = this.cache.requestPayload['types-communication'];
    dataObj['interestArea'] = this.cache.requestPayload['interestArea'];
    dataObj['country'] = this.cache.requestPayload['country'];
    dataObj['pageurl'] = this.cache.requestPayload['pageurl'];

    subscriptionAnalytics(this.mainHead, { ...this.restObj,'country':dataObj.country, 'Marketing Consent': dataObj.marketingConsent ? 'Checked':'Unchecked' }, 'formcomplete', 'formload', 'Step 1', 'Subscribe', []);


    ajaxWrapper.getXhrObj({
      url: servletPath,
      method: ajaxMethods.POST,
      data: $.param(dataObj,true),
      dataType: 'html'
    }).done(
      () => {
        $('.sf-tab-pane', this.root).removeClass('active');
        $('#sf-step-final', this.root).addClass('active');
        $('.serviceError').removeClass('d-block');
        $('#sf-step-final', this.root)[0].scrollIntoView({block:'center'});
        $('html, body').animate({
          scrollTop: $('#sf-step-final').offset().top - 150
        });
      }
    ).fail(() => {
      $('.serviceError').addClass('d-block');
    });
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
          if (($(this).prop('required') && $(this).val() === '') || (fieldName === 'emailSubscription' && !self.validEmail($(this).val()))) {
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
