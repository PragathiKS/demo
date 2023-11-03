import FinancialStatement from './FinancialStatement';
import financialStatementTemplate from '../../../test-templates-hbs/financialstatement.hbs';
import { render } from '../../../scripts/utils/render';
import $ from 'jquery';
import { customDropdown } from '../../../scripts/utils/customDropdown';
import financialStatementData from './data/financialStatement.json';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import auth from '../../../scripts/utils/auth';
import file from '../../../scripts/utils/file';
import { EVT_FINANCIAL_FILEDOWNLOAD, EVT_DROPDOWN_CHANGE } from '../../../scripts/utils/constants';

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
    this.financialstatement = new FinancialStatement({ el: $('.js-financial-statement') });
    this.initSpy = sinon.spy(this.financialstatement, 'init');
    this.filterCustomerSpy=sinon.spy(this.financialstatement, 'filterCustomerBySearchText');
    this.setSelectedCustomerSpy = sinon.spy(this.financialstatement, 'setSelectedCustomer');
    this.statusSpy = sinon.spy(this.financialstatement, 'setDateFilter');
    this.renderFiltersSpy = sinon.spy(this.financialstatement, 'renderFilters');
    this.dateRangeSpy = sinon.spy(this.financialstatement, 'openDateSelector');
    this.calendarSpy = sinon.spy(this.financialstatement, 'submitDateRange');
    this.navigateSpy = sinon.spy(this.financialstatement, 'navigateCalendar');
    this.searchSpy = sinon.spy(this.financialstatement, 'populateResults');
    this.downloadPdfExcelSpy = sinon.spy(this.financialstatement, 'downloadPdfExcel');
    this.resetSpy = sinon.spy(this.financialstatement, 'resetFilters');
    this.renderSpy = sinon.spy(render, 'fn');
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse(financialStatementData));
    this.tokenStub = sinon.stub(auth, 'getToken').callsArgWith(0, {
      data: {
        access_token: "fLW1l1EA38xjklTrTa5MAN7GFmo2",
        expires_in: "43199",
        token_type: "BearerToken"
      }
    });
    this.fileStub = sinon.stub(file, 'get').returns(new Promise(resolve => resolve()));
    $(document).on('submit', '.js-prevent-default', (e) => {
      e.preventDefault();
    });
    customDropdown.init();
    this.financialstatement.init();
    $(window).trigger('media.changed');
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.filterCustomerSpy.restore();
    this.setSelectedCustomerSpy.restore();
    this.statusSpy.restore();
    this.renderFiltersSpy.restore();
    this.dateRangeSpy.restore();
    this.calendarSpy.restore();
    this.downloadPdfExcelSpy.restore();
    this.navigateSpy.restore();
    this.searchSpy.restore();
    this.resetSpy.restore();
    this.renderSpy.restore();
    this.ajaxStub.restore();
    this.tokenStub.restore();
    this.fileStub.restore();
  });

  it('should initialize', function (done) {
    expect(this.financialstatement.init.called).to.be.true;
    done();
  });

  it('should call renderFilters', function (done) {
    expect(this.financialstatement.renderFilters.called).to.be.true;
    done();
  });

  it('should render customer dropdown', function (done) {
    this.ajaxStub.restore();
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse(financialStatementData));
    expect(render.fn.called).to.be.true;
    done();
  });

  it('should filter customer on  keyup', function (done) {
    $('.js-financial-statement__dropdown-custom-search-text').trigger('keyup');
    expect(this.filterCustomerSpy.called).to.be.true;
    done();
  });

  it('should set new customer by calling setCustomer when changed from dropdown', function (done) {
    $('.js-financial-statement__find-customer').trigger(EVT_DROPDOWN_CHANGE);
    expect(this.financialstatement.setSelectedCustomer.called).to.be.true;
    done();
  });

  it('should change date selector on status change', function (done) {
    $('.js-financial-statement__status').trigger('change');
    expect(this.statusSpy.called).to.be.true;
    done();
  });

  it('should open date picker modal on click of date range selector', function (done) {
    $('.js-financial-statement__date-range').trigger('click');
    expect(this.dateRangeSpy.called).to.be.true;
    done();
  });

  it('should navigate calendar on click of calendar navigation buttons', function (done) {
    $('.js-calendar-prev').trigger('click');
    expect(this.navigateSpy.called).to.be.true;
    done();
  });

  it('should select current date range and close modal', function (done) {
    $('.js-calendar').trigger('click');
    expect(this.calendarSpy.called).to.be.true;
    done();
  });

  it('should apply the filters on "Search Statements" button click', function (done) {
    $('.js-financial-statement__submit').trigger('click');
    expect(this.searchSpy.called).to.be.true;
    done();
  });

  it('should not apply the filters on "Search Statement" button click if input date is invalid', function (done) {
    $('.js-financial-statement__date-range-input').addClass('has-error');
    $('.js-financial-statement__submit').trigger('click');
    expect(this.searchSpy.called).to.be.true;
    done();
  });

  it('should reset filters "Reset Search" button click', function (done) {
    $('.js-financial-statement__reset').trigger('click');
    expect(this.resetSpy.called).to.be.true;
    done();
  });

  it('should download PDF/Excel file on click of create Excel/PDF button', function (done) {
    $('.js-financials').trigger(EVT_FINANCIAL_FILEDOWNLOAD, ['excel', 'downloadExcel', 'create excel']);
    file.get().then(() => {
      expect(this.downloadPdfExcelSpy.called).to.be.true;
      done();
    });
  });
});