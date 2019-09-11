import $ from 'jquery';
import * as jqRouter from 'jqueryrouter';
import FinancialsStatementSummary from './FinancialsStatementSummary';
import financialsStatementSummaryData from './data/financialsStatementSummary.json';
import financialStatementSummaryTemplate from '../../../test-templates-hbs/financialstatementsummary.hbs';
import { render } from '../../../scripts/utils/render';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import auth from '../../../scripts/utils/auth';
import file from '../../../scripts/utils/file';

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
    this.initSpy = sinon.spy(this.financialsStatementSummary, 'init');
    this.renderTableSpy = sinon.spy(this.financialsStatementSummary, 'renderTable');
    this.processTableData = sinon.spy(this.financialsStatementSummary, 'processTableData');
    this.downloadInvoice = sinon.spy(this.financialsStatementSummary, 'downloadInvoice');
    this.downloadPdfExcel = sinon.spy(this.financialsStatementSummary, 'downloadPdfExcel');
    this.analyticsSpy = sinon.spy(this.financialsStatementSummary, 'trackAnalytics');
    this.errorSpy = sinon.spy(this.financialsStatementSummary, 'trackErrors');
    this.renderSpy = sinon.spy(render, 'fn');
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse(financialsStatementSummaryData));
    this.openStub = sinon.stub(window, 'open');
    this.tokenStub = sinon.stub(auth, 'getToken').callsArgWith(0, {
      data: {
        access_token: 'fLW1l1EA38xjklTrTa5MAN7GFmo2',
        expires_in: '43199',
        token_type: 'BearerToken'
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
    this.fileStub = sinon.stub(file, 'get').returns(new Promise(resolve => resolve()));
    this.financialsStatementSummary.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.renderTableSpy.restore();
    this.processTableData.restore();
    this.downloadInvoice.restore();
    this.downloadPdfExcel.restore();
    this.renderSpy.restore();
    this.analyticsSpy.restore();
    this.errorSpy.restore();
    this.ajaxStub.restore();
    this.openStub.restore();
    this.tokenStub.restore();
    this.routeStub.restore();
    this.fileStub.restore();
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
    file.get().then(() => {
      $('.js-financials-summary__documents__row').first().trigger('click');
      expect(this.financialsStatementSummary.downloadInvoice.called).to.be.true;
      done();
    });
  });
  it('downloadPdfExcel should be called on download excel btn click', function (done) {
    file.get().then(() => {
      $('.js-financials-summary__create-pdf').first().trigger('click');
      $('.js-financials-summary__create-excel').first().trigger('click');
      expect(this.financialsStatementSummary.downloadPdfExcel.called).to.be.true;
      done();
    });
  });
  it('should track analytics on accordion expansion', function (done) {
    $('.js-financials-summary__accordion.collapsed').trigger('click');
    expect(this.analyticsSpy.called).to.be.true;
    done();
  });
  it('should track error analytics on error scenarios', function (done) {
    $(document.body).trigger('financial.error', ['test', 'invoice download', 'error']);
    expect(this.errorSpy.called).to.be.true;
    done();
  });
});
