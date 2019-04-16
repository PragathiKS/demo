import $ from 'jquery';
import OrderDetail from './OrderDetail';
import orderDetail from '../../../test-templates-hbs/orderDetail.hbs';
import { render } from '../../../scripts/utils/render';
import { logger } from '../../../scripts/utils/logger';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import auth from '../../../scripts/utils/auth';

describe('OrderDetail', function () {
  const jqRef = {
    setRequestHeader() {
      // Dummy method
    }
  };
  const dummyAjaxResponse = {};
  function ajaxResponse(response) {
    const pr = $.Deferred();
    pr.resolve(response, 'success', jqRef);
    return pr.promise();
  };
  before(function () {
    this.orderDetail = new OrderDetail({
      el: document.body
    });
    $(document.body).empty().html(orderDetail());
    this.initSpy = sinon.spy(this.orderDetail, "init");
    this.renderOrderSummarySpy = sinon.spy(this.orderDetail, "renderOrderSummary");
    this.processTableDataSpy = sinon.spy(this.orderDetail, "processTableData");
    this.openOverlaySpy = sinon.spy(this.orderDetail, 'openOverlay');
    this.renderSpy = sinon.spy(render, 'fn');
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse(dummyAjaxResponse));
    this.openStub = sinon.stub(window, 'open');
    this.tokenStub = sinon.stub(auth, 'getToken').callsArgWith(0, {
      data: {
        access_token: "fLW1l1EA38xjklTrTa5MAN7GFmo2",
        expires_in: "43199",
        token_type: "BearerToken"
      }
    });
    this.orderDetail.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.renderOrderSummarySpy.restore();
    this.processTableDataSpy.restore();
    this.openOverlaySpy.restore();
    this.renderSpy.restore();
    this.ajaxStub.restore();
    this.openStub.restore();
    this.tokenStub.restore();
  });
  it('should initialize', function (done) {
    expect(this.orderDetail.init.called).to.be.true;
    done();
  });
  it('should render component on page load', function (done) {
    expect(render.fn.called).to.be.true;
    done();
  });
  it('should process data before rendering order detail summary section', function (done) {
    expect(this.orderDetail.processTableData.called).to.be.true;
    done();
  });
  it('should process `packmat` data before rendering order detail if order type is `packmat`', function (done) {
    this.orderDetail.cache.orderType = 'packmat';
    this.orderDetail.renderOrderSummary();
    expect(this.orderDetail.processTableData.called).to.be.true;
    done();
  });
  it('should render order detail summary section', function (done) {
    expect(this.orderDetail.renderOrderSummary.called).to.be.true;
    done();
  });
  it('should open overlay when info icon clicked', function () {
    $('.js-icon-Info').trigger('click');
    expect(this.orderDetail.openOverlay.called).to.be.true;
  });
});
