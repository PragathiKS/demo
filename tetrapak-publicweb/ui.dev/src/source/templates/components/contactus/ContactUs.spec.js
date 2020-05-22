import ContactUs from './ContactUs';
import $ from 'jquery';
import contactUsTemplate from '../../../test-templates-hbs/contactUs.hbs';

describe('ContactUs', function () {
  beforeEach(function () {
    $(document.body).empty().html(contactUsTemplate());
    this.contactUs = new ContactUs({
      el: document.body
    });
    this.initSpy = sinon.spy(this.contactUs, 'init');
    this.getCountryListSpy = sinon.spy(this.contactUs, 'getCountryList');
    this.submitFormSpy = sinon.spy(this.contactUs, 'submitForm');
    this.contactUs.init();
  });
  afterEach(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.getCountryListSpy.restore();
    this.submitFormSpy.restore();
  });

  it('should initialize', function () {
    expect(this.contactUs.init.called).to.be.true;
  });
  it('should get country list and it should be equal to 2', function () {
    expect(this.contactUs.getCountryList.called).to.be.true;
    expect(this.contactUs.cache.countryList.length).to.equal(2);
  });

  it('Should update request payload on step-1 next button click', function () {
    document.getElementById('purposeOfContact').value = 'Career';
    document.getElementById('step1btn').click();
    expect(this.contactUs.cache.requestPayload['purposeOfContactTitle']).to.equal('Career');
  });

  it('Should update request payload on radio button change', function () {
    $('input[type=radio][name="purposeOfContactOptions"]').trigger('change');
    expect(this.contactUs.cache.requestPayload['purposeOfContactTitle']).to.equal('Other');
    expect(this.contactUs.cache.requestPayload['purposeOfContact']).to.equal('other');
  });

  it('should update request payload when step-2 next button is clicked', function (done) {
    document.getElementById('firstName').value = 'first';
    document.getElementById('lastName').value = 'last';
    document.getElementById('email').value = 'email';
    document.getElementById('step2btn').click();
    expect(this.contactUs.cache.requestPayload['firstName']).to.equal('first');
    expect(this.contactUs.cache.requestPayload['lastName']).to.equal('last');
    expect(this.contactUs.cache.requestPayload['email']).to.equal('email');
    done();
  });

  it('should update request payload when step-3 next button is clicked', function (done) {
    document.getElementById('messageText').value = 'mockmessage';
    document.getElementById('step3btn').click();
    expect(this.contactUs.cache.requestPayload['message']).to.equal('mockmessage');
    done();
  });

  it('should not submit Form when required fields are empty', function (done) {
    document.getElementById('messageText').value = 'mockmessage';
    this.contactUs.cache.$submitBtn.click();
    expect(this.contactUs.submitForm.called).to.be.false;
    done();
  });

  it('should submit Form when required fields are not empty', function (done) {
    document.getElementById('firstName').value = 'first';
    document.getElementById('lastName').value = 'last';
    document.getElementById('email').value = 'email';
    document.getElementById('messageText').value = 'mockmessage';
    document.getElementById('purposeOfContact').value = 'Career';
    document.getElementById('country').value = 'mock country';
    this.contactUs.cache.$submitBtn.click();
    expect(this.contactUs.submitForm.called).to.be.true;
    done();
  });

  it('should not submit Form when honeypot field is filled', function (done) {
    document.getElementById('firstName').value = 'first';
    document.getElementById('lastName').value = 'last';
    document.getElementById('email').value = 'email';
    document.getElementById('messageText').value = 'mockmessage';
    document.getElementById('purposeOfContact').value = 'Career';
    document.getElementById('country').value = 'mock country';
    document.getElementById('pardot_extra_field').value = 'honeypot';
    this.contactUs.cache.$submitBtn.click();
    expect(this.contactUs.submitForm.called).to.be.false;
    done();
  });
});