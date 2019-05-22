import $ from 'jquery';
import * as routeExports from 'jqueryrouter';
import OrderSearch from './OrderSearch';
import orderSearchData from './data/orderSearchSummary.json';
import orderingCardData from '../orderingcard/data/orderingCardData.json';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { render } from '../../../scripts/utils/render';
import orderSearchTemplate from '../../../test-templates-hbs/ordersearch.hbs';
import auth from '../../../scripts/utils/auth';

describe('OrderSearch', function () {
  const jqRef = {
    setRequestHeader() {
      // Dummy method
    }
  };
  function ajaxResponse(response) {
    const pr = $.Deferred();
    pr.resolve(response, 'success', jqRef);
    return pr.promise();
  }
  before(function () {
    $(document.body).empty().html(orderSearchTemplate());
    this.orderSearch = new OrderSearch({ el: document.body });
    this.initSpy = sinon.spy(this.orderSearch, 'init');
    this.analyticsSpy = sinon.spy(this.orderSearch, 'trackAnalytics');
    this.resetSpy = sinon.spy(this.orderSearch, 'resetSearch');
    this.renderSpy = sinon.spy(render, 'fn');
    this.dateRangeSpy = sinon.spy(this.orderSearch, 'openRangeSelector');
    this.calendarSpy = sinon.spy(this.orderSearch, 'submitDateRange');
    this.navigateSpy = sinon.spy(this.orderSearch, 'navigateCalendar');
    this.openStub = sinon.stub(window, 'open');
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse(orderSearchData));
    this.tokenStub = sinon.stub(auth, 'getToken').callsArgWith(0, {
      data: {
        access_token: "fLW1l1EA38xjklTrTa5MAN7GFmo2",
        expires_in: "43199",
        token_type: "BearerToken"
      }
    });
    this.routeStub = sinon.stub(routeExports, 'route').callsArgWith(
      0,
      {
        hash: true
      },
      undefined,
      {
        'orderdate-from': 'YYYY-MM-DD',
        'orderdate-to': 'YYYY-MM-DD'
      }
    );
    $(document).on('submit', '.js-prevent-default', (e) => {
      e.preventDefault();
    });
    this.orderSearch.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.analyticsSpy.restore();
    this.resetSpy.restore();
    this.renderSpy.restore();
    this.dateRangeSpy.restore();
    this.calendarSpy.restore();
    this.navigateSpy.restore();
    this.openStub.restore();
    this.routeStub.restore();
    this.ajaxStub.restore();
    this.tokenStub.restore();
  });
  it('should initialize', function (done) {
    expect(this.orderSearch.init.called).to.be.true;
    done();
  });
  it('should render component on page load', function (done) {
    expect(render.fn.called).to.be.true;
    done();
  });
  it('should render table with current filter criteria on click of "filter order" button', function (done) {
    this.ajaxStub.restore();
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse(orderingCardData));
    this.routeStub.yield(
      {
        hash: true
      },
      undefined,
      {
        'orderdate-from': '2018-01-05',
        'orderdate-to': '2018-02-20',
        'search': 'test'
      }
    );
    $('.js-order-search__search-term').val('test');
    $('.js-order-search__submit').trigger('click');
    expect(render.fn.called).to.be.true;
    done();
  });
  it('should set analytics tags on click of search button', function (done) {
    $('.js-order-search__submit').trigger('click');
    expect(this.orderSearch.trackAnalytics.called).to.be.true;
    done();
  });

  it('should set analytics tags on click of reset button', function (done) {
    $('.js-order-search__reset').trigger('click');
    expect(this.orderSearch.trackAnalytics.called).to.be.true;
    done();
  });
  it('should set Analytics tags on click of order row', function () {
    $('.js-ordering-card__row').trigger('click');
    expect(this.orderSearch.trackAnalytics.called).to.be.true;
  });
  it('should reset search filters and table on click of "reset" button', function (done) {
    this.routeStub.yield(
      {
        hash: true
      },
      undefined,
      {
        'orderdate-from': 'YYYY-MM-DD',
        'orderdate-to': 'YYYY-MM-DD'
      }
    );
    $('.js-order-search__reset').trigger('click');
    expect(this.resetSpy.called).to.be.true;
    done();
  });
  it('should go to next page on click of pagination', function () {
    $('.js-pagination').trigger('ordersearch.pagenav', [{
      pageNumber: 2,
      pageIndex: 1
    }]);
    expect(render.fn.called).to.be.true;
  });
  it('should open date range selector on click of "date range" input', function () {
    $('.js-order-search__date-range').trigger('click');
    expect(this.dateRangeSpy.called).to.be.true;
  });
  it('should navigate calendar of click of calendar navigation buttons', function () {
    $('.js-calendar-nav').eq(0).trigger('click');
    expect(this.navigateSpy.called).to.be.true;
  });
  it('should select date range and close range selector on click of "Set Dates" button', function () {
    $('.js-calendar').trigger('click');
    expect(this.calendarSpy.called).to.be.true;
  });
  it('should navigate to order detail page on click of order history row', function () {
    $('.js-ordering-card__row').first().trigger('click');
    expect(this.openStub.called).to.be.true;
  });
});
