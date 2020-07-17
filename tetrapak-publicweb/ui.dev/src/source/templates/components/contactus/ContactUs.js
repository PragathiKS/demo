import $ from 'jquery';
import 'bootstrap';

import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { ajaxMethods, REG_EMAIL } from '../../../scripts/utils/constants';
import keyDownSearch from '../../../scripts/utils/searchDropDown';
import { validateFieldsForTags } from '../../../scripts/common/common';
import { onErrorAnalytics, newRequestButtonAnalytics, onNextClickAnalytics, onPreviousClickAnalytics, onLoadTrackAnalytics, onSubmitClickAnalytics } from './contactUs.analytics';

class ContactUs {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    /* Initialize selector cache here */
    this.cache.contactusapi = this.root.find('form.pw-form-contactUs');
    this.cache.$newRequestBtn = $('.newRequestBtn', this.root);
    this.cache.$submitBtn = $('form.pw-form-contactUs button[type="submit"]', this.root);
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
    newRequestButtonAnalytics(e);
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
      } else if($(this).hasClass('previousbtn')) {
        onPreviousClickAnalytics(currentTarget,tab);
      }
      if (isvalid) {
        tab.find('.form-group, .formfield').removeClass('field-error');
        if (target) {
          $('.tab-pane').removeClass('active');
          $(target).addClass('active');
          if(!$(this).hasClass('previousbtn')){
            onNextClickAnalytics(currentTarget,tab,requestPayload);
          }

        }
      } else {
        onErrorAnalytics(currentTarget,tab);
      }
    });

    $submitBtn.click(function (e) {
      e.preventDefault();
      e.stopPropagation();
      let isvalid = true;
      const tab = $(this).closest('.tab-content-steps');
      const honeyPotFieldValue = $('#pardot_extra_field', self.root).val();
      requestPayload['message'] = validateFieldsForTags($('[name="message"]').val());
      $('input, textarea', tab).each(function () {
        if ($(this).prop('required') && $(this).val() === '') {
          isvalid = false;
          $(this).closest('.form-group, .formfield').addClass('field-error');
          onErrorAnalytics('cf-step-3',tab);
        }
      });
      if (isvalid && !honeyPotFieldValue) {
        onSubmitClickAnalytics(tab);
        self.submitForm();

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

    onLoadTrackAnalytics();
  }

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.getCountryList();
  }
}

export default ContactUs;
