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
    this.onPurposeOfContactHandlerSpy = sinon.spy(this.businessinquiry, 'onPurposeOfContactHandler');
    this.onBusinessInterestChangeHandlerSpy = sinon.spy(this.businessinquiry, 'onBusinessInterestChangeHandler');
    this.onBaIntPackagingHandlerSpy = sinon.spy(this.businessinquiry, 'onBaIntPackagingHandler');
    this.onBaIntProcessingSupportHandlerSpy = sinon.spy(this.businessinquiry, 'onBaIntProcessingSupportHandler');
    this.onBaIntProcessingCategoryFoodHandlerSpy = sinon.spy(this.businessinquiry, 'onBaIntProcessingCategoryFoodHandler');
    this.onBaIntSubCategoryFoodHandlerSpy = sinon.spy(this.businessinquiry, 'onBaIntSubCategoryFoodHandler');
    this.onBaIntServicesHandlerSpy = sinon.spy(this.businessinquiry, 'onBaIntServicesHandler');
    this.onBusinessEnqNeedHandlerSpy = sinon.spy(this.businessinquiry, 'onBusinessEnqNeedHandler');
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
    this.onPurposeOfContactHandlerSpy.restore();
    this.onBusinessInterestChangeHandlerSpy.restore();
    this.onBaIntPackagingHandlerSpy.restore();
    this.onBaIntProcessingSupportHandlerSpy.restore();
    this.onBaIntProcessingCategoryFoodHandlerSpy.restore();
    this.onBaIntSubCategoryFoodHandlerSpy.restore();
    this.onBaIntServicesHandlerSpy.restore();
    this.onBusinessEnqNeedHandlerSpy.restore();
    this.getCountryListSpy.restore();
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
  it('should call onPurposeOfContactHandler on onPurposeOfContactHandler button is changes', function (done) {
    $('input[type=radio][name="purposeOfContactOptionsInBusinessEq"]').change();
    expect(this.businessinquiry.onPurposeOfContactHandler.called).to.be.true;
    done();
  });
  it('should call onBusinessInterestChangeHandler on onBusinessInterestChangeHandler button is changes', function (done) {
    $('input[type=checkbox][name="purposeOfContactOptionsInInterestArea"]').change();
    expect(this.businessinquiry.onBusinessInterestChangeHandler.called).to.be.true;
    done();
  });
  it('should call onBaIntPackagingHandler on onBaIntPackagingHandler button is changes', function (done) {
    $('input[type=radio][name="businessAreaInterestPackaging"]').change();
    expect(this.businessinquiry.onBaIntPackagingHandler.called).to.be.true;
    done();
  });
  it('should call onBaIntProcessingSupportHandler on onBaIntProcessingSupportHandler button is changes', function (done) {
    $('input[type=radio][name="businessAreaInterestProcessingSupport"]').change();
    expect(this.businessinquiry.onBaIntProcessingSupportHandler.called).to.be.true;
    done();
  });
  it('should call onBaIntProcessingCategoryFoodHandler on onBaIntProcessingCategoryFoodHandler button is changes', function (done) {
    $('input[type=radio][name="businessAreaProcessingCategoryFood"]').change();
    expect(this.businessinquiry.onBaIntProcessingCategoryFoodHandler.called).to.be.true;
    done();
  });
  it('should call onBaIntSubCategoryFoodHandler on onBaIntSubCategoryFoodHandler button is changes', function (done) {
    $('input[type=radio][name="businessAreaProcessingNeedBeverage"]').change();
    expect(this.businessinquiry.onBaIntSubCategoryFoodHandler.called).to.be.true;
    done();
  });
  it('should call onBaIntServicesHandler on onBaIntServicesHandler button is changes', function (done) {
    $('input[type=radio][name="businessAreaInterestServices"]').change();
    expect(this.businessinquiry.onBaIntServicesHandler.called).to.be.true;
    done();
  });
  it('should call onBusinessEnqNeedHandler on onBusinessEnqNeedHandler button is changes', function (done) {
    $('input[type=radio][name="businessEnquiryNeed"]').change();
    expect(this.businessinquiry.onBusinessEnqNeedHandler.called).to.be.true;
    done();
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
