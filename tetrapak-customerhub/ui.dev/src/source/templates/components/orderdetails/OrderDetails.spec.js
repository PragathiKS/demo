import $ from 'jquery';
import OrderDetails from './OrderDetails';
import orderDetailTemplate from '../../../test-templates-hbs/orderDetail.hbs';
import orderdetailpackagingData from './data/orderdetailpackaging.json';
import { render } from '../../../scripts/utils/render';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import auth from '../../../scripts/utils/auth';

describe('OrderDetails', function () {
  const jqRef = {
    setRequestHeader() {
      // Dummy method
    }
  };
  function ajaxResponse(response) {
    const pr = $.Deferred();
    pr.resolve(response, 'success', jqRef);
    return pr.promise();
  };
  before(function () {
    this.orderDetails = new OrderDetails({
      el: document.body
    });
    $(document.body).empty().html(orderDetailTemplate());
    this.initSpy = sinon.spy(this.orderDetails, "init");
    this.renderOrderSummarySpy = sinon.spy(this.orderDetails, "renderOrderSummary");
    this.renderPaginateDataSpy = sinon.spy(this.orderDetails, "renderPaginateData");
    this.processTableDataSpy = sinon.spy(this.orderDetails, "processTableData");
    this.openOverlaySpy = sinon.spy(this.orderDetails, 'openOverlay');
    this.downloadSpy = sinon.spy(this.orderDetails, 'downloadContent');
    this.renderSpy = sinon.spy(render, 'fn');
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse(orderdetailpackagingData));
    this.openStub = sinon.stub(window, 'open');
    this.tokenStub = sinon.stub(auth, 'getToken').callsArgWith(0, {
      data: {
        access_token: "fLW1l1EA38xjklTrTa5MAN7GFmo2",
        expires_in: "43199",
        token_type: "BearerToken"
      }
    });
    this.orderDetails.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.renderOrderSummarySpy.restore();
    this.renderPaginateDataSpy.restore();
    this.processTableDataSpy.restore();
    this.openOverlaySpy.restore();
    this.downloadSpy.restore();
    this.renderSpy.restore();
    this.ajaxStub.restore();
    this.openStub.restore();
    this.tokenStub.restore();
  });
  it('should initialize', function (done) {
    expect(this.orderDetails.init.called).to.be.true;
    done();
  });
  it('should render component on page load', function (done) {
    expect(render.fn.called).to.be.true;
    done();
  });
  it('should process data before rendering order detail summary section', function (done) {
    expect(this.orderDetails.processTableData.called).to.be.true;
    done();
  });
  it('should process `packmat` data before rendering order detail if order type is `packmat`', function (done) {
    this.orderDetails.cache.orderType = 'packmat';
    this.orderDetails.renderOrderSummary();
    expect(this.orderDetails.processTableData.called).to.be.true;
    done();
  });
  it('should render order detail summary section', function (done) {
    expect(this.orderDetails.renderOrderSummary.called).to.be.true;
    done();
  });
  it('should render delivery data on click of pagination button', function (done) {
    this.orderDetails.cache.orderType = 'parts';
    this.orderDetails.renderOrderSummary();
    $('.js-pagination-multiple:first').trigger("orderdetail.pagenav", [{
      pageNumber: 1,
      pageIndex: 0
    }]);
    expect(this.orderDetails.renderPaginateData.called).to.be.true;
    console.log($(document.body).html());
    done();
  });
  it('should open overlay when info icon clicked', function () {
    $('.js-icon-Info').trigger('click');
    expect(this.orderDetails.openOverlay.called).to.be.true;
  });
  it('should download excel on click of "download excel" button', function () {
    $('.js-create-excel').trigger('click');
    expect(this.downloadSpy.called).to.be.true;
  });
});
