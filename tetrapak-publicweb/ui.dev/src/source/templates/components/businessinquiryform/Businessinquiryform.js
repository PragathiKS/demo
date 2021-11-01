import $ from 'jquery';
import 'bootstrap';
import keyDownSearch from '../../../scripts/utils/searchDropDown';
import { makeLoad, changeStepNext, loadThankYou, changeStepPrev, changeStepError, newPage } from './businessinquiryform.analytics.js';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { ajaxMethods, REG_EMAIL, REG_NUM } from '../../../scripts/utils/constants';
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
      'pardot_extra_field': '',
      'pageurl': window.location.href
    };
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
    
    dataObj['language'] = langCode;
    dataObj['site'] = countryCode;
    if(this.root.find(`#befconsentcheckbox`).is(':checked')){
      dataObj['marketingConsent'] = 'True';
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
    $('input[type=hidden][name="purposeOfInterestAreaEqTitle"]').val(val);
    this.resetBusinessIntFields();
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
  }
  
  onBaIntPackagingHandler = e => {
    const { requestPayload } = this.cache;
    const id = e.target.id;
    const value = e.target.value;
    const labelValue = $('label[for="'+id+'"]').text().trim();
    $('input[type=hidden][name="specificInterestAreaPackagingEqTitle"]').val(labelValue);
    requestPayload['businessAreaInterestPackaging'] = value;
  }

  onBaIntProcessingSupportHandler = e => {
    const { requestPayload } = this.cache;
    const id = e.target.id;
    const value = e.target.value;
    const labelValue = $('label[for="'+id+'"]').text().trim();
    $('input[type=hidden][name="businessAreaInterestProcessingSupportEqTitle"]').val(labelValue);
    requestPayload['businessAreaInterestProcessingSupport'] = value;
  }

  onBaIntProcessingCategoryFoodHandler = e => {
    const self = this;
    const { requestPayload } = this.cache;
    const id = e.target.id;
    const value = e.target.value;
    const labelValue = $('label[for="'+id+'"]').text().trim();

    // Hide and Reset Sub food Categories
    self.resetSubFoodCategory();
    
    $(e.currentTarget).parent().next('.subFoodData').show().find('.form-control.field-handler').attr('required', true);
    $('input[type=hidden][name="businessAreaProcessingCategoryFoodEqtitle"]').val(labelValue);
    requestPayload['businessAreaProcessingCategoryFood'] = value;
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
  }

  onBaIntServicesHandler = e => {
    const { requestPayload } = this.cache;
    const id = e.target.id;
    const value = e.target.value;
    const labelValue = $('label[for="'+id+'"]').text().trim();
    $('input[type=hidden][name="businessAreaInterestServicesEqTitle"]').val(labelValue);
    requestPayload['businessAreaInterestServices'] = value;
  }
  
  onBusinessEnqNeedHandler = e => {
    const { requestPayload } = this.cache;
    const id = e.target.id;
    const value = e.target.value;
    const labelValue = $('label[for="'+id+'"]').text().trim();
    $('input[type=hidden][name="businessEnquiryNeedEqTitle"]').val(labelValue);
    requestPayload['businessEnquiryNeed'] = value;
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
    $businessInterest.on('change', this.onBusinessInterestChangeHandler);
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
            if(fieldName === 'businessEnquiryProfile') {
              requestPayload[fieldName] = self.cache.requestPayload[fieldName];
            } else if(fieldName === 'businessEnquiryProfileOther') {
              requestPayload[fieldName] = self.cache.requestPayload[fieldName];
            } else {
              requestPayload[fieldName] = newSafeValues;
            }
          }
          
          if (($(this).prop('required') && $(this).val() === '') || self.validateField(requestPayload[fieldName]) || ((fieldName === 'email') && !self.validEmail($(this).val())) || (fieldName === 'phone') && !self.validPhone($(this).val())) {
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
            if(fieldName === 'position') {
              requestPayload[fieldName] = self.cache.requestPayload[fieldName];
            } else if(fieldName === 'function') {
              requestPayload[fieldName] = self.cache.requestPayload[fieldName];
            } else {
              requestPayload[fieldName] = newSafeValues;
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
      requestPayload['position'] = positionTitle;
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
        requestPayload['businessEnquiryProfileOther'] = roleTitle;
        $('input', parentDrop).attr('name', 'businessEnquiryProfileOther');
      } else {
        requestPayload['businessEnquiryProfile'] = roleTitle;
        $('input', parentDrop).attr('name', 'businessEnquiryProfile');
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
      requestPayload['function'] = functionTitle;
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