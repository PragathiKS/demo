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
    this.cache.$radioListFirst = this.root.find('input[type=radio][name="purposeOfContactOptionsInBusinessEq"]');
    this.cache.$radioListSecond = this.root.find('input[type=radio][name="purposeOfContactOptionsInInterestArea"]');
    this.cache.$newRequestBtn = $('.newRequestBtn', this.root);
    this.cache.$submitBtn = $('form.pw-form-businessEnquiry button[type="submit"]', this.root);
    this.cache.$inputText = $('form.pw-form-businessEnquiry  input[type="text"]', this.root);
    this.cache.$inputEmail = $('form.pw-form-businessEnquiry  input[type="email"]', this.root);
    this.cache.$dropItem = $('.pw-form__dropdown a.dropdown-item', this.root);
    this.cache.countryList = [];
    this.cache.$countryField = this.root.find('.formfield.country-field');

    this.cache.requestPayload = {
      'domainURL': window.location.host,
      'purposeOfContact': '',
      'interestArea': '',
      'firstNameField': '',
      'lastNameField': '',
      'emailBef': '',
      'message': '',
      'phoneField': '',
      'purposeOfContactInBusinessEqTitle': '',
      'purposeOfInterestAreaEqTitle': '',
      'company': '',
      'position': '',
      'country': '',
      'countryTitle': ''
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
    const self = this;
    const servletPath = this.cache.businessformapi.data('bef-api-servlet');
    const countryCode = this.cache.businessformapi.data('bef-countrycode');
    const langCode = this.cache.businessformapi.data('bef-langcode');
    const dataObj = {};
    dataObj['purpose'] = this.cache.requestPayload.purposeOfContactInBusinessEqTitle;
    dataObj['businessArea'] = this.cache.requestPayload.purposeOfInterestAreaEqTitle;
    dataObj['firstName'] = this.cache.requestPayload.firstNameField;
    dataObj['lastName'] = this.cache.requestPayload.lastNameField;
    dataObj['email'] = this.cache.requestPayload.emailBef;
    if(this.cache.requestPayload.phoneField.trim()){
      dataObj['phoneNumber'] = this.cache.requestPayload.phoneField;
    }
    dataObj['company'] = this.cache.requestPayload.company;
    dataObj['position'] = this.cache.requestPayload.position;
    dataObj['country'] = this.cache.requestPayload.country;
    dataObj['countryTitle'] = this.cache.requestPayload.countryTitle;
    dataObj['language'] = langCode;
    dataObj['site'] = countryCode;
    if(this.root.find(`#befconsentcheckbox`).is(':checked')){
      dataObj['marketingConsent'] = this.root.find(`#befconsentcheckbox`).is(':checked');
    }
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
    const labelValue = $('label[for="'+id+'"]').text().trim();
    $('input[type=hidden][name="purposeOfContactInBusinessEqTitle"]').val(value);
    requestPayload['purposeOfContact'] = id;
    requestPayload['purposeOfContactTitle'] = labelValue;
  }

  onRadioChangeHandlerSecond = e => {
    const { requestPayload } = this.cache;
    const value = e.target.value;
    const id = e.target.id;
    const labelValue = $('label[for="'+id+'"]').text().trim();
    $('input[type=hidden][name="purposeOfInterestAreaEqTitle"]').val(value);
    requestPayload['areaOfInterest'] = id;
    requestPayload['areaOfInterestTitle'] = labelValue;
  }


  bindEvents() {
    const { requestPayload, $radioListFirst, $radioListSecond, $newRequestBtn, $nextbtn, $submitBtn, $dropItem } = this.cache;
    const self = this;
    $radioListFirst.on('change', this.onRadioChangeHandlerFirst);
    $radioListSecond.on('change', this.onRadioChangeHandlerSecond);
    $newRequestBtn.on('click', this.newRequestHanlder);

    $nextbtn.click(function (e) {
      let isvalid = true;
      const target = $(this).data('target'),  tab = $(this).closest('.tab-content-steps'), input = tab.find('input'), textarea = tab.find('textarea'), errObj = [];
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
          if (($(this).prop('required') && $(this).val() === '') || self.validateField(requestPayload[fieldName]) || (fieldName === 'emailBef') && !self.validEmail($(this).val()) || (fieldName === 'consent') && $(this).prop('checked')) {
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
              erLbl = self.step2head;
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
        if (!(self.cache.requestPayload['phoneField']).length > 0) {
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
      const errObj = [], target = $(this).data('target'), tab = $(this).closest('.tab-content-steps'), input = tab.find('input'), textarea = tab.find('textarea');

      if (!$(this).hasClass('previousbtn') && (input.length > 0 || textarea.length > 0)) {
        $('input, textarea', tab).each(function () {
          const fieldName = $(this).attr('name');
          const newSafeValues = $(this).attr('type') !== 'hidden' ? validateFieldsForTags($(this).val()) : $(this).val();

          $('div.' + fieldName).text($(this).val());
          if (fieldName in self.cache.requestPayload) {
            requestPayload[fieldName] = newSafeValues;
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
              erLbl = self.step2head;
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
    this.mainHead = $($('.pw-businessEnquiry-form .main-heading').find('h2')[0]).text().trim();
    this.restObj = {};
    this.restObj2 = {};
    this.linkTitle = this.root.find('.thankyou').find('h2').text().trim();
    this.linkText = this.root.find('.newRequestBtn').text().trim();
    $('#bef-step-3 label:not(.country-value)').each((i, v) => this.restObj[$(v).text()] = 'NA');
    $('#bef-step-4 label').slice(0, 2).each((i, v) => this.restObj2[$(v).text()] = 'NA');
    makeLoad(this.step1head, this.mainHead);
    this.getCountryList();
  }
}

export default Businessinquiryform;
