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
    this.reloadPageSpy = sinon.stub(this.businessinquiry, 'reloadPage');
    this.validateFieldSpy = sinon.stub(this.businessinquiry, 'validateField');
    this.resetErrorMsgSpy = sinon.stub(this.businessinquiry, 'resetErrorMsg');
    this.checkMessageLengthSpy = sinon.stub(this.businessinquiry, 'checkMessageLength');
    this.onRadioClickHandlerSpy = sinon.spy(this.businessinquiry, 'onRadioClickHandler');
    this.onBusinessInterestChangeHandlerSpy = sinon.spy(this.businessinquiry, 'onBusinessInterestChangeHandler');
    this.onBaIntSubCategoryFoodHandlerSpy = sinon.spy(this.businessinquiry, 'onBaIntSubCategoryFoodHandler');
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.returns(ajaxResponse({statusCode:'200'}));
    this.getCountryListSpy = sinon.spy(this.businessinquiry, 'getCountryList');
    this.getPositionListSpy = sinon.spy(this.businessinquiry, 'getPositionList');
    this.getFunctionListSpy = sinon.spy(this.businessinquiry, 'getFunctionList');
    this.getRoleListSpy = sinon.spy(this.businessinquiry, 'getRoleList');
    this.businessinquiry.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.submitFormSpy.restore();
    this.newRequestHanlderSpy.restore();
    this.reloadPageSpy.restore();
    this.validateFieldSpy.restore();
    this.resetErrorMsgSpy.restore();
    this.checkMessageLengthSpy.restore();
    this.onRadioClickHandlerSpy.restore();
    this.onBusinessInterestChangeHandlerSpy.restore();
    this.onBaIntSubCategoryFoodHandlerSpy.restore();
    this.getCountryListSpy.restore();
    this.getPositionListSpy.restore();
    this.getFunctionListSpy.restore();
    this.getRoleListSpy.restore();
    this.ajaxStub.restore();
  });

  it('should initialize', function () {
    expect(this.businessinquiry.init.called).to.be.true;
  });

  it('should call newRequestHanlder on newRequestBtn is clicked', function (done) {
    $('.newRequestBtn').click();
    expect(this.businessinquiry.newRequestHanlder.called).to.be.true;
    done();
  });

  it('should call reloadPage on button is clicked', function (done) {
    $('.newRequestBtn').click();
    expect(this.businessinquiry.reloadPage.called).to.be.true;
    done();
  });

  it('should call validateField on button is clicked', function (done) {
    $('.tpatom-btn').click();
    expect(this.businessinquiry.validateField.called).to.be.true;
    done();
  });

  it('should call resetErrorMsg on button is clicked', function (done) {
    $('input').change();
    expect(this.businessinquiry.resetErrorMsg.called).to.be.true;
    done();
  });

  it('should call checkMessageLength on button is clicked', function (done) {
    $('.tpatom-btn').click();
    expect(this.businessinquiry.checkMessageLength.called).to.be.true;
    done();
  });

  it('should call onRadioClickHandler on radio button is changes', function (done) {
    $('input[type=radio][name="purposeOfContactOptionsInBusinessEq"]').change();
    expect(this.businessinquiry.onRadioClickHandler.called).to.be.true;
    done();
  });

  it('should call onBusinessInterestChangeHandler on onBusinessInterestChangeHandler button is changes', function (done) {
    $('input[type=checkbox][name="purposeOfContactOptionsInInterestArea"]').change();
    expect(this.businessinquiry.onBusinessInterestChangeHandler.called).to.be.true;
    done();
  });
  
  it('should call onBaIntSubCategoryFoodHandler on onBaIntSubCategoryFoodHandler button is changes', function (done) {
    $('input[type=radio][name="businessAreaProcessingNeedBeverage"]').change();
    expect(this.businessinquiry.onBaIntSubCategoryFoodHandler.called).to.be.true;
    done();
  });
  
  it('should get country list and it should be equal to 2', function () {
    expect(this.businessinquiry.getCountryList.called).to.be.true;
    expect(this.businessinquiry.cache.countryList.length).to.equal(2);
    $('.country-dropdown, .country-dropdown-select').keydown();
  });

  it('should get position list and it should be equal to 2', function () {
    expect(this.businessinquiry.getPositionList.called).to.be.true;
    expect(this.businessinquiry.cache.positionList.length).to.equal(2);
    $('.position-dropdown, .position-dropdown-select').keydown();
  });

  it('should get function list and it should be equal to 2', function () {
    expect(this.businessinquiry.getFunctionList.called).to.be.true;
    expect(this.businessinquiry.cache.functionList.length).to.equal(2);
    $('.function-dropdown, .function-dropdown-select').keydown();
  });

  it('should get role list and it should be equal to 2', function () {
    expect(this.businessinquiry.getRoleList.called).to.be.true;
    expect(this.businessinquiry.cache.roleList.length).to.equal(2);
    $('.role-dropdown, .role-dropdown-select').keydown();
  });

  it('Should update payload with dropItem changes', function() {
    document.getElementById('ddtest').click();
    expect(this.businessinquiry.cache.requestPayload['countryTitle']).to.equal('Albania');
    expect(this.businessinquiry.cache.requestPayload['country']).to.equal('Albania');
  });

  it('Should update payload with positionDropItem changes', function() {
    document.getElementById('pddtest').click();
    expect(this.businessinquiry.cache.requestPayload['position']).to.equal('Associate');
  });

  it('Should update payload with functionDropItem changes', function() {
    document.getElementById('fddtest').click();
    expect(this.businessinquiry.cache.requestPayload['function']).to.equal('Administrative');
  });

  it('Should update payload with roleDropItem changes', function() {
    document.getElementById('rddtest').click();
    expect(this.businessinquiry.cache.requestPayload['businessEnquiryProfile']).to.equal('Consultant');
  });
  
  it('Should update request payload on radio button change', function () {
    $('input[type=radio][name="purposeOfContactInBusinessEqTitle"]').trigger('change');
    expect(this.businessinquiry.cache.requestPayload['purposeOfContactInBusinessEqTitle']).to.not.equal('Other');
  });
  
  it('should submit Form when required fields are not empty', function (done) {
    $('input[name="purposeOfContactInBusinessEqTitle"]').val("Request for Quote");
    $('input[name="purposeOfInterestAreaEqTitle"]').val("Packaging");
    document.getElementById('email').value = 'email@email.com';
    document.getElementById('firstName').value = 'first';
    document.getElementById('lastName').value = 'last';
    document.getElementById('phone').value = '1234567890';
    document.getElementById('workplaceCity').value = 'mockmessage';
    document.getElementById('company').value = 'company';
    document.getElementById('befconsentcheckbox').checked = true;
    this.businessinquiry.cache.$submitBtn.click();
    expect(this.businessinquiry.submitForm.called).to.be.true;
    done();
  });

  it('should submit Form when honeypot field is filled', function (done) {
    $('input[name="purposeOfContactInBusinessEqTitle"]').val("Request for Quote");
    $('input[name="purposeOfInterestAreaEqTitle"]').val("Packaging");
    document.getElementById('email').value = 'email@email.com';
    document.getElementById('firstName').value = 'first';
    document.getElementById('lastName').value = 'last';
    document.getElementById('phone').value = '1234567890';
    document.getElementById('workplaceCity').value = 'mockmessage';
    document.getElementById('company').value = 'company';
    document.getElementById('befconsentcheckbox').checked = true;
    document.getElementById('pardot_extra_field_bef').value = 'honeypot';
    this.businessinquiry.cache.$submitBtn.click();
    expect(this.businessinquiry.submitForm.called).to.be.true;
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
  
  it('Should call step-2 previous button click', function () {
    $('#step1btn').addClass('previousbtn');
    $('#step2btn').addClass('previousbtn');
    document.getElementById('step1btn').click();
    document.getElementById('step2btn').click();
  });

  it('should give error on empty fields', function (done) {
    document.getElementById('email').value = 'email@email.com';
    document.getElementById('firstName').value = '';
    document.getElementById('lastName').value = 'last';
    document.getElementById('phone').value = '';
    document.getElementById('workplaceCity').value = 'Sweden';
    document.getElementById('company').value = 'company';
    document.getElementById('step3btn').click();
    document.getElementById('firstName').value = 'first';
    document.getElementById('lastName').value = '';
    document.getElementById('step3btn').click();
    document.getElementById('phone').value = '';
    document.getElementById('lastName').value = 'last';
    document.getElementById('step3btn').click();
    document.getElementById('phone').value = '9870980980';
    document.getElementById('workplaceCity').value = 'Sweden';
    document.getElementById('company').value = '';
    $('input[name="purposeOfContactInBusinessEqTitle"]').val('');
    $('input[name="purposeOfInterestAreaEqTitle"]').val('value');
    $('#step1btn').addClass('previousbtn');
    document.getElementById('step1btn').click();
    done();
  });
});
