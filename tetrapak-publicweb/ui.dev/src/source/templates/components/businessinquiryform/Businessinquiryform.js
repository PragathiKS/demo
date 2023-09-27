import $ from 'jquery';
import 'bootstrap';
import keyDownSearch from '../../../scripts/utils/searchDropDown';
import { makeLoad, changeStepNext, loadThankYou, changeStepPrev, changeStepError, newPage } from './businessinquiryform.analytics.js';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { ajaxMethods, REG_EMAIL, REG_NUM } from '../../../scripts/utils/constants';
import { validateFieldsForTags, removeParams, storageUtil } from '../../../scripts/common/common';

function isInvalidBusinessAreaOption(key, businessArea) {
  if(key === 'businessArea') {
    return false;
  }
  const isInvalid = key.startsWith('businessArea') && !key.toLowerCase().includes(businessArea.toLowerCase());
  return isInvalid;
}

function capitalizeFirstLetter([ first, ...rest ]) {
  return [ first.toUpperCase(), ...rest ].join('');
}
class Businessinquiryform {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    /* Initialize selector cache here */
    const ua = window.navigator.userAgent;
    const trident = ua.indexOf('Trident/');
    if (trident > 0) { // detect ie 11
      $('.pw-progressbar__steps').css('padding-left', '32px');
    }
    this.cache.businessformapi = this.root.find('form.pw-form-businessEnquiry');
    this.cache.$nextbtn = this.root.find('.pw-businessEnquiry-form .tpatom-btn[type=button]');
    this.cache.$purposeContact = this.root.find('input[type=radio][name="purposeOfContactOptionsInBusinessEq"]');
    this.cache.$baIntPackaging = this.root.find('input[type=radio][name="businessAreaInterestPackaging"]');
    this.cache.$baIntProcessingSupport = this.root.find('input[type=radio][name="businessAreaInterestProcessingSupport"]');
    this.cache.$baIntProcessingCategoryFood = this.root.find('input[type=radio][name="businessAreaProcessingCategoryFood"]');
    this.cache.$subFoodCategory = $('.sub-food-data');
    this.cache.$baIntServices = this.root.find('input[type=radio][name="businessAreaInterestServices"]');
    this.cache.$businessInterest = this.root.find('input[type=checkbox][name="purposeOfContactOptionsInInterestArea"]');
    this.cache.$businessEnqNeed = this.root.find('input[type=radio][name="businessEnquiryNeed"]');
    this.cache.$newRequestBtn = $('.newRequestBtn', this.root);
    this.cache.$submitBtn = $('form.pw-form-businessEnquiry button[type="submit"]', this.root);
    this.cache.$inputText = $('form.pw-form-businessEnquiry input[type="text"]', this.root);
    this.cache.$inputEmail = $('form.pw-form-businessEnquiry input[type="email"]', this.root);
    this.cache.$dropItem = $('.pw-form__dropdown a.dropdown-item', this.root);
    this.cache.countryList = [];
    this.cache.positionList = [];
    this.cache.functionList = [];
    this.cache.roleList = [];
    this.cache.$roleField = this.root.find('.formfield.role-field');
    this.cache.$countryField = this.root.find('.formfield.country-field');
    this.cache.$positionField = this.root.find('.formfield.position-field');
    this.cache.$functionField = this.root.find('.formfield.function-field');
    this.cache.$formInfo = this.root.find('form');
    this.cache.$isFormStart = false;
    this.cache.$countryOrigin = this.cache.businessformapi.data('bef-countrycode');
    this.cache.$preFix = 'Contact Sales | ';
    this.cache.requestPayload = {
      'domainURL': window.location.host,
      'firstName': '',
      'lastName': '',
      'email': '',
      'businessEnquiryMessage': '',
      'phone': '',
      'workplaceCity': '',
      'company': '',
      'pageurl': window.location.href
    };
  }

  getFormHandler() {
    const befPardotURL = this.cache.businessformapi.data('bef-pardoturl');
    const countryData = {};

    countryData['formHandler'] = befPardotURL;
    return countryData;
  }

  onKeydown(event, options) {
    if ($('.dropdown-menu').hasClass('show')) {
      keyDownSearch.call(this, event, options);
    }
  }

  getCountryList() {
    const self = this;
    $('.country-dropdown-select > a').map(function () {
      const datael = $(this)[0];
      self.cache.countryList.push($(datael).data('title'));
    });
    $('.country-dropdown, .country-dropdown-select').keydown(e => this.onKeydown(e, this.cache.countryList));
  }

  getPositionList() {
    const self = this;
    $('.position-dropdown-select > a').map(function () {
      const datael = $(this)[0];
      self.cache.positionList.push($(datael).data('title'));
    });
    $('.position-dropdown, .position-dropdown-select').keydown(e => this.onKeydown(e, this.cache.positionList));
  }

  getFunctionList() {
    const self = this;
    $('.function-dropdown-select > a').map(function () {
      const datael = $(this)[0];
      self.cache.functionList.push($(datael).data('title'));
    });
    $('.function-dropdown, .function-dropdown-select').keydown(e => this.onKeydown(e, this.cache.functionList));
  }

  getRoleList() {
    const self = this;
    $('.role-dropdown-select > a').map(function () {
      const datael = $(this)[0];
      self.cache.roleList.push($(datael).data('title'));
    });
    $('.role-dropdown, .role-dropdown-select').keydown(e => this.onKeydown(e, this.cache.roleList));
  }

  validateField(val) {
    if (String(val).indexOf('>') > -1 || String(val).indexOf('<') > -1) {
      return true;
    }
    return false;
  }

  validEmail(email) {
    return REG_EMAIL.test(email);
  }

  validPhone(phone) {
    return REG_NUM.test(phone);
  }

  reloadPage() {
    location.reload();
  }

  newRequestHanlder = e => {
    e.preventDefault();
    e.stopPropagation();
    newPage(this.linkTitle, this.linkText);
    this.reloadPage();
  }

  // Get Analytics Object
  getAnalyticsObject = (tab) => {
    const $objAnalytics = {};
    $('input, textarea', tab).each(function () {
      if($(this).attr('type') !== 'radio' && $(this).attr('type') !== 'checkbox') {
        const fieldKeyName = $(this).attr('data-analyticsField') ? $(this).attr('data-analyticsField'):$(this).attr('name');
        $objAnalytics[fieldKeyName] = $(this).val();
      }
    });
    return $objAnalytics;
  }

  submitForm = () => {
    const { requestPayload } = this.cache;
    const self = this;
    const servletPath = this.cache.businessformapi.data('bef-api-servlet');
    const countryCode = this.cache.businessformapi.data('bef-countrycode');
    const langCode = this.cache.businessformapi.data('bef-langcode');
    const befPardotURL = this.cache.businessformapi.data('bef-pardoturl')?.replace(/^https?:\/\//, '');
    const dataObj = {};
    $.each( requestPayload, function( key, value ) {
      if(isInvalidBusinessAreaOption(key, requestPayload.businessArea)) {
        return;
      }
      dataObj[key] = value;
    });
    
    dataObj['language'] = langCode;
    dataObj['site'] = countryCode;
    if(requestPayload.country === 'China' || countryCode ==='cn') {
      dataObj['route_country'] = 'China';
    }
    else {
      dataObj['route_country'] = 'Global';
    }
    dataObj['pardotUrl'] = befPardotURL;
    dataObj['pardot_extra_field'] = this.cache.requestPayload.pardot_extra_field || '';
    if(this.root.find(`#befconsentcheckbox`).is(':checked')){
      dataObj['marketingConsent'] = capitalizeFirstLetter(String(this.root.find(`#befconsentcheckbox`).is(':checked')));
    }

    dataObj['pageurl'] = this.cache.requestPayload.pageurl;

    loadThankYou(self.mainHead, 'Step 4', self.cache.requestPayload['purposeOfInterestAreaEqTitle'], { ...self.restObj, ...self.restObj2, 'Marketing Consent': 'Checked' }, self.getFormHandler());
    window.scrollTo(0, $('.pw-businessEnquiry-form').offset().top);

    // IF UTM fields in URL
    const params = {};
    window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(_, key, value) {
      return params[key] = value;
    });

    let pageURL = this.cache.requestPayload.pageurl;

    Object.keys(params).forEach(key => {
      if(key === 'utm_campaign') {
        dataObj['utm_campaign'] = params[key];
        pageURL = removeParams('utm_campaign', pageURL);
      } else if(key === 'utm_content') {
        dataObj['utm_content'] = params[key];
        pageURL = removeParams('utm_content', pageURL);
      } else if(key === 'utm_medium') {
        dataObj['utm_medium'] = params[key];
        pageURL = removeParams('utm_medium', pageURL);
      } else if(key === 'utm_source') {
        dataObj['utm_source'] = params[key];
        pageURL = removeParams('utm_source', pageURL);
      }
    });

    pageURL = pageURL.split('?');
    pageURL = pageURL[0];
  
    // Send Visitor Params
    const visitorId = storageUtil.getCookie('visitor_id857883');
    if(visitorId) {
      dataObj['pardot_cookie_id'] = visitorId;
    }

    ajaxWrapper.getXhrObj({
      url: servletPath,
      method: ajaxMethods.POST,
      data: dataObj
    }).done(
      (response) => {
        if (response.statusCode === '200') {
          $('.bef-tab-pane', this.root).removeClass('active');
          $('#bef-step-final', this.root).addClass('active');
          $('.serviceError').removeClass('d-block');
          $('html, body').animate({
            scrollTop: $('#befUs').offset().top - 150
          });
        } else {
          $('.serviceError').addClass('d-block');
        }
      }
    );
  }
  
  onRadioClickHandler = (e, hiddenVal, rpVal) => {
    const { requestPayload } = this.cache;
    const labelValue = $('label[for="'+e.target.id+'"]').text().trim();
    $('input[type=hidden][name="'+hiddenVal+'"]').val(labelValue);
    requestPayload[rpVal] = e.target.value;
  }

  onBusinessInterestChangeHandler = (e) => {
    const self = this;
    const { $businessInterest } = this.cache;
    const checkedItems = [];
    const labelItems = [];
    const step1Btn = $('#bef-step-1 .step1Btn');
    const step2Btn = $('#bef-step-2 .step2Btn');
    $('#businessEnquiryMessageText').attr('placeholder', $('#messageBoxDiv').attr('data-general-placeholder'));
    
    $businessInterest.each(function() {
      if($(this).prop('checked')) {
        checkedItems.push($(this).val());
        labelItems.push($(this).attr('data-baoi'));
      } else {
        const index = checkedItems.indexOf($(this).val());
        if (index > -1) {
          checkedItems.splice(index, 1);
          labelItems.splice(index, 1);
        }
      }
    });
    if(checkedItems.length === 0) {
      $(step1Btn).attr('data-target', '#bef-step-2');
      $(step2Btn).attr('data-target', '#bef-step-1');
      self.setRequestPayload('', '');
    } else if(checkedItems.length === 1) {
      $(step1Btn).attr('data-target', '#businessInquiry_'+checkedItems[0].toLowerCase());
      $(step2Btn).attr('data-target', '#businessInquiry_'+checkedItems[0].toLowerCase());
      self.setRequestPayload(labelItems[0] , checkedItems[0]);
      if(checkedItems[0].toLowerCase() === 'packaging' || checkedItems[0].toLowerCase() === 'services') {
        $('.summary-interest').addClass('show');
        $('.summary-packaging').removeClass('show');
      }
      
    } else if(checkedItems.length === 2) {
      $(step1Btn).attr('data-target', '#bef-step-2');
      $(step2Btn).attr('data-target', '#bef-step-1');
      self.setRequestPayload(labelItems.join(' , '), checkedItems.join(' and '));
      $('.summary-packaging').removeClass('show');
      $('.summary-interest').removeClass('show');
    } else if(checkedItems.length === 3) {
      self.setRequestPayload(labelItems.join(' , '), checkedItems.join(' , '));
      $('.summary-packaging').removeClass('show');
      $('.summary-interest').removeClass('show');
    }
    if(checkedItems.length === 1 && checkedItems[0].toLowerCase() === 'processing') {
      $('#businessEnquiryMessageText').attr('placeholder', $('#processingCheckboxDiv').attr('data-processing-msg-placeholder-text'));
      $('.summary-packaging').addClass('show');
      $('.summary-interest').removeClass('show');
    }

    if(checkedItems.length > 0) {
      $(e.currentTarget).closest('.formfield').removeClass('field-error');
    }
  }

  setRequestPayload = (lbl , val) => {
    const { requestPayload } = this.cache;
    if(val.indexOf(',') > 0){
      requestPayload['businessArea'] = 'All 3';
    } else {
      requestPayload['businessArea'] = val;
    }
    $('input[type=hidden][name="purposeOfInterestAreaEqTitle"]').val(lbl);
    requestPayload['purposeOfInterestAreaEqTitle'] = lbl;
  }

  resetBusinessIntFields = () => {
    const self = this;
    $('input[type=hidden][name="specificInterestAreaPackagingEqTitle"]').val('');
    $('input[type=hidden][name="businessAreaInterestProcessingSupportEqTitle"]').val('');
    $('input[type=hidden][name="businessAreaProcessingCategoryFoodEqtitle"]').val('');
    $('input[type=hidden][name="businessAreaInterestServicesEqTitle"]').val('');
    self.resetSubFoodCategory();
  }

  resetSubFoodCategory = () => {
    const { $subFoodCategory } = this.cache;
    $subFoodCategory.hide();
    $subFoodCategory.find('.formfield').removeClass('field-error');
    $subFoodCategory.each(function() {
      const inputHandler = $(this).find('.form-control.field-handler');
      $(inputHandler).val('').removeAttr('required');
      const $subFoodCategoryChecks = $(this).find('input');
      $subFoodCategoryChecks.each(function() {
        $(this).prop('checked', false);
      });
    });
    if(this.cache.requestPayload[this.cache.subFoodCategoryKey]) {
      delete this.cache.requestPayload[this.cache.subFoodCategoryKey];
    }
  }
  
  onBaIntProcessingCategoryFoodHandler = e => {
    const self = this;
    const { requestPayload } = this.cache;
    const id = e.target.id;
    const value = e.target.value;
    const labelValue = $('label[for="'+id+'"]').text().trim();

    // Hide and Reset Sub food Categories
    self.resetSubFoodCategory();
    
    $(e.currentTarget).parent().next('.sub-food-data').show().find('.form-control.field-handler').attr('required', true);
    $('input[type=hidden][name="businessAreaProcessingCategoryFoodEqtitle"]').val(labelValue);
    requestPayload['businessAreaProcessingCategoryFood'] = value;
  }
  
  onBaIntSubCategoryFoodHandler = e => {
    const { requestPayload } = this.cache;
    const formField = $(e.currentTarget).closest('.formfield');
    const inputHandler = $(formField).find('.form-control.field-handler');
    const hiddenInput = $(inputHandler).attr('data-fieldname');
    const id = e.target.id;
    const value = e.target.value;
    const labelValue = $('label[for="'+id+'"]').text().trim();
    $(inputHandler).val(labelValue);
    requestPayload[hiddenInput] = value;
    this.cache.subFoodCategoryKey = e.target.name;
  }

  resetErrorMsg = (e) => {
    if($(this).attr('type') !== 'checkbox') {
      $(e.currentTarget).closest('.formfield').removeClass('field-error');
      $(e.currentTarget).closest('.form-group').removeClass('field-error');
    }
  }
  
  checkMessageLength = () => {
    const msgBox = this.root.find('textarea#businessEnquiryMessageText');
    if(msgBox.val() && msgBox.val().trim() && msgBox.val().trim().length > 204){
      this.root.find('.businessEnquiryMessage').text(`${msgBox.val().substring(0, 204)}...`);
    }
  }

  bindEvents() {
    const { businessformapi, requestPayload, $purposeContact, $baIntPackaging, $baIntProcessingSupport, $baIntProcessingCategoryFood, $subFoodCategory, $baIntServices, $businessInterest, $businessEnqNeed, $newRequestBtn, $nextbtn, $submitBtn, $dropItem } = this.cache;
    
    const self = this;
    $purposeContact.on('change', function(e) {
      self.onRadioClickHandler(e, 'purposeOfContactInBusinessEqTitle', 'purpose');
    });
    $baIntPackaging.on('change', function(e) {
      self.onRadioClickHandler(e, 'specificInterestAreaPackagingEqTitle', 'businessAreaInterestPackaging');
    });
    $baIntProcessingSupport.on('change', function(e) {
      self.onRadioClickHandler(e, 'businessAreaInterestProcessingSupportEqTitle', 'businessAreaInterestProcessingSupport');
    });
    $baIntProcessingCategoryFood.on('change', function(e) {
      self.resetSubFoodCategory();
      $(e.currentTarget).parent().next('.sub-food-data').show().find('.form-control.field-handler').attr('required', true);
      self.onRadioClickHandler(e, 'businessAreaProcessingCategoryFoodEqtitle', 'businessAreaProcessingCategoryFood');
    });
    $baIntServices.on('change', function(e) {
      self.onRadioClickHandler(e, 'businessAreaInterestServicesEqTitle', 'businessAreaInterestServices');
    });
    $businessEnqNeed.on('change', function(e) {
      self.onRadioClickHandler(e, 'businessEnquiryNeedEqTitle', 'businessEnquiryNeed');
    });

    $businessInterest.on('change', this.onBusinessInterestChangeHandler);
    
    // Sub Food Category
    $subFoodCategory.each(function() {
      const $subFoodCategoryChecks = $(this).find('input');
      $subFoodCategoryChecks.each(function() {
        $(this).on('change', self.onBaIntSubCategoryFoodHandler);
      });
    });
    $newRequestBtn.on('click', this.newRequestHanlder);

    // Remove Error message on change
    $('input, textarea', businessformapi).each(function () {
      $(this).on('change', self.resetErrorMsg);
    });
    
    $nextbtn.click(function (e) {
      // Analytics Form Start
      const tabId = $(this).closest('.bef-tab-pane.active').attr('id');
      if(tabId === 'bef-step-1') {
        self.cache.$isFormStart = true;
        makeLoad(self.step1head, self.cache.$preFix+self.mainHead, self.getFormHandler());
      }

      let isvalid = true;
      const target = $(this).attr('data-target'),  tab = $(this).closest('.tab-content-steps'), input = tab.find('input'), textarea = tab.find('textarea'), errObj = [];
      if ($(this).hasClass('previousbtn')) {
        let formTypeTitle = '';
        let formStepNumber = '';
        if(target === '#bef-step-1') {
          formTypeTitle = $(this).closest('.tab-content-steps').find('h4').eq(0).text();
          formStepNumber = $(this).closest('.bef-tab-pane').attr('id') === 'bef-step-2' ? 'Step 3':'Step 2';
        }
        switch (target) {
        case '#bef-step-1':
          changeStepPrev(self.mainHead, formStepNumber, formTypeTitle, self.cache.requestPayload['purposeOfInterestAreaEqTitle'], self.getFormHandler());
          break;
        case '#businessInquiry_packaging':
          changeStepPrev(self.mainHead, 'Step 3', self.step2head, self.cache.requestPayload['purposeOfInterestAreaEqTitle'], self.getFormHandler());
          break;
        case '#businessInquiry_processing':
          changeStepPrev(self.mainHead, 'Step 3', self.step2head, self.cache.requestPayload['purposeOfInterestAreaEqTitle'], self.getFormHandler());
          break;
        case '#businessInquiry_services':
          changeStepPrev(self.mainHead, 'Step 3', self.step2head, self.cache.requestPayload['purposeOfInterestAreaEqTitle'], self.getFormHandler());
          break;
        case '#bef-step-2':
          changeStepPrev(self.mainHead, 'Step 4', self.step3head, self.cache.requestPayload['purposeOfInterestAreaEqTitle'], self.getFormHandler());
          break;
        default:
          break;
        }
      }

      if (!$(this).hasClass('previousbtn') && (input.length > 0 || textarea.length > 0)) {
        $('input, textarea', tab).each(function () {
          const fieldName = $(this).attr('name');
          $('div.' + fieldName).text($(this).val());
          const newSafeValues = $(this).attr('type') !== 'hidden' ? validateFieldsForTags($(this).val()) : $(this).val();
          
          if (fieldName in self.cache.requestPayload) {
            if($(this).attr('type') !== 'radio' && $(this).attr('type') !== 'checkbox') {
              if(fieldName === 'businessEnquiryProfile') {
                requestPayload[fieldName] = self.cache.requestPayload[fieldName];
              } else {
                requestPayload[fieldName] = newSafeValues;
              }
            }
          }
          
          if (($(this).prop('required') && $(this).val() === '') || self.validateField(requestPayload[fieldName]) || ((fieldName === 'email') && !self.validEmail($(this).val())) || (fieldName === 'phone') && !self.validPhone($(this).val())) {
            isvalid = false;
            e.preventDefault();
            e.stopPropagation();
            const errmsg = $(this).closest('.form-group, .formfield').eq(0).find('.errorMsg').text().trim(),
              fieldName = $(this).attr('data-analyticsfield') ? $(this).attr('data-analyticsfield'):$(this).attr('name');
            let erLbl = '';
            switch (fieldName) {
            case 'purposeOfContactInBusinessEqTitle':
              erLbl = self.step1head;
              break;
            case 'purposeOfInterestAreaEqTitle':
              erLbl = self.step1head2;
              break;
            case 'specificInterestAreaPackagingEqTitle':
              erLbl = $(this).closest('.tab-content-steps').find('h4').text();
              break;
            case 'businessAreaInterestServices':
              erLbl = $(this).closest('.tab-content-steps').find('h4').text();
              break;
            case 'businessAreaInterestProcessingSupportEqTitle':
              erLbl = $(this).closest('.tab-content-steps').find('h4').eq(0).text();
              break;
            case 'businessAreaProcessingCategoryFoodEqtitle':
              erLbl = $(this).closest('.tab-content-steps').find('h4').eq(1).text();
              break;
            case 'businessEnquiryNeedEqTitle':
              erLbl = $(this).closest('.tab-content-steps').find('h4').eq(0).text();
              break;
            case 'businessEnquiryProfile':
              erLbl = $('#bef-step-2 label')[2].textContent;
              break;
            case 'businessEnquiryMessage':
              erLbl = $('#bef-step-2 label')[3].textContent;
              break;
            default:
              erLbl = fieldName;
              break;
            }
            errObj.push({
              formErrorMessage: errmsg,
              formErrorField: erLbl
            });
            $(this).closest('.form-group, .formfield').addClass('field-error');
          } else {
            $(this).closest('.form-group, .formfield').removeClass('field-error');
          }
        });
      }
      if (isvalid) {
        tab.find('.form-group, .formfield').removeClass('field-error');
        if (!(self.cache.requestPayload['phone']).length > 0) {
          $('#phoneSummery').hide();
        }
        else {
          $('#phoneSummery').show();
        }

        if (target) {
          $('.bef-tab-pane').removeClass('active');
          $(target).addClass('active');

          // Get Analytics
          const $objAnalytics = self.getAnalyticsObject(tab);

          if (!$(this).hasClass('previousbtn')) {
            let formTypeTitle = '';
            let formStepNumber = '';
            if(target === '#bef-step-2') {
              formTypeTitle = $(this).closest('.tab-content-steps').find('h4').eq(0).text();
              formStepNumber = $(this).closest('.bef-tab-pane').attr('id') === 'bef-step-1' ? 'Step 1':'Step 2';
            }
            switch (target) {
            case '#businessInquiry_packaging':
              changeStepNext(self.mainHead, 'Step 1', self.step1head, self.cache.requestPayload['purposeOfInterestAreaEqTitle'], $objAnalytics, self.getFormHandler());
              break;
            case '#businessInquiry_processing':
              changeStepNext(self.mainHead, 'Step 1', self.step1head, self.cache.requestPayload['purposeOfInterestAreaEqTitle'], $objAnalytics, self.getFormHandler());
              break;
            case '#businessInquiry_services':
              changeStepNext(self.mainHead, 'Step 1', self.step1head, self.cache.requestPayload['purposeOfInterestAreaEqTitle'], $objAnalytics, self.getFormHandler());
              break;
            case '#bef-step-2':
              changeStepNext(self.mainHead, formStepNumber, formTypeTitle, self.cache.requestPayload['purposeOfInterestAreaEqTitle'], $objAnalytics, self.getFormHandler());
              break;
            case '#bef-step-3':
              changeStepNext(self.mainHead, 'Step 3', self.step2head, self.cache.requestPayload['purposeOfInterestAreaEqTitle'], $objAnalytics, self.getFormHandler());
              break;
            default:
              break;
            }
          }
        }
      } else {
        let formTypeTitle = '';
        let formStepNumber = '';

        if(target === '#bef-step-2') {
          formTypeTitle = $(this).closest('.tab-content-steps').find('h4').eq(0).text();
          formStepNumber = $(this).closest('.bef-tab-pane').attr('id') === 'bef-step-1' ? 'Step 1':'Step 2';
        }
        
        switch (target) {
        case '#businessInquiry_packaging':
          changeStepError(self.mainHead, 'Step 1', self.step1head, self.cache.requestPayload['purposeOfInterestAreaEqTitle'], {}, errObj, self.getFormHandler());
          break;
        case '#businessInquiry_processing':
          changeStepError(self.mainHead, 'Step 1', self.step1head, self.cache.requestPayload['purposeOfInterestAreaEqTitle'], {}, errObj, self.getFormHandler());
          break;
        case '#businessInquiry_services':
          changeStepError(self.mainHead, 'Step 1', self.step1head, self.cache.requestPayload['purposeOfInterestAreaEqTitle'], {}, errObj, self.getFormHandler());
          break;
        case '#bef-step-2':
          changeStepError(self.mainHead, formStepNumber, formTypeTitle, self.cache.requestPayload['purposeOfInterestAreaEqTitle'], {}, errObj, self.getFormHandler());
          break;
        case '#bef-step-3':
          changeStepError(self.mainHead, 'Step 3', self.step2head, self.cache.requestPayload['purposeOfInterestAreaEqTitle'], {}, errObj, self.getFormHandler());
          break;
        case '#bef-step-final':
          changeStepError(self.mainHead, 'Step 4', self.step3head, self.cache.requestPayload['purposeOfInterestAreaEqTitle'], {}, errObj, self.getFormHandler());
          break;
        default:
          break;
        }
      }
      self.checkMessageLength();
    });

    $submitBtn.click(function (e) {
      e.preventDefault();
      e.stopPropagation();
      let isvalid = true;
      const errObj = [], target = $(this).data('target'), tab = $(this).closest('.tab-content-steps'), input = tab.find('input'), textarea = tab.find('textarea');

      if (!$(this).hasClass('previousbtn') && (input.length > 0 || textarea.length > 0)) {
        $('input, textarea', tab).each(function () {
          const fieldName = $(this).attr('name');
          const newSafeValues = $(this).attr('type') !== 'hidden' ? validateFieldsForTags($(this).val()) : $(this).val();

          $('div.' + fieldName).text($(this).val());
          
          if (fieldName in self.cache.requestPayload) {
            if($(this).attr('type') !== 'radio' && $(this).attr('type') !== 'checkbox') {
              if(fieldName === 'position') {
                requestPayload[fieldName] = self.cache.requestPayload[fieldName];
              } else if(fieldName === 'function') {
                requestPayload[fieldName] = self.cache.requestPayload[fieldName];
              } else {
                requestPayload[fieldName] = newSafeValues;
              }
            }
          }
          
          if($(this).attr('type') === 'checkbox' && $(this).attr('name') === 'marketingConsent') {
            if($('input[name="marketingConsent"]:checked').length > 0) {
              requestPayload[fieldName] = 'True';
            }
          }
          if (($(this).prop('required') && $(this).val() === '') || ((fieldName === 'email') && !self.validEmail($(this).val())) || (fieldName === 'phone') && !self.validPhone($(this).val())) {
            isvalid = false;
            e.preventDefault();
            e.stopPropagation();
            const errmsg = $(this).closest('.form-group, .formfield').find('.errorMsg').text().trim();
            const fieldName = $(this).attr('name');
            let erLbl = '';
            switch (fieldName) {
            case 'email':
              erLbl = $('#bef-step-3 label')[0].textContent;
              break;
            case 'firstName':
              erLbl = $('#bef-step-3 label')[1].textContent;
              break;
            case 'lastName':
              erLbl = $('#bef-step-3 label')[2].textContent;
              break;
            case 'phone':
              erLbl = $('#bef-step-3 label')[3].textContent;
              break;
            case 'workplaceCity':
              erLbl = $('#bef-step-3 label')[4].textContent;
              break;
            case 'countryTitle':
              erLbl = $(this).closest('.formfield').find('label').text();
              break;
            case 'company':
              erLbl = $('#bef-step-3 label')[6].textContent;
              break;
            case 'position':
              erLbl = $(this).closest('.formfield').find('label').text();
              break;
            case 'function':
              erLbl = $(this).closest('.formfield').find('label').text();
              break;
            default:
              erLbl = fieldName;
              break;
            }
            errObj.push({
              formErrorMessage: errmsg,
              formErrorField: erLbl
            });
            $(this).closest('.form-group, .formfield').addClass('field-error');
          } else {
            $(this).closest('.form-group, .formfield').removeClass('field-error');
          }
        });
      }
      if (isvalid) {
        tab.find('.form-group, .formfield').removeClass('field-error');
        if (target) {
          $('.bef-tab-pane').removeClass('active');
          $(target).addClass('active');
        }
      } else {
        switch (target) {
        case '#bef-step-final':
          changeStepError(self.mainHead, 'Step 4', self.step3head, self.cache.requestPayload['purposeOfInterestAreaEqTitle'], {}, errObj, self.getFormHandler());
          break;
        default:
          break;
        }
      }
      if (isvalid) {
        self.submitForm();
      }
    });

    $dropItem.click(function (e) {
      e.preventDefault();
      const title = $(this).attr('data-title');
      const pardtottitle = $(this).attr('data-pardot-title');
      const field = $(this).attr('data-field-name');
      const fieldtitle = $(this).attr('data-field-title');
      const parentDrop = $(this).closest('.dropdown');
      $('.dropdown-toggle span', parentDrop).text(title);
      $('input', parentDrop).val(title);
      requestPayload[field] = pardtottitle;
      if(field === 'country') {
        self.restObj[self.cache.$countryField.data('country-name-label')] = requestPayload[field];
        requestPayload[fieldtitle] = title;
      }
      if(field === 'position') {
        self.restObj2[self.cache.$positionField.data('position-name-label')] = requestPayload[field];
      }
      if(field === 'function') {
        self.restObj2[self.cache.$functionField.data('function-name-label')] = requestPayload[field];
      }
      $dropItem.removeClass('active');
      $(this).addClass('active');
      $(this).closest('.form-group').removeClass('field-error');
    });
  }

  analyticsFormstart(stepHead, mainHead) {
    const self = this;
    const formElements = $(this.cache.$formInfo).find('input, select, textarea');
    formElements.each(function(i, val) {
      $(val).on('click', function () {
        if (!self.cache.$isFormStart) {
          self.cache.$isFormStart = true;
          makeLoad(stepHead, self.cache.$preFix+mainHead, self.getFormHandler());
        }
      });
    });
  }

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.purposeLabel = $('.purposeOfBusinessContactError').text().trim();
    this.interestLabel = $('.purposeOfBusinessIntersetError').text().trim();
    this.step1head = $('#bef-step-1').find('h4').eq(0).text().trim();
    this.step1head2 = $('#bef-step-1').find('h4').eq(1).text().trim();
    this.step2head = $('#bef-step-2 .tab-content-steps').find('h4').text();
    this.step3head = $('#bef-step-3 .tab-content-steps').find('h4').text();
    this.mainHead = $($('.pw-businessEnquiry-form .main-heading').find('h2')[0]).text().trim();
    this.restObj = {};
    this.restObj2 = {};
    this.linkTitle = this.root.find('.thankyou').find('h2').text().trim();
    this.linkText = this.root.find('.newRequestBtn').text().trim();
    $('#bef-step-3 label:not(.non-title-field)').each((i, v) => this.restObj[$(v).text()] = 'NA');
    $('#bef-step-3 label').slice(0, 1).each((i, v) => this.restObj2[$(v).text()] = 'NA');
    this.getCountryList();
    this.getPositionList();
    this.getFunctionList();
    this.getRoleList();
    this.analyticsFormstart(this.step1head, this.mainHead);
  }
}

export default Businessinquiryform;