import $ from 'jquery';
import TabList from './TabsList';
import tablistTemplate from '../../../test-templates-hbs/tablist.hbs';

describe('Tablist', function () {
  before(function () {
    this.tabList = new TabList({
      el: document.body
    });
    $(document.body).empty().html(tablistTemplate());
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
  it('should initialize', function () {
    expect(this.tabList.init.called).to.be.true;
  });

  it('should show tab content on click of tab', function () {
    $('.js-tablist__event').first().trigger('click');
    expect(this.tabList.showTabDetail.called).to.be.true;
  });
  it('should track analytics on click of description link', function () {
    $('.js-tablist__event-detail-description-link').first().trigger('click');
    expect(this.analyticsSpy.called).to.be.true;
  });
  it('should pause video if section is collapsed', function () {
    $('.collapse').trigger('hidden.bs.collapse');
    expect(this.pauseVideoSpy.called).to.be.true;
  });
});
