import ContactUs from './ContactUs';
import $ from 'jquery';
import contactUsTemplate from '../../../test-templates-hbs/contactUs.hbs';

describe('ContactUs', function () {
  before(function () {
    $(document.body).empty().html(contactUsTemplate());
    this.contactUs = new ContactUs({
      el: document.body
    });
    this.initSpy = sinon.spy(this.contactUs, 'init');
    this.initSpy = sinon.spy(this.contactUs, 'getCountryList');
    this.contactUs.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
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

  it('should update request payload when step-2 next button is clicked', function () {
    document.getElementById('firstName').value = 'first';
    document.getElementById('lastName').value = 'last';
    document.getElementById('email').value = 'email';
    document.getElementById('step2btn').click();
    expect(this.contactUs.cache.requestPayload['firstName']).to.equal('first');
    expect(this.contactUs.cache.requestPayload['lastName']).to.equal('last');
    expect(this.contactUs.cache.requestPayload['email']).to.equal('email');
  });

  it('should update request payload when step-3 next button is clicked', function () {
    document.getElementById('messageText').value = 'mockmessage';
    document.getElementById('step3btn').click();
    expect(this.contactUs.cache.requestPayload['message']).to.equal('mockmessage');
  });
});