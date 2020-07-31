import $ from 'jquery';
import TabsList from './TabsList';
import tabslistTemplate from '../../../test-templates-hbs/tabslist.hbs';
import { trackAnalytics } from '../../../scripts/utils/analytics';

describe('TabsList', function() {
  before(function() {
    this.enableTimeouts(false);
    $(document.body)
      .empty()
      .html(tabslistTemplate());
    this.tabslist = new TabsList({ el: document.body });
    this.initSpy = sinon.spy(this.tabslist, 'init');
    this.showTabDetailSpy = sinon.spy(this.tabslist, 'showTabDetail');
    this.openStub = sinon.stub(window, 'open');


    this.trackAnalyticsSpy = sinon.spy(this.tabslist, 'trackAnalytics');
    window._satellite = {
      track() {
        /* Dummy method */
      }
    };
    this.tabslist.init();

  });

  after(function() {
    $(document.body).empty();
    this.initSpy.restore();
    this.trackAnalyticsSpy.restore();
    this.showTabDetailSpy.restore();
    this.openStub.restore();

  });

  it('should initialize', function(done) {
    expect(this.tabslist.init.called).to.be.true;
    done();
  });
  it('should call track analytics on click', function (done) {
    $('.js-tablist__event-detail-description-link').trigger('click');
    expect(this.tabslist.trackAnalytics.called).to.be.true;
    done();
  });

  it('should call showTabDetail on tab click', function (done) {
    $('.js-tablist__event').trigger('click');
    expect(this.tabslist.showTabDetail.called).to.be.true;
    done();
  });
});
