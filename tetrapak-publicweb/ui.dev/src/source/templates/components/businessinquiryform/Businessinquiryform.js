import $ from 'jquery';
import 'bootstrap';
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
    newPage(this.linkTitle, this.linkText);
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
    dataObj['marketingConsent'] = this.root.find(`#consentcheckbox`).is(':checked');
    dataObj['pardot_extra_field'] = this.cache.requestPayload.pardot_extra_field;
    loadThankYou(self.mainHead, self.cache.requestPayload['purposeOfInterestAreaEqTitle'], { ...self.restObj2, 'Marketing Consent': 'Checked' });
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
          changeStepPrev(self.mainHead, 'Step 2', self.step2head, self.cache.requestPayload['purposeOfInterestAreaEqTitle']);
          break;
        case '#bef-step-2':
          changeStepPrev(self.mainHead, 'Step 3', self.step3head, self.cache.requestPayload['purposeOfInterestAreaEqTitle']);
          break;
        case '#bef-step-3':
          changeStepPrev(self.mainHead, 'Step 4', self.step4head, self.cache.requestPayload['purposeOfInterestAreaEqTitle']);
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
            const errmsg = $(this).closest('.form-group, .formfield').find('.errorMsg').text().trim();
            const fieldName = $(this).attr('name');
            let erLbl = '';
            switch (fieldName) {
            case 'purposeOfContactInBusinessEqTitle':
              erLbl = self.step1head;
              break;
            case 'purposeOfInterestAreaEqTitle':
              erLbl = self.step2head;
              break;
            case 'firstName':
              erLbl = $('#bef-step-3 label')[0].textContent;
              break;
            case 'lastName':
              erLbl = $('#bef-step-3 label')[1].textContent;
              break;
            case 'email':
              erLbl = $('#bef-step-3 label')[2].textContent;
              break;
            case 'company':
              erLbl = $('#bef-step-4 label')[0].textContent;
              break;
            case 'position':
              erLbl = $('#bef-step-4 label')[1].textContent;
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
            case '#bef-step-2':
              changeStepNext(self.mainHead, 'Step 1', self.step1head, self.cache.requestPayload['purposeOfInterestAreaEqTitle'], { [self.step1head]: self.cache.requestPayload['purposeOfContactInBusinessEqTitle'] });
              break;
            case '#bef-step-3':
              changeStepNext(self.mainHead, 'Step 2', self.step2head, self.cache.requestPayload['purposeOfInterestAreaEqTitle'], { [self.step2head]: self.cache.requestPayload['purposeOfInterestAreaEqTitle'] });
              break;
            case '#bef-step-4':
              changeStepNext(self.mainHead, 'Step 3', self.step3head, self.cache.requestPayload['purposeOfInterestAreaEqTitle'], { ...self.restObj });
              break;
            default:
              break;
            }
          }
        }
      } else {
        switch (target) {
        case '#bef-step-2':
          changeStepError(self.mainHead, 'Step 1', self.step1head, self.cache.requestPayload['purposeOfInterestAreaEqTitle'], {}, errObj);
          break;
        case '#bef-step-3':
          changeStepError(self.mainHead, 'Step 2', self.step2head, self.cache.requestPayload['purposeOfInterestAreaEqTitle'], {}, errObj);
          break;
        case '#bef-step-4':
          changeStepError(self.mainHead, 'Step 3', self.step3head, self.cache.requestPayload['purposeOfInterestAreaEqTitle'], {}, errObj);
          break;
        case '#bef-step-final':
          changeStepError(self.mainHead, 'Step 4', self.step4head, self.cache.requestPayload['purposeOfInterestAreaEqTitle'], {}, errObj);
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
      const errObj = [];
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
          if (($(this).prop('required') && $(this).val() === '') || (fieldName === 'email') && !self.validEmail($(this).val()) && !self.validEmail($(this).val())) {
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
              erLbl = self.step2head;
              break;
            case 'firstName':
              erLbl = $('#bef-step-3 label')[0].textContent;
              break;
            case 'lastName':
              erLbl = $('#bef-step-3 label')[1].textContent;
              break;
            case 'email':
              erLbl = $('#bef-step-3 label')[2].textContent;
              break;
            case 'company':
              erLbl = $('#bef-step-4 label')[0].textContent;
              break;
            case 'position':
              erLbl = $('#bef-step-4 label')[1].textContent;
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
          changeStepError(self.mainHead, 'Step 4', self.step4head, self.cache.requestPayload['purposeOfInterestAreaEqTitle'], {}, errObj);
          break;
        default:
          break;
        }
      }
      if (isvalid) {
        self.submitForm();
      }
    });

  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.purposeLabel = $('.purposeOfBusinessContactError').text().trim();
    this.interestLabel = $('.purposeOfBusinessIntersetError').text().trim();
    this.step1head = $('#bef-step-1').find('h4').text().trim();
    this.step2head = $('#bef-step-2 .tab-content-steps').find('h4').text();
    this.step3head = $('#bef-step-3 .tab-content-steps').find('h4').text();
    this.step4head = $('#bef-step-4 .tab-content-steps').find('h4').text();
    this.mainHead = String($('.pw-businessEnquiry-form .main-heading').find('h2')[0].textContent).trim();
    this.restObj = {};
    this.restObj2 = {};
    this.linkTitle = this.root.find('.thankyou').find('h2').text().trim();
    this.linkText = this.root.find('.newRequestBtn').text();
    $('#bef-step-3 label').each((i, v) => this.restObj[$(v).text()] = 'NA');
    $('#bef-step-4 label').slice(0, 2).each((i, v) => this.restObj2[$(v).text()] = 'NA');
    makeLoad(this.step1head, this.mainHead);
  }
}

export default Businessinquiryform;
