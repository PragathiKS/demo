import $ from 'jquery';
import FinancialsStatementSummary from './FinancialsStatementSummary';
import financialsStatementSummaryData from './data/financialsStatementSummary.json';
import financialStatementSummaryTemplate from '../../../test-templates-hbs/financialstatementsummary.hbs';
import { render } from '../../../scripts/utils/render';
import { logger } from '../../../scripts/utils/logger';
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
    this.financialsStatementSummary.init();
    $('.js-financial-statement').trigger('financialStatement');
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.renderTableSpy.restore();
    this.processTableData.restore();
    this.renderSpy.restore();
    this.ajaxStub.restore();
    this.openStub.restore();
    this.tokenStub.restore();
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
});
