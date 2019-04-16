import $ from 'jquery';
import OrderdetailpackagingTemplate from '../../../test-templates-hbs/orderdetailpackaging.hbs';
import OrderdetailpackagingData from './data/orderdetailpackaging.json';
import { render } from '../../../scripts/utils/render';
import Orderdetailpackaging from './Orderdetailpackaging';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import auth from '../../../scripts/utils/auth';

describe('Orderdetailpackaging', function () {
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
    $(document.body).empty().html(OrderdetailpackagingTemplate());
    this.orderdetailpackaging = new Orderdetailpackaging({ el: document.body });
    this.initSpy = sinon.spy(this.orderdetailpackaging, 'init');
    this.renderDeliveryDetailsSpy = sinon.spy(this.orderdetailpackaging, 'renderDeliveryDetails');
    this.processTableDataSpy = sinon.spy(this.orderdetailpackaging, 'processTableData');
    this.openOverlaySpy = sinon.spy(this.orderdetailpackaging, 'openOverlay');
    this.renderSpy = sinon.spy(render, 'fn');
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse(OrderdetailpackagingData));
    this.tokenStub = sinon.stub(auth, 'getToken').callsArgWith(0, {
      data: {
        access_token: "fLW1l1EA38xjklTrTa5MAN7GFmo2",
        expires_in: "43199",
        token_type: "BearerToken"
      }
    });

    this.orderdetailpackaging.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.processTableDataSpy.restore();
    this.renderDeliveryDetailsSpy.restore();
    this.openOverlaySpy.restore();
    this.renderSpy.restore();
    this.ajaxStub.restore();
    this.tokenStub.restore();
  });
  it('should initialize', function () {
    expect(this.orderdetailpackaging.init.called).to.be.true;
  });

  it('should call renderDeliveryDetails', function () {
    this.ajaxStub.restore();
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse(OrderdetailpackagingData));
    expect(render.fn.called).to.be.true;
  });

  it('should call processTableData', function () {
    expect(this.orderdetailpackaging.processTableData.called).to.be.true;
  });

  it('should open overlay when info icon clicked', function () {
    $('.js-icon-Info').trigger('click');
    expect(this.orderdetailpackaging.openOverlay.called).to.be.true;
  });
});
