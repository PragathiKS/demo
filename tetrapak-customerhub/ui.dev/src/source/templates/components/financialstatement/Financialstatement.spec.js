import FinancialStatement from './FinancialStatement';
import financialStatementTemplate from '../../../test-templates-hbs/financialstatement.hbs';
import { render } from '../../../scripts/utils/render';
import $ from 'jquery';
import financialStatementData from './data/financialStatement.json';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import auth from '../../../scripts/utils/auth';




describe('FinancialStatement', function () {
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
    $(document.body).empty().html(financialStatementTemplate());
    this.financialstatement = new FinancialStatement({ el: document.body });
    this.initSpy = sinon.spy(this.financialstatement, 'init');
    this.setSelectedCustomerSpy = sinon.spy(this.financialstatement, 'setSelectedCustomer');
    this.renderFiltersSpy = sinon.spy(this.financialstatement, 'renderFilters');
    this.renderSpy = sinon.spy(render, 'fn');
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse(financialStatementData));
    this.financialstatement.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.setSelectedCustomerSpy.restore();
    this.renderFiltersSpy.restore();
    this.renderSpy.restore();
    this.ajaxStub.restore();
  });
  it('should initialize', function (done) {
    expect(this.financialstatement.init.called).to.be.true;
    done();
  });
  it('should render component on page load', function (done) {
    expect(render.fn.called).to.be.true;
    done();
  });
  it('should call renderFilters', function (done) {
    expect(this.financialstatement.renderFilters.called).to.be.true;
    done();
  });
  it('should set new customer by calling setCustomer', function () {
    this.ajaxStub.restore();
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse(financialStatementData));
    $('.js-financial-statement__find-customer').trigger('change');
    expect(this.financialstatement.setSelectedCustomer.called).to.be.true;
  });
});
