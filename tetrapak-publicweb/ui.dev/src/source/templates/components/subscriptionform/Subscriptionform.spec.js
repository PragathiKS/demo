import $ from 'jquery';
import Subscriptionform from './subscriptionform';
//import subscriptionTemplate from '../../../test-templates-hbs/subscriptionform.hbs';

describe('SubscriptionForm', function () {
  beforeEach(function () {
    $(document.body).empty().html('<div></div>');
    this.subscription = new Subscriptionform({
      el: document.body
    });
    this.initSpy = sinon.spy(this.subscription, 'init');
    this.subscription.init();
  });
  afterEach(function () {
    $(document.body).empty();
    this.initSpy.restore();
  });

  it('should initialize', function () {
    expect(this.subscription.init.called).to.be.true;
  });
});
