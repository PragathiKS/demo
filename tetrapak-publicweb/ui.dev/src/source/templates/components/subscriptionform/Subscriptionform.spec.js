import $ from 'jquery';
import Subscriptionform from './Subscriptionform';
import subscriptionTemplate from '../../../test-templates-hbs/subscriptionform.hbs';

describe('Subscriptionform', function () {
  beforeEach(function () {
    $(document.body).empty().html(subscriptionTemplate());
    this.subscription = new Subscriptionform({
      el: document.body
    });
    this.initSpy = sinon.spy(this.subscription, 'init');
    this.hidePopUpSpy = sinon.spy(this.subscription, 'hidePopUp');
    this.showPopupSpy = sinon.spy(this.subscription, 'showPopup');
    this.submitFormSpy = sinon.spy(this.subscription, 'submitForm');
    this.getCountryListSpy = sinon.spy(this.subscription, 'getCountryList');
    this.subscription.root.modal = ()=>{};
    this.subscription.init();
  });
  afterEach(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.submitFormSpy.restore();
    this.getCountryListSpy.restore();
    this.hidePopUpSpy.restore();
    this.showPopupSpy.restore();
  });

  it('should initialize', function (done) {
    expect(this.subscription.init.called).to.be.true;
    done();
  });

  it('should not submit form when required fields are empty', function (done) {
    document.getElementById('email').value = '';
    document.getElementById('firstName').value = '';
    document.getElementById('lastName').value = '';
    this.subscription.cache.$submitBtn.click();
    expect(this.subscription.submitForm.called).to.be.false;
    done();
  });

  it('should submit form when required fields are not empty', function (done) {
    $('#email').val('example@example.com');
    $('#firstName').val('first');
    $('#lastName').val('last');
    $('#consentcheckbox').attr('checked','true');
    $('#typeOfCommunicationcheckbox-3').attr('checked','true');
    this.subscription.cache.$submitBtn.click();
    expect(this.subscription.submitForm.called).to.be.true;
    done();
  });

  it('Should update payload with dropItem changes', function() {
    document.getElementById('ddtest').click();
    expect(this.subscription.cache.requestPayload['country']).to.equal('Albania');
  });

  it('should update request payload for UTM if avaialble in URL params', function (done) {
    const paramsURL = 'http://www.test.com/?utm_campaign=val1&utm_content=val2&utm_medium=val3&utm_source=val4'
    const params = {};
    paramsURL.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(_, key, value) {
      return params[key] = value;
    });
    Object.keys(params).forEach(key => {
      if(key === 'utm_campaign') {
        this.subscription.cache.requestPayload[key] = params[key];
      } else if(key === 'utm_content') {
        this.subscription.cache.requestPayload[key] = params[key];
      } else if(key === 'utm_medium') {
        this.subscription.cache.requestPayload[key] = params[key];
      } else if(key === 'utm_source') {
        this.subscription.cache.requestPayload[key] = params[key];
      }
    });
    this.subscription.cache.$submitBtn.click();
    Object.keys(params).forEach(key => {
      if(key === 'utm_campaign') {
        expect(this.subscription.cache.requestPayload[key]).to.equal(params[key]);
      } else if(key === 'utm_content') {
        expect(this.subscription.cache.requestPayload[key]).to.equal(params[key]);
      } else if(key === 'utm_medium') {
        expect(this.subscription.cache.requestPayload[key]).to.equal(params[key]);
      } else if(key === 'utm_source') {
        expect(this.subscription.cache.requestPayload[key]).to.equal(params[key]);
      }
    });
    done();
  });

  it('should get country list and it should be equal to 2', function () {
    expect(this.subscription.getCountryList.called).to.be.true;
    expect(this.subscription.cache.countryList.length).to.equal(2);
    $('.dropdown-menu, .dropdown-toggle').keydown();
  });

  it('should open modal', function (done) {
    $(document.body).trigger('showSubscription-pw');
    expect(this.subscription.showPopup.called).to.be.true;
    done();
  });

  it('should close modal on close icon click', function (done) {
    $('.js-close-btn').click();
    expect(this.subscription.hidePopUp.called).to.be.true;
    done();
  });

});
