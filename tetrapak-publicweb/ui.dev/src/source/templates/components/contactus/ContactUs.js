import $ from 'jquery';

import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { ajaxMethods, API_CONTACTUS_FORM, REG_EMAIL } from '../../../scripts/utils/constants';

class ContactUs {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    /* Initialize selector cache here */
    /**
     * Use "this.root" to find elements within current component
     * Example:
     * this.cache.$submitBtn = this.root.find('.js-submit-btn');
     */
    this.cache.$submitBtn = $('button[type="submit"]', this.root);
    this.cache.$nextbtn = this.root.find('.tpatom-btn[type=button]');
    this.cache.$radio = this.root.find('input[type=radio][name="purposeOfContactOptions"]');
    this.cache.$dropItem = $('.pw-form__dropdown a.dropdown-item', this.root);
    this.cache.$dropdown = $('.pw-form__dropdown', this.root);
    this.cache.requestPayload = {
      'purposeOfContact': '',
      'country' : '',
      'firstName': '',
      'lastName': '',
      'email': '',
      'message' : '',
      'countryTitle' : '',
      'purposeOfContactTitle' : ''
    };
  }

  validEmail(email) {
    return REG_EMAIL.test(email);
  }

  submitForm = () => {
    ajaxWrapper.getXhrObj({
      url: API_CONTACTUS_FORM,
      method: ajaxMethods.POST,
      data: {'inputJson': this.cache.requestPayload}
    }).done(
      () => {
        $('.tab-pane' , this.root).removeClass('active');
        $('#cf-step-final', this.root).addClass('active');
      }
    );
  }

  bindEvents() {
    /* Bind jQuery events here */
    const self = this;
    this.cache.$radio.on('change', function() {
      const value = this.value;
      const id = $(this).attr('id');
      $('input[type=hidden][name="purposeOfContact"]').val(value);
      self.cache.requestPayload['purposeOfContact'] = id;
      self.cache.requestPayload['purposeOfContactTitle'] = value;
    });

    this.cache.$nextbtn.click(function (e) {
      let isvalid = true;
      const target = $(this).data('target');
      const tab = $(this).closest('.tab-content-steps');
      const input = tab.find('input');
      const textarea = tab.find('textarea');
      if(!$(this).hasClass('previousbtn') && (input.length > 0 || textarea.length > 0)) {
        $('input, textarea', tab).each(function () {
          const fieldName = $(this).attr('name');
          $('div.' + fieldName).text($(this).val());
          if(fieldName in self.cache.requestPayload) {
            self.cache.requestPayload[fieldName] = $(this).val();
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
      if(isvalid) {
        tab.find('.form-group, .formfield').removeClass('field-error');
        $('.tab-pane').removeClass('active');
        $(target).addClass('active');
      }
    });

    this.cache.$submitBtn.click(function (e) {
      e.preventDefault();
      e.stopPropagation();
      var isvalid = true;
      const honeyPotFieldValue = $('#contactUsHoneyPot', self.root).val();
      $('input, textarea').each(function () {
        if ($(this).prop('required') && $(this).val() === '') {
          isvalid = false;
          $(this).closest('.form-group, .formfield').addClass('field-error');
        }
      });
      if (isvalid  && !honeyPotFieldValue) {
        self.submitForm();   
      }
    });

    this.cache.$dropdown.click(function () {
      $(this).toggleClass('show');
      $(this).find('.dropdown-menu').toggleClass('show');
    });

    this.cache.$dropItem.click(function (e) {
      e.preventDefault();
      const country = $(this).data('country');
      const countryTitle = $(this).data('countrytitle');
      const parentDrop = $(this).closest('.dropdown');
      $('.dropdown-toggle', parentDrop).text(country);
      $('input', parentDrop).val(country);
      self.cache.requestPayload['country'] = country;
      self.cache.requestPayload['countryTitle'] = countryTitle;
      self.cache.$dropItem.removeClass('active');
      $(this).addClass('active');
      $(parentDrop).removeClass('show');
      $(parentDrop).find('.dropdown-menu').removeClass('show');
      e.stopPropagation();
    });

  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default ContactUs;
