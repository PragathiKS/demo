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
    this.tabList.init();
  });
  after(function () {
    this.initSpy.restore();
  });
  it('should initialize', function () {
    expect(this.tabList.init.called).to.be.true;
  });
});
