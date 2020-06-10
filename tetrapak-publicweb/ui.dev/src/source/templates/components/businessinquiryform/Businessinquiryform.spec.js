import Businessinquiryform from './Businessinquiryform';
import $ from 'jquery';
import businessinquiryTemplate from '../../../test-templates-hbs/businessinquiryform.hbs';

describe('BusinessInquiryForm', function () {
  before(function () {
    $(document.body).empty().html(businessinquiryTemplate());
    this.businessinquiry = new Businessinquiryform({
      el: document.body
    });
    this.initSpy = sinon.spy(this.businessinquiry, 'init');
    this.submitFormSpy = sinon.spy(this.businessinquiry, 'submitForm');
    this.businessinquiry.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.submitFormSpy.restore();
  });

  it('should initialize', function () {
    expect(this.businessinquiry.init.called).to.be.true;
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
    console.log(this.businessinquiry.cache.requestPayload['purposeOfInterestAreaEqTitle'], )
    expect(this.businessinquiry.cache.requestPayload['purposeOfInterestAreaEqTitle']).to.equal('End-to-End solutions');
  });

  it('should update request payload when step-3 next button is clicked', function (done) {
    document.getElementById('firstName').value = 'first';
    document.getElementById('lastName').value = 'last';
    document.getElementById('email').value = 'email';
    document.getElementById('phone').value = 'phone';
    document.getElementById('step3btn').click();
    expect(this.businessinquiry.cache.requestPayload['firstName']).to.equal('first');
    expect(this.businessinquiry.cache.requestPayload['lastName']).to.equal('last');
    expect(this.businessinquiry.cache.requestPayload['email']).to.equal('email');
    expect(this.businessinquiry.cache.requestPayload['phone']).to.equal('phone');
    done();
  });

  it('should update request payload when step-4 next button is clicked', function (done) {
    document.getElementById('company').value = 'company';
    document.getElementById('position').value = 'position';
    document.getElementById('consentcheckbox').checked = true;
    document.getElementById('step4btn').click();
    expect(this.businessinquiry.cache.requestPayload['company']).to.equal('company');
    expect(this.businessinquiry.cache.requestPayload['position']).to.equal('position');
    done();
  });

  it('should submit Form when required fields are not empty', function (done) {
    $('input[name="purposeOfContactInBusinessEqTitle"]').val("Contact me");
    $('input[name="purposeOfInterestAreaEqTitle"]').val("End to End Solution");
    document.getElementById('firstName').value = 'first';
    document.getElementById('lastName').value = 'last';
    document.getElementById('email').value = 'email';
    document.getElementById('phone').value = 'mockmessage';
    document.getElementById('company').value = 'company';
    document.getElementById('position').value = 'position';
    document.getElementById('consentcheckbox').checked = true;
    this.businessinquiry.cache.$submitBtn.click();
    expect(this.businessinquiry.submitForm.called).to.be.true;
    done();
  });

  it('should not submit Form when honeypot field is filled', function (done) {
    $('input[name="purposeOfContactInBusinessEqTitle"]').val("Contact me");
    $('input[name="purposeOfInterestAreaEqTitle"]').val("End to End Solution");
    document.getElementById('firstName').value = 'first';
    document.getElementById('lastName').value = 'last';
    document.getElementById('email').value = 'email';
    document.getElementById('phone').value = 'mockmessage';
    document.getElementById('company').value = 'company';
    document.getElementById('position').value = 'position';
    document.getElementById('consentcheckbox').checked = true;
    document.getElementById('pardot_extra_field').value = 'honeypot';
    this.businessinquiry.cache.$submitBtn.click();
    expect(this.businessinquiry.submitForm.called).to.be.false;
    done();
  });


});
