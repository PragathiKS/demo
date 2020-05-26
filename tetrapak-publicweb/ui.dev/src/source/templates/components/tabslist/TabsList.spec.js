import $ from 'jquery';
import TabsList from './TabsList';
import tabslistTemplate from '../../../test-templates-hbs/tabslist.hbs';
import { trackAnalytics } from '../../../scripts/utils/analytics';
import { pauseVideosByReference} from '../../../scripts/utils/videoAnalytics';

describe('TabsList', function () {
  before(function () {
    $(document.body).empty().html(tabslistTemplate());
    this.tabslist = new TabsList({ el: document.body });
    this.initSpy = sinon.spy(this.tabslist, 'init');
    this.renderFirstTabSpy = sinon.spy(this.tabslist, 'renderFirstTab');
    this.showTabDetailSpy = sinon.spy(this.tabslist, 'showTabDetail');
    this.pauseVideoIfExistsSpy = sinon.spy(this.tabslist, 'pauseVideoIfExists');

    this.trackAnalyticsSpy = sinon.spy(this.tabslist, 'trackAnalytics');
    window.digitalData = {};
    window._satellite = {
      track() { /* Dummy method */ }
    };
    this.tabslist.init();
    this.tabslist.pauseVideoIfExists();
  });

  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.trackAnalyticsSpy.restore();
    this.showTabDetailSpy.restore();
    this.pauseVideoIfExistsSpy.restore();
  });

  it('should initialize', function () {
    expect(this.tabslist.init.called).to.be.true;
  });

  
});
