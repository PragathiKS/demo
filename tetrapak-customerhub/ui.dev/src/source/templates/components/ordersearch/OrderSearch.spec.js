import $ from 'jquery';
import * as routeExports from 'jqueryrouter';
import OrderSearch from './OrderSearch';
import orderSearchData from './data/orderSearchSummary.json';
import orderingCardData from '../orderingcard/data/orderingCardData.json';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { render } from '../../../scripts/utils/render';
import orderSearchTemplate from '../../../test-templates-hbs/ordersearch.hbs';

describe('OrderSearch', function () {
  const jqRef = {};
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
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse(orderSearchData));
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
    this.orderSearch.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.analyticsSpy.restore();
    this.resetSpy.restore();
    this.renderSpy.restore();
    this.routeStub.restore();
    this.ajaxStub.restore();
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
        'orderdate-from': 'YYYY-MM-DD',
        'orderdate-to': 'YYYY-MM-DD',
        'search': 'test'
      }
    );
    $('.js-order-search__search-term').val('test');
    $('.js-order-search__submit').trigger('click');
    expect(render.fn.called).to.be.true;
    done();
  });
  it('should set analytics tags on click of search button', function (done) {
    expect(this.orderSearch.trackAnalytics.called).to.be.true;
    done();
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
});
