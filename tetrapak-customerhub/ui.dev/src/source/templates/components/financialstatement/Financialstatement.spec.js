import FinancialStatement from './FinancialStatement';
import financialStatementTemplate from '../../../test-templates-hbs/financialstatement.hbs';
import { render } from '../../../scripts/utils/render';
import $ from 'jquery';


describe('FinancialStatement', function () {
  before(function () {
    $(document.body).empty().html(financialStatementTemplate());
    this.financialstatement = new FinancialStatement({ el: document.body });
    this.initSpy = sinon.spy(this.financialstatement, 'init');
    this.setSelectedCustomerSpy = sinon.spy(this.financialstatement, 'setSelectedCustomer');
    this.renderSpy = sinon.spy(render, 'fn');
    this.financialstatement.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.setSelectedCustomerSpy.restore();
    this.renderSpy.restore();
  });
  it('should initialize', function (done) {
    expect(this.financialstatement.init.called).to.be.true;
    done();
  });
  it('should render component on page load', function (done) {
    expect(render.fn.called).to.be.true;
    done();
  });
  it('should set new customer by calling setCustomer', function () {
    $('.js-financial-statement__find-customer').trigger('change');
    expect(this.financialstatement.setSelectedCustomer.called).to.be.false;
  });
});
