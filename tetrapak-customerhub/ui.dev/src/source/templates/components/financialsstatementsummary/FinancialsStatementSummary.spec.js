import $ from 'jquery';
import * as jqRouter from 'jqueryrouter';
import FinancialsStatementSummary from './FinancialsStatementSummary';
import financialsStatementSummaryData from './data/financialsStatementSummary.json';
import financialStatementSummaryTemplate from '../../../test-templates-hbs/financialstatementsummary.hbs';
import { render } from '../../../scripts/utils/render';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import auth from '../../../scripts/utils/auth';

describe('FinancialsStatementSummary', function () {
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
    this.financialsStatementSummary = new FinancialsStatementSummary({
      el: document.body
    });
    $(document.body).empty().html(financialStatementSummaryTemplate());
    this.initSpy = sinon.spy(this.financialsStatementSummary, "init");
    this.renderTableSpy = sinon.spy(this.financialsStatementSummary, "renderTable");
    this.processTableData = sinon.spy(this.financialsStatementSummary, "processTableData");
    this.downloadInvoice = sinon.spy(this.financialsStatementSummary, "downloadInvoice");
    this.downloadPdfExcel = sinon.spy(this.financialsStatementSummary, "downloadPdfExcel");
    this.analyticsSpy = sinon.spy(this.financialsStatementSummary, 'trackAnalytics');
    this.renderSpy = sinon.spy(render, 'fn');
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse(financialsStatementSummaryData));
    this.openStub = sinon.stub(window, 'open');
    this.tokenStub = sinon.stub(auth, 'getToken').callsArgWith(0, {
      data: {
        access_token: "fLW1l1EA38xjklTrTa5MAN7GFmo2",
        expires_in: "43199",
        token_type: "BearerToken"
      }
    });
    this.routeStub = sinon.stub(jqRouter, 'route').callsArgWith(
      0,
      {
        hash: true
      },
      undefined,
      {
        status: 123,
        'document-type': 123,
        'invoicedate-from': '2019-04-13',
        customerkey: 123
      }
    );
    this.financialsStatementSummary.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.renderTableSpy.restore();
    this.processTableData.restore();
    this.downloadInvoice.restore();
    this.downloadPdfExcel.restore();
    this.analyticsSpy.restore();
    this.renderSpy.restore();
    this.ajaxStub.restore();
    this.openStub.restore();
    this.tokenStub.restore();
    this.routeStub.restore();
  })
  it('should initialize', function (done) {
    expect(this.financialsStatementSummary.init.called).to.be.true;
    done();
  });
  it('should render component on page load', function (done) {
    expect(render.fn.called).to.be.true;
    done();
  });
  it('should render statement summary and documents sections', function (done) {
    expect(this.financialsStatementSummary.renderTable.called).to.be.true;
    done();
  });
  it('should process data before rendering statement summary and documents sections', function (done) {
    expect(this.financialsStatementSummary.processTableData.called).to.be.true;
    done();
  });
  it('should download invoice on click of document row', function (done) {
    $('.js-financials-summary__documents__row').trigger('click');
    expect(this.financialsStatementSummary.downloadInvoice.called).to.be.true;
    done();
  });
  it('should fire analytics on invoice download', function (done) {
    $('.js-financials-summary__documents__row').trigger('click');
    expect(this.financialsStatementSummary.trackAnalytics.called).to.be.true;
    done();
  });
  it('downloadPdfExcel should be called on download excel btn click', function (done) {
    $('.js-financials-summary__create-pdf').trigger('click');
    expect(this.financialsStatementSummary.downloadPdfExcel.called).to.be.true;
    done();
  });
});
