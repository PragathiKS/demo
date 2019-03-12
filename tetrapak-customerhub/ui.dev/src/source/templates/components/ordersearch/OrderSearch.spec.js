import $ from 'jquery';
import OrderSearch from './OrderSearch';
import { render } from '../../../scripts/utils/render';
import orderSearchTemplate from '../../../test-templates-hbs/ordersearch.hbs';

describe('OrderSearch', function () {
  before(function () {
    $(document.body).empty().html(orderSearchTemplate());
    this.orderSearch = new OrderSearch({ el: document.body });
    this.initSpy = sinon.spy(this.orderSearch, 'init');
    this.renderSpy = sinon.spy(render, 'fn');
    this.orderSearch.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.renderSpy.restore();
  });
  it('should initialize', function () {
    expect(this.orderSearch.init.called).to.be.true;
  });
  it('should render component on page load', function () {
    expect(render.fn.called).to.be.true;
  });
});
