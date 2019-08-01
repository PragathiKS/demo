import $ from 'jquery';
import TabList from './TabsList';
import tablistTemplate from '../../../test-templates-hbs/tablist.hbs';

describe('Tablist', function () {
  before(function () {
    $(document.body).empty().html(tablistTemplate());
    this.tabList = new TabList({
      el: document.body
    });
    this.initSpy = sinon.spy(this.tabList, "init");
    this.showTabDetailSpy = sinon.spy(this.tabList, "showTabDetail");
    this.analyticsSpy = sinon.spy(this.tabList, 'trackAnalytics');
    this.pauseVideoSpy = sinon.spy(this.tabList, 'pauseVideoIfExists');
    this.tabList.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.showTabDetailSpy.restore();
    this.analyticsSpy.restore();
    this.pauseVideoSpy.restore();
  });
  it('should initialize', function (done) {
    expect(this.tabList.init.called).to.be.true;
    done();
  });

  it('should show tab content on click of tab', function (done) {
    $('.js-tablist__event').first().trigger('click');
    expect(this.tabList.showTabDetail.called).to.be.true;
    done();
  });
  it('should track analytics on click of description link', function (done) {
    $('.js-tablist__event-detail-description-link').first().trigger('click');
    expect(this.analyticsSpy.called).to.be.true;
    done();
  });
  it('should pause video if section is collapsed', function (done) {
    $('.collapse').trigger('hidden.bs.collapse');
    expect(this.pauseVideoSpy.called).to.be.true;
    done();
  });
});
