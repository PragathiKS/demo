import Financialsearch from './Financialsearch';
import financialSearchTemplate from '../../../test-templates-hbs/financialsearch.hbs';
import { render } from '../../../scripts/utils/render';
import $ from 'jquery';


describe('Financialsearch', function () {
  before(function () {
    $(document.body).empty().html(financialSearchTemplate());
    this.financialsearch = new Financialsearch({ el: document.body });
    this.initSpy = sinon.spy(this.financialsearch, 'init');
    this.setSelectedCustomerSpy = sinon.spy(this.financialsearch, 'setSelectedCustomer');
    this.renderSpy = sinon.spy(render, 'fn');
    this.financialsearch.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.setSelectedCustomerSpy.restore();
    this.renderSpy.restore();
  });
  it('should initialize', function (done) {
    expect(this.financialsearch.init.called).to.be.true;
    done();
  });
  it('should render component on page load', function (done) {
    expect(render.fn.called).to.be.true;
    done();
  });
  it('should set new customer by calling setCustomer', function () {
    $('.js-financial-search__find-customer').trigger('change');
    expect(this.financialsearch.setSelectedCustomer.called).to.be.false;
  });
});
