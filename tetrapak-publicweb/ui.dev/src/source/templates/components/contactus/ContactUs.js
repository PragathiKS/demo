import $ from 'jquery';
import 'bootstrap';

import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { ajaxMethods, API_CONTACTUS_FORM, REG_EMAIL } from '../../../scripts/utils/constants';
import keyDownSearch from '../../../scripts/utils/searchDropDown';

class ContactUs {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    /* Initialize selector cache here */
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
    ajaxWrapper.getXhrObj({
      url: API_CONTACTUS_FORM,
      method: ajaxMethods.POST,
      data: { 'inputJson': JSON.stringify(this.cache.requestPayload) }
    }).done(
      (response) => {
        if (response.statusCode === '200') {
          $('.tab-pane', this.root).removeClass('active');
          $('#cf-step-final', this.root).addClass('active');
          $('.serviceError').removeClass('d-block');
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
      const tab = $(this).closest('.tab-content-steps');
      const input = tab.find('input');
      const textarea = tab.find('textarea');
      if (!$(this).hasClass('previousbtn') && (input.length > 0 || textarea.length > 0)) {
        $('input, textarea', tab).each(function () {
          const fieldName = $(this).attr('name');
          $('div.' + fieldName).text($(this).val());
          if (fieldName in self.cache.requestPayload) {
            requestPayload[fieldName] = $(this).val();
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
      }
      if (isvalid) {
        tab.find('.form-group, .formfield').removeClass('field-error');
        if (target) {
          $('.tab-pane').removeClass('active');
          $(target).addClass('active');
        }
      }
    });

    $submitBtn.click(function (e) {
      e.preventDefault();
      e.stopPropagation();
      let isvalid = true;
      const honeyPotFieldValue = $('#pardot_extra_field', self.root).val();
      requestPayload['message'] = $('[name="message"]').val();
      $('input, textarea').each(function () {
        if ($(this).prop('required') && $(this).val() === '') {
          isvalid = false;
          $(this).closest('.form-group, .formfield').addClass('field-error');
        }
      });
      if (isvalid && !honeyPotFieldValue) {
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
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.getCountryList();
  }
}

export default ContactUs;
