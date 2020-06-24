import $ from 'jquery';
import 'bootstrap';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { ajaxMethods, REG_EMAIL } from '../../../scripts/utils/constants';
import { validateFieldsForTags } from '../../../scripts/common/common';

class Subscriptionform {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    /* Initialize selector cache here */
    this.cache.businessformapi = this.root.find('form.pw-form-subscriptionForm');
    this.cache.$submitBtn = $('form.pw-form-subscriptionForm button[type="submit"]', this.root);
    this.cache.$inputEmail = $('form.pw-form-subscriptionForm  input[type="email"]', this.root);

    this.cache.requestPayload = {
      'email': '',
      'consent' :''
    };

  }

  validEmail(email) {
    return REG_EMAIL.test(email);
  }

  submitForm = () => {
    const servletPath = this.cache.businessformapi.data('sf-api-servlet');
    const countryCode = this.cache.businessformapi.data('sf-countrycode');
    const langCode = this.cache.businessformapi.data('sf-langcode');
    const pardot_extra_field = $('#pardot_extra_field_sf').val();
    
    const dataObj = {};
    dataObj['policyConsent'] = true;
    dataObj['email'] = this.cache.requestPayload.email;
    dataObj['pardot_extra_field'] = pardot_extra_field;
    dataObj['language'] = langCode;
    dataObj['site'] = countryCode;
   

    ajaxWrapper.getXhrObj({
      url: servletPath,
      method: ajaxMethods.POST,
      data: dataObj
    }).done(
      (response) => {
        if (response.statusCode === '200') {
          $('.sf-tab-pane', this.root).removeClass('active');
          $('#sf-step-final', this.root).addClass('active');
          $('.serviceError').removeClass('d-block');
          $('html, body').animate({
            scrollTop: $('#sfUs').offset().top - 150
          });
        } else {
          $('.serviceError').addClass('d-block');
        }
      }
    );
  }


  bindEvents() {
    const { requestPayload, $submitBtn } = this.cache;
    const self = this;
    $submitBtn.click(function (e) {
      e.preventDefault();
      e.stopPropagation();
      let isvalid = true;
      const target = $(this).data('target');
      const tab = $(this).closest('.tab-content-steps');
      const input = tab.find('input');
      if (!$(this).hasClass('previousbtn') && (input.length > 0)) {
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
          $('.sf-tab-pane').removeClass('active');
          $(target).addClass('active');
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
  }
}

export default Subscriptionform;
