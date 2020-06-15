import $ from 'jquery';
import 'bootstrap';
import { makeLoad, changeStepNext, loadThankYou, changeStepPrev, changeStepError } from './businessinquiryform.analytics.js';
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
    this.cache.businessformapi = this.root.find('form.pw-form-businessEnquiry');
    this.cache.$nextbtn = this.root.find('.pw-businessEnquiry-form .tpatom-btn[type=button]');
    this.cache.$radioListFirst = this.root.find('input[type=radio][name="purposeOfContactOptionsInBusinessEq"]');
    this.cache.$radioListSecond = this.root.find('input[type=radio][name="purposeOfContactOptionsInInterestArea"]');
    this.cache.$newRequestBtn = $('.newRequestBtn', this.root);
    this.cache.$submitBtn = $('form.pw-form-businessEnquiry button[type="submit"]', this.root);
    this.cache.$inputText = $('form.pw-form-businessEnquiry  input[type="text"]', this.root);
    this.cache.$inputEmail = $('form.pw-form-businessEnquiry  input[type="email"]', this.root);

    this.cache.requestPayload = {
      'domainURL': window.location.host,
      'purposeOfContact': '',
      'interestArea': '',
      'firstName': '',
      'lastName': '',
      'email': '',
      'message': '',
      'phone': '',
      'purposeOfContactInBusinessEqTitle': '',
      'purposeOfInterestAreaEqTitle': '',
      'company': '',
      'position': ''
    };
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

  newRequestHanlder = e => {
    e.preventDefault();
    e.stopPropagation();
    loadThankYou(this.cache.requestPayload['purposeOfInterestAreaEqTitle'], this.cache.requestPayload);
    location.reload();
  }

  submitForm = () => {
    const self = this;
    const servletPath = this.cache.businessformapi.data('bef-api-servlet');
    const countryCode = this.cache.businessformapi.data('bef-countrycode');
    const langCode = this.cache.businessformapi.data('bef-langcode');
    const dataObj = {};
    dataObj['purpose'] = this.cache.requestPayload.purposeOfContactInBusinessEqTitle;
    dataObj['businessArea'] = this.cache.requestPayload.purposeOfInterestAreaEqTitle;
    dataObj['firstName'] = this.cache.requestPayload.firstName;
    dataObj['lastName'] = this.cache.requestPayload.lastName;
    dataObj['email'] = this.cache.requestPayload.email;
    dataObj['phoneNumber'] = this.cache.requestPayload.phone;
    dataObj['company'] = this.cache.requestPayload.company;
    dataObj['position'] = this.cache.requestPayload.position;
    dataObj['language'] = langCode;
    dataObj['site'] = countryCode;
    dataObj['policyConsent'] = true;
    dataObj['pardot_extra_field'] = this.cache.requestPayload.pardot_extra_field;
    changeStepNext('Step 4', 'Company information', self.cache.requestPayload['purposeOfInterestAreaEqTitle'], self.cache.requestPayload);
    window.scrollTo(0, $('.pw-businessEnquiry-form').offset().top);

    ajaxWrapper.getXhrObj({
      url: servletPath,
      method: ajaxMethods.POST,
      data: dataObj
    }).done(
      (response) => {
        if (response.statusCode === '200') {
          $('.bef-tab-pane', this.root).removeClass('active');
          $('#bef-step-final', this.root).addClass('active');
          loadThankYou('ThankYou', 'request a quote', self.cache.requestPayload['purposeOfInterestAreaEqTitle'], self.cache.requestPayload);
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


  onRadioChangeHandlerFirst = e => {
    const { requestPayload } = this.cache;
    const value = e.target.value;
    const id = e.target.id;
    $('input[type=hidden][name="purposeOfContactInBusinessEqTitle"]').val(value);
    requestPayload['purposeOfContact'] = id;
    requestPayload['purposeOfContactTitle'] = value;
  }

  onRadioChangeHandlerSecond = e => {
    const { requestPayload } = this.cache;
    const value = e.target.value;
    const id = e.target.id;
    $('input[type=hidden][name="purposeOfInterestAreaEqTitle"]').val(value);
    requestPayload['areaOfInterest'] = id;
    requestPayload['areaOfInterestTitle'] = value;
  }


  bindEvents() {
    /* Bind jQuery events here */
    const { requestPayload, $radioListFirst, $radioListSecond, $newRequestBtn, $nextbtn, $submitBtn } = this.cache;
    const self = this;
    $radioListFirst.on('change', this.onRadioChangeHandlerFirst);
    $radioListSecond.on('change', this.onRadioChangeHandlerSecond);
    $newRequestBtn.on('click', this.newRequestHanlder);

    $nextbtn.click(function (e) {
      let isvalid = true;
      const target = $(this).data('target');
      const tab = $(this).closest('.tab-content-steps');
      const input = tab.find('input');
      const textarea = tab.find('textarea');
      const errObj = [];
      if ($(this).hasClass('previousbtn')) {
        switch (target) {
        case '#bef-step-1':
          changeStepPrev('Step 1', 'Purpose of Contact', self.cache.requestPayload['purposeOfInterestAreaEqTitle'], self.cache.requestPayload);
          break;
        case '#bef-step-2':
          changeStepPrev('Step 2', 'Business area of interest', self.cache.requestPayload['purposeOfInterestAreaEqTitle'], self.cache.requestPayload);
          break;
        case '#bef-step-3':
          changeStepPrev('Step 3', 'Contact information', self.cache.requestPayload['purposeOfInterestAreaEqTitle'], self.cache.requestPayload);
          break;
        case '#bef-step-4':
          changeStepPrev('Step 4', 'Company information', self.cache.requestPayload['purposeOfInterestAreaEqTitle'], self.cache.requestPayload);
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
          if (($(this).prop('required') && $(this).val() === '') || self.validateField(requestPayload[fieldName]) || (fieldName === 'email') && !self.validEmail($(this).val()) || (fieldName === 'consent') && $(this).prop('checked')) {
            isvalid = false;
            e.preventDefault();
            e.stopPropagation();
            const errmsg = $(this).closest('.form-group, .formfield').find('.errorMsg').text();
            const fieldName = $(this).attr('name');    
            errObj.push({
              formErrorMessage: errmsg,
              formErrorField: fieldName
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

          switch (target) {
          case '#bef-step-2':
            changeStepNext('Step 1', 'Purpose of contact', self.cache.requestPayload['purposeOfInterestAreaEqTitle'], self.cache.requestPayload);
            break;
          case '#bef-step-3':
            changeStepNext('Step 2', 'Business area of interest', self.cache.requestPayload['purposeOfInterestAreaEqTitle'], self.cache.requestPayload);
            break;
          case '#bef-step-4':
            changeStepNext('Step 3', 'Contact information', self.cache.requestPayload['purposeOfInterestAreaEqTitle'], self.cache.requestPayload);
            break;
          default:
            break;
          }
        }
      } else {
        switch (target) {
        case '#bef-step-2':
          changeStepError('Step 1', 'Purpose of contact', self.cache.requestPayload['purposeOfInterestAreaEqTitle'], self.cache.requestPayload, errObj);
          break;
        case '#bef-step-3':
          changeStepError('Step 2', 'Business area of interest', self.cache.requestPayload['purposeOfInterestAreaEqTitle'], self.cache.requestPayload, errObj);
          break;
        case '#bef-step-4':
          changeStepError('Step 3', 'Contact information', self.cache.requestPayload['purposeOfInterestAreaEqTitle'], self.cache.requestPayload, errObj);
          break;
        default:
          break;
        }
      }
    });

    $submitBtn.click(function (e) {
      e.preventDefault();
      e.stopPropagation();
      let isvalid = true;
      const honeyPotFieldValue = $('#pardot_extra_field_bef', self.root).val();
      const target = $(this).data('target');
      const tab = $(this).closest('.tab-content-steps');
      const input = tab.find('input');
      const textarea = tab.find('textarea');
      if (!$(this).hasClass('previousbtn') && (input.length > 0 || textarea.length > 0)) {
        $('input, textarea', tab).each(function () {
          const fieldName = $(this).attr('name');
          const newSafeValues = $(this).attr('type') !== 'hidden' ? validateFieldsForTags($(this).val()) : $(this).val();
      
          $('div.' + fieldName).text($(this).val());
          if (fieldName in self.cache.requestPayload) {
            requestPayload[fieldName] = newSafeValues;
          }
          if (($(this).prop('required') && $(this).val() === '') || (fieldName === 'email') && !self.validEmail($(this).val()) && !self.validEmail($(this).val()) || (fieldName === 'consent') && !$(this).prop('checked')) {
            isvalid = false;
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
          $('.bef-tab-pane').removeClass('active');
          $(target).addClass('active');
        }
      }
      if (isvalid && !honeyPotFieldValue) {
        self.submitForm();
      }
    });

  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    makeLoad();
  }
}

export default Businessinquiryform;
