import $ from 'jquery';
import 'bootstrap';

import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { ajaxMethods, REG_EMAIL } from '../../../scripts/utils/constants';
import keyDownSearch from '../../../scripts/utils/searchDropDown';
import { validateFieldsForTags } from '../../../scripts/common/common';
import { trackAnalytics } from '../../../scripts/utils/analytics';

class ContactUs {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    /* Initialize selector cache here */
    this.cache.contactusapi = this.root.find('form.pw-form-contactUs');
    this.cache.$newRequestBtn = $('.newRequestBtn', this.root);
    this.cache.$submitBtn = $('button[type="submit"]', this.root);
    this.cache.$nextbtn = this.root.find('.tpatom-btn[type=button]');
    this.cache.$radio = this.root.find('input[type=radio][name="purposeOfContactOptions"]');
    this.cache.$dropItem = $('.pw-form__dropdown a.dropdown-item', this.root);
    this.cache.countryList = [];
    this.cache.requestPayload = {
      'domainURL': window.location.host,
      'purposeOfContact': '',
      'country': '',
      'firstName': '',
      'lastName': '',
      'email': '',
      'message': '',
      'countryTitle': '',
      'purposeOfContactTitle': ''
    };
  }

  validEmail(email) {
    return REG_EMAIL.test(email);
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

  submitForm = () => {
    const servletPath = this.cache.contactusapi.data('contactus-api-servlet');
    ajaxWrapper.getXhrObj({
      url: servletPath,
      method: ajaxMethods.POST,
      data: { 'inputJson': JSON.stringify(this.cache.requestPayload) }
    }).done(
      (response) => {
        if (response.statusCode === '200') {
          const offsetContact = $('#pw-contactUs').offset();
          $('.tab-pane', this.root).removeClass('active');
          $('#cf-step-final', this.root).addClass('active');
          $('.serviceError').removeClass('d-block');
          $('html, body').animate({
            scrollTop: offsetContact.top - 50
          });
        } else {
          $('.serviceError').addClass('d-block');
        }
      }
    );
  }

  newRequestHanlder = e => {
    e.preventDefault();
    e.stopPropagation();
    location.reload();
  }

  onRadioChangeHandler = e => {
    const { requestPayload } = this.cache;
    const value = e.target.value;
    const id = e.target.id;
    $('input[type=hidden][name="purposeOfContactTitle"]').val(value);
    $('div.purposeOfContactTitle').text(value);
    requestPayload['purposeOfContact'] = id;
    requestPayload['purposeOfContactTitle'] = value;
  }

  bindEvents() {
    /* Bind jQuery events here */
    const { requestPayload, $radio, $newRequestBtn, $nextbtn, $submitBtn, $dropItem } = this.cache;
    const self = this;
    $newRequestBtn.on('click', this.newRequestHanlder);
    $radio.on('change', this.onRadioChangeHandler);

    $nextbtn.click(function (e) {
      let isvalid = true;
      const target = $(this).data('target');
      const currentTarget = $(this).data('current-target');
      const tab = $(this).closest('.tab-content-steps');
      const input = tab.find('input');
      const textarea = tab.find('textarea');
      if (!$(this).hasClass('previousbtn') && (input.length > 0 || textarea.length > 0)) {
        $('.validateForTags', tab).each(function () {
          const fieldName = $(this).attr('name');
          const newSafeValues = $(this).attr('type') !== 'hidden' ? validateFieldsForTags($(this).val()) : $(this).val();
          $('div.' + fieldName).text(newSafeValues);
          if (fieldName in self.cache.requestPayload) {
            requestPayload[fieldName] = newSafeValues;
          }
          if (($(this).prop('required') && $(this).val() === '') || (fieldName === 'email') && !self.validEmail($(this).val())) {
            isvalid = false;
            e.preventDefault();
            e.stopPropagation();
            $(this).closest('.form-group, .formfield').addClass('field-error');
          } else {
            $(this).closest('.form-group, .formfield').removeClass('field-error');
          }
        });
      } else {
        self.onPreviousClickAnalytics(currentTarget,tab);
      }
      if (isvalid) {
        tab.find('.form-group, .formfield').removeClass('field-error');
        if (target) {
          $('.tab-pane').removeClass('active');
          $(target).addClass('active');
          if(!$(this).hasClass('previousbtn')){
            self.onNextClickAnalytics(currentTarget,tab);
          }

        }
      }
    });

    $submitBtn.click(function (e) {
      e.preventDefault();
      e.stopPropagation();
      let isvalid = true;
      const honeyPotFieldValue = $('#pardot_extra_field', self.root).val();
      requestPayload['message'] = validateFieldsForTags($('[name="message"]').val());
      $('input, textarea').each(function () {
        if ($(this).prop('required') && $(this).val() === '') {
          isvalid = false;
          $(this).closest('.form-group, .formfield').addClass('field-error');
        }
      });
      if (isvalid && !honeyPotFieldValue) {
        self.submitForm();
        self.onSubmitClickAnalytics();
      }
    });

    $dropItem.click(function (e) {
      e.preventDefault();
      const country = $(this).data('country');
      const countryTitle = $(this).data('countrytitle');
      const parentDrop = $(this).closest('.dropdown');
      $('.dropdown-toggle span', parentDrop).text(countryTitle);
      $('input', parentDrop).val(countryTitle);
      requestPayload['country'] = country;
      requestPayload['countryTitle'] = countryTitle;
      $dropItem.removeClass('active');
      $(this).addClass('active');
    });

    this.onLoadTrackAnalytics();
  }

  onNextClickAnalytics = (currentTarget,tab) => {
    const { requestPayload } = this.cache;
    const formType = tab.find('.form-field-heading').data('step-heading');
    const formField = [];
    let event = {
      eventType : 'step 1 next'
    };
    if(currentTarget === 'cf-step-1'){
      formField.push({formFieldName:tab.find('.form-field-heading').data('step-heading'), formFieldValue:requestPayload['purposeOfContact']});
    } else if(currentTarget === 'cf-step-2'){
      formField.push(
        {formFieldName:tab.find('.first-name').data('first-name-label'), formFieldValue:'NA'},
        {formFieldName:tab.find('.last-name').data('last-name-label'), formFieldValue:'NA'},
        {formFieldName:tab.find('.email').data('email-name-label'), formFieldValue:'NA'},
        {formFieldName:tab.find('.country').data('country-name-label'), formFieldValue:requestPayload['country']},
      );
      event = {
        eventType : 'step 2 next'
      };
    }
    const form  = {
      formType,
      formField,
      formStep : tab.find('.button').data('form-step')
    };

    this.trackAnalytics(form,event);
  };

  onPreviousClickAnalytics = (currentTarget,tab) => {
    const formType = tab.find('.form-field-heading').data('step-heading');
    const formField = [];
    let event = {
      eventType : 'step 2 previous'
    };
    if(currentTarget === 'cf-step-3'){
      event = {
        eventType : 'step 3 previous'
      };
    }
    const form  = {
      formType,
      formField,
      formStep : tab.find('.button').data('form-step')
    };

    this.trackAnalytics(form,event);
  }

  onLoadTrackAnalytics = () => {
    const form = {
      formType:$('.js-form-field-heading-step1').data('step1-heading'),
      formStep:'step 1',
      formField: []
    };
    const event = {
      eventType : 'formStart'
    };
    this.trackAnalytics(form,event);
  };

  onSubmitClickAnalytics = () => {
    const form = {
      formType:'ThankYou',
      formStep:'step 3',
      formField: []
    };
    const event = {
      eventType : 'formComplete'
    };
    this.trackAnalytics(form,event);
  }

  trackAnalytics = (formTrackingObj,eventObj) => {

    formTrackingObj = {
      formName:$('.js-step1-main-heading').data('form-name'),
      ...formTrackingObj
    };

    eventObj = {
      ...eventObj,
      event:'Contact Us'
    };
    trackAnalytics(
      formTrackingObj,
      'form',
      'formClick',
      undefined,
      false,
      eventObj,
    );
  }

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.getCountryList();
  }
}

export default ContactUs;
