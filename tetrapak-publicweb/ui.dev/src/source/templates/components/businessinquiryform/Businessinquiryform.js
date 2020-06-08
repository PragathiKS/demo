import $ from 'jquery';
import 'bootstrap';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { ajaxMethods, REG_EMAIL } from '../../../scripts/utils/constants';

/* eslint-disable */
class Businessinquiryform {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    /* Initialize selector cache here */
    this.cache.businessformapi = this.root.find('form.pw-form-businessform');
    this.cache.$nextbtn = this.root.find('.pw-businessEnquiry-form .tpatom-btn[type=button]');
    this.cache.$radioListFirst = this.root.find('input[type=radio][name="purposeOfContactOptionsInBusinessEq"]');
    this.cache.$radioListSecond = this.root.find('input[type=radio][name="purposeOfContactOptionsInInterestArea"]');
    this.cache.$newRequestBtn = $('.newRequestBtn', this.root);
    this.cache.$submitBtn = $('button[type="submit"]', this.root);

    this.cache.requestPayload = {
      'domainURL': window.location.host,
      'purposeOfContact': '',
      'interestArea': '',
      'firstName': '',
      'lastName': '',
      'email': '',
      'message': '',
      'phone': '',
      'purposeOfContactOptionsInBusinessEq': '',
      'purposeOfContactOptionsInInterestArea':'',
      'company':'',
      'position':''
    };
  }

  validEmail(email) {
    return REG_EMAIL.test(email);
  }

  newRequestHanlder = e => {
    e.preventDefault();
    e.stopPropagation();
    location.reload();
  }

  submitForm = () => {
    const servletPath = this.cache.businessformapi.data('bef-api-servlet');
    const countryCode = this.cache.businessformapi.data('bef-countryCode');
    const langCode = this.cache.businessformapi.data('bef-langCode');
    const dataObj = {}
    dataObj['purpose']=  this.cache.requestPayload.purposeOfContactOptionsInBusinessEq
    dataObj['businessArea']= this.cache.requestPayload.purposeOfContactOptionsInInterestArea
    dataObj['firstName']= this.cache.requestPayload.firstName
    dataObj['lastName']= this.cache.requestPayload.lastName
    dataObj['email']= this.cache.requestPayload.email
    dataObj['phoneNumber']= this.cache.requestPayload.phone
    dataObj['company']= this.cache.requestPayload.comapny
    dataObj['position']= this.cache.requestPayload.position
    dataObj['language']= langCode
    dataObj['site']= countryCode
    dataObj['policyConsent']= true
    dataObj['pardot_extra_field']= this.cache.requestPayload.pardot_extra_field
    console.log(dataObj)

    ajaxWrapper.getXhrObj({
      url: servletPath,
      method: ajaxMethods.POST,
      data: { 'inputJson': JSON.stringify(dataObj) }
    }).done(
      (response) => {
        if (response.statusCode === '200') {
          const offsetContact = $('#pw-contactUs').offset();
          $('.bef-tab-pane', this.root).removeClass('active');
          $('#bef-step-final', this.root).addClass('active');
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
    const { requestPayload, $radioListFirst , $radioListSecond, $newRequestBtn, $nextbtn, $submitBtn, $dropItem } = this.cache;
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
      if (!$(this).hasClass('previousbtn') && (input.length > 0 || textarea.length > 0)) {
        $('input, textarea', tab).each(function () {
          const fieldName = $(this).attr('name');
          console.log({fieldName})
          $('div.' + fieldName).text($(this).val());
          if (fieldName in self.cache.requestPayload) {
            requestPayload[fieldName] = $(this).val();
          }
          console.log(fieldName,$(this).val())
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
          $('.bef-tab-pane').removeClass('active');
          $(target).addClass('active');
        }
      }
    });

    $submitBtn.click(function (e) {
      e.preventDefault();
      e.stopPropagation();
      let isvalid = true;
      const honeyPotFieldValue = $('#pardot_extra_field', self.root).val();

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
    
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.getCountryList();
  }
}
/* eslint-ensable */

export default Businessinquiryform;
