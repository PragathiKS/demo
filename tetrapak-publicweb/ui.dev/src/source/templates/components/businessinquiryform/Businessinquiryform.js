/* eslint-disable no-console */
import $ from 'jquery';
import 'bootstrap';
import keyDownSearch from '../../../scripts/utils/searchDropDown';
import { makeLoad, changeStepNext, loadThankYou, changeStepPrev, changeStepError, newPage } from './businessinquiryform.analytics.js';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { ajaxMethods, REG_EMAIL } from '../../../scripts/utils/constants';
import { validateFieldsForTags } from '../../../scripts/common/common';

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
    this.cache.$subFoodCategory = $('.subFoodData');
    this.cache.$baIntServices = this.root.find('input[type=radio][name="businessAreaInterestServices"]');
    this.cache.$businessInterest = this.root.find('input[type=checkbox][name="purposeOfContactOptionsInInterestArea"]');
    this.cache.$businessEnqNeed = this.root.find('input[type=radio][name="businessEnquiryNeed"]');
    this.cache.$newRequestBtn = $('.newRequestBtn', this.root);
    this.cache.$submitBtn = $('form.pw-form-businessEnquiry button[type="submit"]', this.root);
    this.cache.$inputText = $('form.pw-form-businessEnquiry  input[type="text"]', this.root);
    this.cache.$inputEmail = $('form.pw-form-businessEnquiry  input[type="email"]', this.root);
    this.cache.$roleDropItem = $('.role-field-wrapper .pw-form__dropdown a.dropdown-item', this.root);
    this.cache.$dropItem = $('.country-field-wrapper .pw-form__dropdown a.dropdown-item', this.root);
    this.cache.$positionDropItem = $('.position-field-wrapper .pw-form__dropdown a.dropdown-item', this.root);
    this.cache.$functionDropItem = $('.function-field-wrapper .pw-form__dropdown a.dropdown-item', this.root);
    this.cache.countryList = [];
    this.cache.$roleField = this.root.find('.formfield.role-field');
    this.cache.$countryField = this.root.find('.formfield.country-field');
    this.cache.$positionField = this.root.find('.formfield.position-field');
    this.cache.$functionField = this.root.find('.formfield.function-field');
    this.cache.$formInfo = this.root.find('form');
    this.cache.$isFormStart = false;    
    this.cache.requestPayload = {
      'domainURL': window.location.host,
      'purpose': '',
      'businessArea': '',
      /*
      'businessAreaInterestPackaging': '',
      'businessAreaInterestProcessingSupport': '',
      'businessAreaInterestServices': '',
      'businessAreaProcessingCategoryFood': '',
      'businessAreaProcessingNeedBeverage': '',
      'businessAreaProcessingNeedCheese': '',
      'businessAreaProcessingNeedDairy': '',
      'businessAreaProcessingNeedPreparedFoods': '',
      'businessAreaProcessingNeedPowder': '',
      'businessEnquiryProfile': '',
      'businessEnquiryProfileOther': '',
      */
      'businessEnquiryNeed': '',
      'firstName': '',
      'lastName': '',
      'email': '',
      'message': '',
      'phone': '',
      'workplaceCity': '',
      'country': '',
      'countryTitle': '',
      'company': '',
      'position': '',
      'function': '',
      'marketingConsent':'',
      /*
      'purposeOfContactInBusinessEqTitle': '',
      'purposeOfInterestAreaEqTitle': '',
      'specificInterestAreaPackagingEqTitle': '',
      'businessAreaInterestProcessingSupportEqTitle': '',
      'businessAreaProcessingCategoryFoodEqtitle': '',
      'businessAreaInterestServicesEqTitle': '',
      'businessEnquiryNeedEqTitle': '',
      */
      'pageurl': window.location.href
    };
    /*
    // Previous RequestPayload
    this.cache.requestPayload = {
      'domainURL': window.location.host,
      'purposeOfContact': '',
      'interestArea': '',
      'firstNameField': '',
      'lastNameField': '',
      'emailBef': '',
      'message': '',
      'phoneField': '',
      'cityOfWorkPlaceField': '',
      'purposeOfContactInBusinessEqTitle': '',
      'purposeOfInterestAreaEqTitle': '',
      'specificInterestAreaPackagingEqTitle': '',
      'businessAreaInterestProcessingSupportEqTitle': '',
      'businessAreaProcessingCategoryFoodEqtitle': '',
      'businessAreaInterestServicesEqTitle': '',
      'businessEnquiryNeedEqTitle': '',
      'company': '',
      'position': '',
      'country': '',
      'countryTitle': '',
      'marketingConsent':'',
      'pageurl': window.location.href
    };
     */
  }

  onKeydown = (event, options) => {
    if ($('.dropdown-menu').hasClass('show')) {
      keyDownSearch.call(this, event, options);
    }
  };

  getCountryList() {
    const self = this;
    $('.country-dropdown-select > a').map(function () {
      const datael = $(this)[0];
      self.cache.countryList.push($(datael).data('countrytitle'));
    });
    $('.country-dropdown, .country-dropdown-select').keydown(e => this.onKeydown(e, this.cache.countryList));
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

  reloadPage() {
    location.reload();
  }

  newRequestHanlder = e => {
    e.preventDefault();
    e.stopPropagation();
    newPage(this.linkTitle, this.linkText);
    this.reloadPage();
  }

  submitForm = () => {
    const { requestPayload } = this.cache;
    const self = this;
    const servletPath = this.cache.businessformapi.data('bef-api-servlet');
    const countryCode = this.cache.businessformapi.data('bef-countrycode');
    const langCode = this.cache.businessformapi.data('bef-langcode');
    const dataObj = {};
    $.each( requestPayload, function( key, value ) {
      dataObj[key] = value;
    });
    
    /*
    dataObj['purpose'] = this.cache.requestPayload.purposeOfContactTitle;
    dataObj['businessArea'] = this.cache.requestPayload.areaOfInterestTitle;
    dataObj['firstName'] = this.cache.requestPayload.firstNameField;
    dataObj['lastName'] = this.cache.requestPayload.lastNameField;
    dataObj['email'] = this.cache.requestPayload.emailBef;
    if(this.cache.requestPayload.phoneField.trim()){
      dataObj['phoneNumber'] = this.cache.requestPayload.phoneField;
    }
    dataObj['company'] = this.cache.requestPayload.company;
    dataObj['businessEnquiryMessage'] = this.cache.requestPayload.message;
    dataObj['position'] = this.cache.requestPayload.position;
    dataObj['country'] = this.cache.requestPayload.country;
    dataObj['countryTitle'] = this.cache.requestPayload.countryTitle;
    */
    dataObj['language'] = langCode;
    dataObj['site'] = countryCode;
    if(this.root.find(`#befconsentcheckbox`).is(':checked')){
      dataObj['marketingConsent'] = this.root.find(`#befconsentcheckbox`).is(':checked');
    }
    dataObj['pardot_extra_field'] = this.cache.requestPayload.pardot_extra_field;
    dataObj['pageurl'] = this.cache.requestPayload.pageurl;
    loadThankYou(self.mainHead, self.cache.requestPayload['purposeOfInterestAreaEqTitle'], { ...self.restObj2, 'Marketing Consent': 'Checked' });
    window.scrollTo(0, $('.pw-businessEnquiry-form').offset().top);

    // IF UTM fields in URL
    const params = {};
    window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(_, key, value) {
      return params[key] = value;
    });
    
    Object.keys(params).forEach(key => {
      if(key === 'utm_campaign') {
        dataObj['utm_campaign'] = params[key];
      } else if(key === 'utm_content') {
        dataObj['utm_content'] = params[key];
      } else if(key === 'utm_medium') {
        dataObj['utm_medium'] = params[key];
      } else if(key === 'utm_source') {
        dataObj['utm_source'] = params[key];
      }
    });

    console.log('Hiren Parmar - Data Object', dataObj);

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
  
  onPurposeOfContactHandler = e => {
    const { requestPayload } = this.cache;
    const id = e.target.id;
    const value = e.target.value;
    const labelValue = $('label[for="'+id+'"]').text().trim();
    $('input[type=hidden][name="purposeOfContactInBusinessEqTitle"]').val(labelValue);
    requestPayload['purpose'] = value;
    // requestPayload['purposeOfContactInBusinessEqTitle'] = labelValue;
  }

  onBusinessInterestChangeHandler = () => {
    const self = this;
    const { $businessInterest } = this.cache;
    const checkedItems = [];
    const step1Btn = $('#bef-step-1 .step1Btn');
    const step2Btn = $('#bef-step-2 .step2Btn');
    
    $businessInterest.each(function() {
      if($(this).prop('checked')) {
        checkedItems.push($(this).val());
      } else {
        const index = checkedItems.indexOf($(this).val());
        if (index > -1) {
          checkedItems.splice(index, 1);
        }
      }
    });
    
    if(checkedItems.length === 0) {
      $(step1Btn).attr('data-target', '#bef-step-2');
      $(step2Btn).attr('data-target', '#bef-step-1');
      self.setRequestPayload('');
    } else if(checkedItems.length === 1) {
      $(step1Btn).attr('data-target', '#businessInquiry_'+checkedItems[0].toLowerCase());
      $(step2Btn).attr('data-target', '#businessInquiry_'+checkedItems[0].toLowerCase());
      self.setRequestPayload(checkedItems[0]);
    } else if(checkedItems.length === 2) {
      $(step1Btn).attr('data-target', '#bef-step-2');
      $(step2Btn).attr('data-target', '#bef-step-1');
      self.setRequestPayload(checkedItems.join(' and '));
    } else if(checkedItems.length === 3) {
      self.setRequestPayload('All 3');
    }
  }

  setRequestPayload = (val) => {
    const { requestPayload } = this.cache;
    requestPayload['businessArea'] = val;
    // requestPayload['purposeOfInterestAreaEqTitle'] = val;
    $('input[type=hidden][name="purposeOfInterestAreaEqTitle"]').val(val);
  }
  
  onBaIntPackagingHandler = e => {
    const { requestPayload } = this.cache;
    const id = e.target.id;
    const value = e.target.value;
    const labelValue = $('label[for="'+id+'"]').text().trim();
    $('input[type=hidden][name="specificInterestAreaPackagingEqTitle"]').val(labelValue);
    requestPayload['businessAreaInterestPackaging'] = value;
    // requestPayload['specificInterestAreaPackagingEqTitle'] = labelValue;
  }

  onBaIntProcessingSupportHandler = e => {
    const { requestPayload } = this.cache;
    const id = e.target.id;
    const value = e.target.value;
    const labelValue = $('label[for="'+id+'"]').text().trim();
    $('input[type=hidden][name="businessAreaInterestProcessingSupportEqTitle"]').val(labelValue);
    requestPayload['businessAreaInterestProcessingSupport'] = value;
    // requestPayload['businessAreaInterestProcessingSupportEqTitle'] = labelValue;
  }

  onBaIntProcessingCategoryFoodHandler = e => {
    const { requestPayload, $subFoodCategory } = this.cache;
    const id = e.target.id;
    const value = e.target.value;
    const labelValue = $('label[for="'+id+'"]').text().trim();

    // Hide and Reset Sub food Categories
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
    
    $(e.currentTarget).parent().next('.subFoodData').show().find('.form-control.field-handler').attr('required', true);
    $('input[type=hidden][name="businessAreaProcessingCategoryFoodEqtitle"]').val(labelValue);
    requestPayload['businessAreaProcessingCategoryFood'] = value;
    // requestPayload['businessAreaProcessingCategoryFoodEqtitle'] = labelValue;
  }

  onBaIntSubCategoryFoodHandler = e => {
    const { requestPayload } = this.cache;
    const inputHandler = $(e.currentTarget).closest('.formfield').find('.form-control.field-handler');
    const hiddenInput = $(inputHandler).attr('data-fieldname');
    const id = e.target.id;
    const value = e.target.value;
    const labelValue = $('label[for="'+id+'"]').text().trim();
    $(inputHandler).val(labelValue);
    requestPayload[hiddenInput] = value;
    // requestPayload[hiddenInput+'Eqtitle'] = labelValue;
  }

  onBaIntServicesHandler = e => {
    const { requestPayload } = this.cache;
    const id = e.target.id;
    const value = e.target.value;
    const labelValue = $('label[for="'+id+'"]').text().trim();
    $('input[type=hidden][name="businessAreaInterestServicesEqTitle"]').val(labelValue);
    requestPayload['businessAreaInterestServices'] = value;
    // requestPayload['businessAreaInterestServicesEqTitle'] = labelValue;
  }
  
  onBusinessEnqNeedHandler = e => {
    const { requestPayload } = this.cache;
    const id = e.target.id;
    const value = e.target.value;
    const labelValue = $('label[for="'+id+'"]').text().trim();
    $('input[type=hidden][name="businessEnquiryNeedEqTitle"]').val(labelValue);
    requestPayload['businessEnquiryNeed'] = value;
    // requestPayload['businessEnquiryNeedEqTitle'] = labelValue;
  }

  checkMessageLength = () => {
    const msgBox = this.root.find('textarea#businessEnquiryMessageText');
    if(msgBox.val() && msgBox.val().trim() && msgBox.val().trim().length > 170){
      this.root.find('.message').text(`${msgBox.val().substring(0, 170)}...`);
    }
  }

  bindEvents() {
    const { requestPayload, $purposeContact, $baIntPackaging, $baIntProcessingSupport, $baIntProcessingCategoryFood, $subFoodCategory, $baIntServices, $businessInterest, $businessEnqNeed, $newRequestBtn, $nextbtn, $submitBtn, $roleDropItem, $dropItem, $positionDropItem, $functionDropItem } = this.cache;
    
    const self = this;
    $purposeContact.on('change', this.onPurposeOfContactHandler);
    $baIntPackaging.on('change', this.onBaIntPackagingHandler);
    $baIntProcessingSupport.on('change', this.onBaIntProcessingSupportHandler);
    $baIntProcessingCategoryFood.on('change', this.onBaIntProcessingCategoryFoodHandler);

    // Sub Food Category
    $subFoodCategory.each(function() {
      const $subFoodCategoryChecks = $(this).find('input');
      $subFoodCategoryChecks.each(function() {
        $(this).on('change', self.onBaIntSubCategoryFoodHandler);
      });
    });
    $baIntServices.on('change', this.onBaIntServicesHandler);
    $businessInterest.on('change', this.onBusinessInterestChangeHandler);
    $businessEnqNeed.on('change', this.onBusinessEnqNeedHandler);
    $newRequestBtn.on('click', this.newRequestHanlder);

    $nextbtn.click(function (e) {
      let isvalid = true;
      const target = $(this).attr('data-target'),  tab = $(this).closest('.tab-content-steps'), input = tab.find('input'), textarea = tab.find('textarea'), errObj = [];
      if ($(this).hasClass('previousbtn')) {
        switch (target) {
        case '#bef-step-1':
          changeStepPrev(self.mainHead, 'Step 2', self.step2head, self.cache.requestPayload['purposeOfInterestAreaEqTitle']);
          break;
        case '#businessInquiry_packaging':
          changeStepPrev(self.mainHead, 'Step 2', self.step2head, self.cache.requestPayload['purposeOfInterestAreaEqTitle']);
          break;
        case '#businessInquiry_processing':
          changeStepPrev(self.mainHead, 'Step 2', self.step2head, self.cache.requestPayload['purposeOfInterestAreaEqTitle']);
          break;
        case '#businessInquiry_services':
          changeStepPrev(self.mainHead, 'Step 2', self.step2head, self.cache.requestPayload['purposeOfInterestAreaEqTitle']);
          break;
        case '#bef-step-2':
          changeStepPrev(self.mainHead, 'Step 3', self.step3head, self.cache.requestPayload['purposeOfInterestAreaEqTitle']);
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
            requestPayload[fieldName] = newSafeValues;
          }
          if (($(this).prop('required') && $(this).val() === '') || self.validateField(requestPayload[fieldName]) || (fieldName === 'emailBef') && !self.validEmail($(this).val())) {
            isvalid = false;
            e.preventDefault();
            e.stopPropagation();
            const errmsg = $(this).closest('.form-group, .formfield').find('.errorMsg').text().trim(), fieldName = $(this).attr('name');
            let erLbl = '';
            switch (fieldName) {
            case 'purposeOfContactInBusinessEqTitle':
              erLbl = self.step1head;
              break;
            case 'purposeOfInterestAreaEqTitle':
              erLbl = self.step1head2;
              break;
            case 'message':
              erLbl = $('#bef-step-2 label')[3].textContent;
              break;
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
            case 'country':
              erLbl = $('#bef-step-3 label')[5].textContent;
              break;
            case 'company':
              erLbl = $('#bef-step-3 label')[6].textContent;
              break;
            case 'position':
              erLbl = $('#bef-step-3 label')[7].textContent;
              break;
            case 'function':
              erLbl = $('#bef-step-3 label')[8].textContent;
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
          if (!$(this).hasClass('previousbtn')) {
            switch (target) {
            case '#businessInquiry_packaging':
              changeStepNext(self.mainHead, 'Step 1', self.step1head2, self.cache.requestPayload['purposeOfInterestAreaEqTitle'], { [self.step1head2]: self.cache.requestPayload['purposeOfContactInBusinessEqTitle'] });
              break;
            case '#businessInquiry_processing':
              changeStepNext(self.mainHead, 'Step 1', self.step1head2, self.cache.requestPayload['purposeOfInterestAreaEqTitle'], { [self.step1head2]: self.cache.requestPayload['purposeOfContactInBusinessEqTitle'] });
              break;
            case '#businessInquiry_services':
              changeStepNext(self.mainHead, 'Step 1', self.step1head2, self.cache.requestPayload['purposeOfInterestAreaEqTitle'], { [self.step1head2]: self.cache.requestPayload['purposeOfContactInBusinessEqTitle'] });
              break;
            case '#bef-step-2':
              changeStepNext(self.mainHead, 'Step 1', self.step1head, self.cache.requestPayload['purposeOfInterestAreaEqTitle'], { [self.step1head]: self.cache.requestPayload['purposeOfContactInBusinessEqTitle'] });
              break;
            case '#bef-step-3':
              changeStepNext(self.mainHead, 'Step 2', self.step2head, self.cache.requestPayload['purposeOfInterestAreaEqTitle'], { [self.step2head]: self.cache.requestPayload['purposeOfInterestAreaEqTitle'] });
              break;
            default:
              break;
            }
          }
        }
      } else {
        switch (target) {
        case '#businessInquiry_packaging':
          changeStepError(self.mainHead, 'Step 1', self.step1head2, self.cache.requestPayload['purposeOfInterestAreaEqTitle'], {}, errObj);
          break;
        case '#businessInquiry_processing':
          changeStepError(self.mainHead, 'Step 1', self.step1head2, self.cache.requestPayload['purposeOfInterestAreaEqTitle'], {}, errObj);
          break;
        case '#businessInquiry_services':
          changeStepError(self.mainHead, 'Step 1', self.step1head2, self.cache.requestPayload['purposeOfInterestAreaEqTitle'], {}, errObj);
          break;
        case '#bef-step-2':
          changeStepError(self.mainHead, 'Step 1', self.step1head, self.cache.requestPayload['purposeOfInterestAreaEqTitle'], {}, errObj);
          break;
        case '#bef-step-3':
          changeStepError(self.mainHead, 'Step 2', self.step2head, self.cache.requestPayload['purposeOfInterestAreaEqTitle'], {}, errObj);
          break;
        case '#bef-step-final':
          changeStepError(self.mainHead, 'Step 3', self.step3head, self.cache.requestPayload['purposeOfInterestAreaEqTitle'], {}, errObj);
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
            requestPayload[fieldName] = newSafeValues;
          }
          if($(this).attr('type') === 'checkbox' && $(this).attr('name') === 'consent'){
            requestPayload[fieldName] = $('input[name="consent"]:checked').length > 0;
          }
          if (($(this).prop('required') && $(this).val() === '') || (fieldName === 'emailBef') && !self.validEmail($(this).val()) && !self.validEmail($(this).val())) {
            isvalid = false;
            e.preventDefault();
            e.stopPropagation();
            const errmsg = $(this).closest('.form-group, .formfield').find('.errorMsg').text().trim();
            const fieldName = $(this).attr('name');
            let erLbl = '';
            switch (fieldName) {
            case 'purposeOfContactInBusinessEqTitle':
              erLbl = self.step1head;
              break;
            case 'purposeOfInterestAreaEqTitle':
              erLbl = self.step1head2;
              break;
            case 'firstNameField':
              erLbl = $('#bef-step-3 label')[0].textContent;
              break;
            case 'lastNameField':
              erLbl = $('#bef-step-3 label')[1].textContent;
              break;
            case 'emailBef':
              erLbl = $('#bef-step-3 label')[2].textContent;
              break;
            case 'company':
              erLbl = $('#bef-step-3 label')[3].textContent;
              break;
            case 'position':
              erLbl = $('#bef-step-3 label')[4].textContent;
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
          changeStepError(self.mainHead, 'Step 3', self.step5head, self.cache.requestPayload['purposeOfInterestAreaEqTitle'], {}, errObj);
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
      const countryTitle = $(this).data('countrytitle');
      const parentDrop = $(this).closest('.dropdown');
      $('.dropdown-toggle span', parentDrop).text(countryTitle);
      $('input', parentDrop).val(countryTitle);
      requestPayload['country'] = countryTitle;
      self.restObj[self.cache.$countryField.data('country-name-label')] = requestPayload['country'];
      requestPayload['countryTitle'] = countryTitle;
      $dropItem.removeClass('active');
      $(this).addClass('active');
    });

    $positionDropItem.click(function (e) {
      e.preventDefault();
      const positionTitle = $(this).data('positiontitle');
      const positionKey = $(this).data('positionkey');
      const parentDrop = $(this).closest('.dropdown');
      $('.dropdown-toggle span', parentDrop).text(positionTitle);
      $('input', parentDrop).val(positionKey);
      requestPayload['position'] = positionKey;
      self.restObj2[self.cache.$positionField.data('position-name-label')] = positionTitle;
      $positionDropItem.removeClass('active');
      $(this).addClass('active');
    });

    $roleDropItem.click(function (e) {
      e.preventDefault();
      const roleTitle = $(this).data('roletitle');
      const roleKey = $(this).data('rolekey');
      const parentDrop = $(this).closest('.dropdown');
      $('.dropdown-toggle span', parentDrop).text(roleTitle);
      $('input', parentDrop).val(roleKey);
      if(roleKey === 'other') {
        requestPayload['businessEnquiryProfileOther'] = roleKey; 
      } else {
        requestPayload['businessEnquiryProfile'] = roleKey;
      }
      self.restObj2[self.cache.$roleField.data('role-name-label')] = roleTitle;
      $roleDropItem.removeClass('active');
      $(this).addClass('active');
    });

    $functionDropItem.click(function (e) {
      e.preventDefault();
      const functionTitle = $(this).data('functiontitle');
      const functionKey = $(this).data('functionkey');
      const parentDrop = $(this).closest('.dropdown');
      $('.dropdown-toggle span', parentDrop).text(functionTitle);
      $('input', parentDrop).val(functionKey);
      requestPayload['function'] = functionKey;
      self.restObj2[self.cache.$functionField.data('function-name-label')] = functionTitle;
      $functionDropItem.removeClass('active');
      $(this).addClass('active');
    });
  }

  analyticsFormstart(stepHead, mainHead) {
    const self = this;
    const formElements = $(this.cache.$formInfo).find('input, button, select, textarea');
    formElements.each(function(i, val) {
      $(val).on('click', function () {
        if (!self.cache.$isFormStart) {
          self.cache.$isFormStart = true;
          makeLoad(stepHead, mainHead);
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
    $('#bef-step-3 label:not(.country-value)').each((i, v) => this.restObj[$(v).text()] = 'NA');
    $('#bef-step-3 label').slice(0, 1).each((i, v) => this.restObj2[$(v).text()] = 'NA');
    this.getCountryList();
    this.analyticsFormstart(this.step1head, this.mainHead);
  }
}

export default Businessinquiryform;