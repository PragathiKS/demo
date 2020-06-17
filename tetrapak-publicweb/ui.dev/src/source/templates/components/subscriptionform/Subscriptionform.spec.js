import $ from 'jquery';
import Subscriptionform from './subscriptionform';
import subscriptionTemplate from '../../../test-templates-hbs/subscriptionform.hbs';

describe('SubscriptionForm', function () {
  beforeEach(function () {
    $(document.body).empty().html(subscriptionTemplate);
    this.subscription = new Subscriptionform({
      el: document.body
    });
    this.initSpy = sinon.spy(this.subscription, 'init');
    this.submitFormSpy = sinon.spy(this.subscription, 'submitForm');
    this.subscription.init();
  });
  afterEach(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.submitFormSpy.restore();
  });

  it('should initialize', function () {
    expect(this.subscription.init.called).to.be.true;
  });

  it('should submit Form when required fields are empty', function (done) {
    document.getElementById('email').value = '';
    this.subscription.cache.$submitBtn.click();
    expect(this.subscription.submitForm.called).to.be.false;
    done();
  });

  it('should submit Form when required fields are not empty', function (done) {
    $('#email').val('example@example.com');
    $('#consentcheckbox').attr('checked','true');
    this.subscription.cache.$submitBtn.click();
    expect(this.subscription.submitForm.called).to.be.true;
    done();
  });


});
