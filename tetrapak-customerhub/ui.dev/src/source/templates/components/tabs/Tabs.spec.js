import $ from 'jquery';
import Tabs from './Tabs';
import tabs from '../../../test-templates-hbs/tabs.hbs';

describe('Tabs', function () {
  before(function () {
    $(document.body).empty().html(tabs());
    this.tabs = new Tabs({
      el: document.body
    });
    this.initSpy = sinon.spy(this.tabs, 'init');
    this.analyticsSpy = sinon.spy(this.tabs, 'setAnalyticsParameters');
    $('.js-tabs__tab-link').on('click', (e) => {
      e.preventDefault(); // To prevent page redirect
    });
    this.tabs.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.analyticsSpy.restore();
  });
  it('should initialize component', function (done) {
    expect(this.initSpy.called).to.be.true;
    done();
  });
  it('should trigger analytics events on click', function (done) {
    $('.js-tabs__tab-link').eq(0).trigger('click');
    expect(this.analyticsSpy.called).to.be.true;
    done();
  });
});
