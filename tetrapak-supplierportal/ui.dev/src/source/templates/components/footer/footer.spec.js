import $ from 'jquery';
import Footer from './Footer';
import footerTemplate from '../../../test-templates-hbs/footer.hbs';

describe('Footer', function () {
  before(function () {
    $(document.body).empty().html(footerTemplate());
    this.footer = new Footer({
      el: document.body
    });
    this.initSpy = sinon.spy(this.footer, 'init');
    this.trackAnalyticsSpy = sinon.spy(this.footer, 'trackAnalytics');
    this.openStub = sinon.stub(window, 'open');
    this.isExternalStub = sinon.stub(this.footer, 'isExternal');
    this.isExternalStub.returns(true);
    window.digitalData = {};
    window._satellite = {
      track() { /* Dummy method */ }
    };
    this.footer.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.trackAnalyticsSpy.restore();
    this.openStub.restore();
    this.isExternalStub.restore();
  });
  it('should initialize', function (done) {
    expect(this.footer.init.called).to.be.true;
    done();
  });
  it('should call track analytics on click', function (done) {
    $('.tp-pw-footer-data-analytics').trigger('click');
    expect(this.footer.trackAnalytics.called).to.be.true;
    done();
  });
});
