import OrderingCard from './OrderingCard';
import $ from 'jquery';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
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
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj').yieldsTo('beforeSend', jqRef).returns(ajaxResponse());
    this.orderingCard.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
  });
  it('should initialize', function () {
    expect(this.orderingCard.init.called).to.be.true;
  })
});
