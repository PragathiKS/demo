import $ from 'jquery';
import * as routeExports from 'jqueryrouter';
import OrderSearch from './OrderSearch';
import orderSearchData from './data/orderSearchSummary.json';
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
    this.renderSpy = sinon.spy(render, 'fn');
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse(orderSearchData));
    this.routeStub = sinon.stub(routeExports, 'route').callsArgWith(0, {
      hash: true
    });
    this.orderSearch.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.renderSpy.restore();
    this.routeStub.restore();
    this.ajaxStub.restore();
  });
  it('should initialize', function () {
    expect(this.orderSearch.init.called).to.be.true;
  });
  it('should render component on page load', function () {
    expect(render.fn.called).to.be.true;
  });
});
