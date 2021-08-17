import $ from 'jquery';
import Businessinquiryform from './Businessinquiryform';
import businessinquiryTemplate from '../../../test-templates-hbs/businessinquiryform.hbs';
import { ajaxWrapper } from '../../../scripts/utils/ajax';

describe('BusinessInquiryForm', function () {

  function ajaxResponse(response) {
    const pr = $.Deferred();
    pr.resolve(response, 'success');
    return pr.promise();
  }

  before(function () {
    $(document.body).empty().html(businessinquiryTemplate());
    this.businessinquiry = new Businessinquiryform({
      el: document.body
    });
    this.initSpy = sinon.spy(this.businessinquiry, 'init');
    this.submitFormSpy = sinon.spy(this.businessinquiry, 'submitForm');
    this.newRequestHanlderSpy = sinon.spy(this.businessinquiry, 'newRequestHanlder');
    this.reloadStub = sinon.stub(this.businessinquiry, 'reloadPage');
    this.onRadioChangeHandlerFirstSpy = sinon.spy(this.businessinquiry, 'onRadioChangeHandlerFirst');
    this.onRadioChangeHandlerSecondSpy = sinon.spy(this.businessinquiry, 'onRadioChangeHandlerSecond');
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.returns(ajaxResponse({statusCode:'200'}));
    this.getCountryListSpy = sinon.spy(this.businessinquiry, 'getCountryList');
    this.businessinquiry.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.submitFormSpy.restore();
    this.newRequestHanlderSpy.restore();
    this.reloadStub.restore();
    this.onRadioChangeHandlerFirstSpy.restore();
    this.onRadioChangeHandlerSecondSpy.restore();
    this.getCountryListSpy.restore();
    this.ajaxStub.restore();
  });

  it('should initialize', function () {
    expect(this.businessinquiry.init.called).to.be.true;
  });

  it('should get country list and it should be equal to 2', function () {
    expect(this.businessinquiry.getCountryList.called).to.be.true;
    expect(this.businessinquiry.cache.countryList.length).to.equal(2);
    $('.country-dropdown, .country-dropdown-select').keydown();
  });

  it('Should update payload with dropItem changes', function() {
    document.getElementById('ddtest').click();
    expect(this.businessinquiry.cache.requestPayload['countryTitle']).to.equal('Albania');
    expect(this.businessinquiry.cache.requestPayload['country']).to.equal('Albania');
  });

  it('Should update payload with positionDropItem changes', function() {
    document.getElementById('pddtest').click();
    expect(this.businessinquiry.cache.requestPayload['position']).to.equal('associate');
  });

  it('Should update request payload on step-1 next button click', function () {
    $('input[name="purposeOfContactOptionsInBusinessEq"]').click({target : { value : 'Contact me' , id : 'demo'}});
    $('input[name="purposeOfContactInBusinessEqTitle"]').val('Contact me');
    document.getElementById('step1btn').click();
    expect(this.businessinquiry.cache.requestPayload['purposeOfContactInBusinessEqTitle']).to.equal('Contact me');
  });

  it('Should update request payload on radio button change', function () {
    $('input[type=radio][name="purposeOfContactInBusinessEqTitle"]').trigger('change');
    expect(this.businessinquiry.cache.requestPayload['purposeOfContactInBusinessEqTitle']).to.not.equal('Other');
  });

  it('Should update request payload on step-2 next button click', function () {
    $('input[name="purposeOfContactOptionsInInterestArea"]').click({ target : { value : 'End-to-End solutions' , id : 'demo'}});
    $('input[name="purposeOfInterestAreaEqTitle"]').val('End-to-End solutions');
    document.getElementById('step2btn').click();
    expect(this.businessinquiry.cache.requestPayload['purposeOfInterestAreaEqTitle']).to.equal('End-to-End solutions');
  });

  it('should update request payload when step-3 next button is clicked', function (done) {
    document.getElementById('firstNameField').value = 'first';
    document.getElementById('lastNameField').value = 'last';
    document.getElementById('emailBef').value = 'email';
    document.getElementById('phoneField').value = 'phone';
    document.getElementById('step3btn').click();
    expect(this.businessinquiry.cache.requestPayload['firstNameField']).to.equal('first');
    expect(this.businessinquiry.cache.requestPayload['lastNameField']).to.equal('last');
    expect(this.businessinquiry.cache.requestPayload['emailBef']).to.equal('email');
    expect(this.businessinquiry.cache.requestPayload['phoneField']).to.equal('phone');
    done();
  });


  it('should submit Form when required fields are not empty', function (done) {
    $('input[name="purposeOfContactInBusinessEqTitle"]').val("Contact me");
    $('input[name="purposeOfInterestAreaEqTitle"]').val("End to End Solution");
    document.getElementById('firstNameField').value = 'first';
    document.getElementById('lastNameField').value = 'last';
    document.getElementById('emailBef').value = 'email';
    document.getElementById('phoneField').value = 'mockmessage';
    document.getElementById('company').value = 'company';
    document.getElementById('position').value = 'position';
    document.getElementById('befconsentcheckbox').checked = true;
    this.businessinquiry.cache.$submitBtn.click();
    expect(this.businessinquiry.submitForm.called).to.be.true;
    done();
  });

  it('should submit Form when honeypot field is filled', function (done) {
    $('input[name="purposeOfContactInBusinessEqTitle"]').val("Contact me");
    $('input[name="purposeOfInterestAreaEqTitle"]').val("End to End Solution");
    document.getElementById('firstNameField').value = 'first';
    document.getElementById('lastNameField').value = 'last';
    document.getElementById('emailBef').value = 'email';
    document.getElementById('phoneField').value = 'mockmessage';
    document.getElementById('company').value = 'company';
    document.getElementById('position').value = 'position';
    document.getElementById('befconsentcheckbox').checked = true;
    document.getElementById('pardot_extra_field_bef').value = 'honeypot';
    this.businessinquiry.cache.$submitBtn.click();
    expect(this.businessinquiry.submitForm.called).to.be.true;
    done();
  });

  it('should update request payload when step-4 next button is clicked', function (done) {
    document.getElementById('company').value = 'company';
    document.getElementById('position').value = 'position';
    document.getElementById('befconsentcheckbox').checked = true;
    $(document.getElementById('step4btn')).click();
    expect(this.businessinquiry.cache.requestPayload['company']).to.equal('company');
    expect(this.businessinquiry.cache.requestPayload['position']).to.equal('position');
    done();
  });
  it('should update request payload for UTM if avaialble in URL params', function (done) {
    const paramsURL = 'http://www.test.com/?utm_campaign=val1&utm_content=val2&utm_medium=val3&utm_source=val4'
    const params = {};
    paramsURL.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(_, key, value) {
      return params[key] = value;
    });
    Object.keys(params).forEach(key => {
      if(key === 'utm_campaign') {
        this.businessinquiry.cache.requestPayload[key] = params[key];
      } else if(key === 'utm_content') {
        this.businessinquiry.cache.requestPayload[key] = params[key];
      } else if(key === 'utm_medium') {
        this.businessinquiry.cache.requestPayload[key] = params[key];
      } else if(key === 'utm_source') {
        this.businessinquiry.cache.requestPayload[key] = params[key];
      }
    });
    this.businessinquiry.cache.$submitBtn.click();
    Object.keys(params).forEach(key => {
      if(key === 'utm_campaign') {
        expect(this.businessinquiry.cache.requestPayload[key]).to.equal(params[key]);
      } else if(key === 'utm_content') {
        expect(this.businessinquiry.cache.requestPayload[key]).to.equal(params[key]);
      } else if(key === 'utm_medium') {
        expect(this.businessinquiry.cache.requestPayload[key]).to.equal(params[key]);
      } else if(key === 'utm_source') {
        expect(this.businessinquiry.cache.requestPayload[key]).to.equal(params[key]);
      }
    });
    done();
  });
  it('should call newRequestHanlder on newRequestBtn is clicked', function (done) {
    $('.newRequestBtn').click();
    expect(this.businessinquiry.newRequestHanlder.called).to.be.true;
    done();
  });
  it('should call onRadioChangeHandlerFirst on onRadioChangeHandlerFirst button is changes', function (done) {
    $('input[type=radio][name="purposeOfContactOptionsInBusinessEq"]').change();
    expect(this.businessinquiry.onRadioChangeHandlerFirst.called).to.be.true;
    done();
  });
  it('should call onRadioChangeHandlerSecond on onRadioChangeHandlerSecond button is changes', function (done) {
    $('input[type=radio][name="purposeOfContactOptionsInInterestArea"]').change();
    expect(this.businessinquiry.onRadioChangeHandlerSecond.called).to.be.true;
    done();
  });
  it('Should call step-2 previous button click', function () {
    $('#step1btn').addClass('previousbtn');
    $('#step2btn').addClass('previousbtn');
    $('#step3btn').addClass('previousbtn');
    document.getElementById('step1btn').click();
    document.getElementById('step2btn').click();
    document.getElementById('step3btn').click();
  });

  it('should give error on empty fields', function (done) {
    $('#step3btn').removeClass('previousbtn');
    document.getElementById('firstNameField').value = '';
    document.getElementById('lastNameField').value = 'last';
    document.getElementById('emailBef').value = 'email@gmail.com';
    document.getElementById('phoneField').value = 'phone';
    document.getElementById('company').value = 'company';
    document.getElementById('step4btn').click();
    document.getElementById('firstNameField').value = 'first';
    document.getElementById('lastNameField').value = '';
    document.getElementById('step4btn').click();
    document.getElementById('phoneField').value = '';
    document.getElementById('lastNameField').value = 'last';
    document.getElementById('step4btn').click();
    document.getElementById('phoneField').value = 'phone';
    document.getElementById('company').value = '';
    $('input[name="purposeOfContactInBusinessEqTitle"]').val('');
    $('input[name="purposeOfInterestAreaEqTitle"]').val('value');
    $('#step1btn').addClass('previousbtn');
    document.getElementById('step1btn').click();
    done();
  });
});
