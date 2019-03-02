import OrderingCard from './OrderingCard';
import $ from 'jquery';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { render } from '../../../scripts/utils/render';
import orderingCardData from './data/orderingCardData.json';
import orderingCardTemplate from '../../../test-templates-hbs/orderingcard.hbs';

describe('OrderingCard', function () {
  before(function () {
    const jqRef = {};
    function ajaxResponse() {
      const pr = $.Deferred();
      pr.resolve(orderingCardData, 'success', jqRef);
      return pr.promise();
    }
    $(document.body).empty().html(orderingCardTemplate());
    this.orderingCard = new OrderingCard({ el: document.body });
    this.initSpy = sinon.spy(this.orderingCard, 'init');
    this.renderSpy = sinon.spy(render, 'fn');
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj').yieldsTo('beforeSend', jqRef).returns(ajaxResponse());
    this.orderingCard.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.renderSpy.restore();
  });
  it('should initialize', function () {
    expect(this.orderingCard.init.called).to.be.true;
  });
  it('should render component on page load', function () {
    expect(render.fn.called).to.be.true;
  });
});
