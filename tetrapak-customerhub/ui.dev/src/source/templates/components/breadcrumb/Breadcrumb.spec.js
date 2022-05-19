import Breadcrumb from './Breadcrumb';
import $ from 'jquery';
import breadcrumbTemplate from '../../../test-templates-hbs/breadcrumb.hbs';
import { trackAnalytics } from '../../../scripts/utils/analytics';


describe('Breadcrumb', function () {
  before(function () {
    $(document.body).empty().html(breadcrumbTemplate());
    this.breadcrumb = new Breadcrumb({ el: document.body });
    this.initSpy = sinon.spy(this.breadcrumb, 'init');
    this.trackAnalyticsSpy = sinon.spy(this.breadcrumb, 'trackAnalytics');
    window.digitalData = {};
    window._satellite = {
      track() { /* Dummy method */ }
    };
    this.breadcrumb.init();

  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.trackAnalyticsSpy.restore();
  });
  it('should initialize', function (done) {
    expect(this.breadcrumb.init.called).to.be.true;
    done();
  });

  it('should call track analytics on click', function (done) {
    $('.js-tp_pw-breadcrumb__link').trigger('click');
    expect(this.breadcrumb.trackAnalytics.called).to.be.true;
    done();
  });

});
