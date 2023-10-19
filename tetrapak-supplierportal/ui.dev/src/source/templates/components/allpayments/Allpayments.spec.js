import Allpayments from './Allpayments';
import allpaymentsTmpl from '../../../test-templates-hbs/allpayments.hbs';
import auth from '../../../scripts/utils/auth';
import allpaymentData from './data/allpayments.json';
import statusMappingData from './data/statusmapping.json';
import allpaymentFilterData from './data/allpaymentsfilter.json';
import {_getFilterDateRange} from './allpayments.utils';
import AllPaymentsFilter from './allpayments.filter';
import filterFormTmpl from '../../../test-templates-hbs/filterForm.hbs';
import filterFormInvoiceDateTmpl from '../../../test-templates-hbs/filterFormInvoiceDate.hbs';
import filterFormInvoiceNoTmpl from '../../../test-templates-hbs/filterFormInvoiceNo.hbs';
import filterFormCompanyTmpl from '../../../test-templates-hbs/filterFormCompany.hbs';
import AllPaymentsDateRange from './allpayments.date-range';

describe('Allpayments', function () {
  let fetchStub, windowStub;
  const statusApiUrl = '/content/tetrapak/supplierportal/global/en/testpagenew/jcr:content/root/responsivegrid/allpayments.invoice.status.json';
  let filterApiUrl = 'https://api-dev.tetrapak.com/supplierpayments/filters';

  beforeEach(function () {
    this.enableTimeouts(false);
    document.body.innerHTML = allpaymentsTmpl();
    const modalContainer = document.body.querySelector('.js-filter-modal');
    modalContainer.innerHTML = filterFormTmpl();
    const tagContainer = document.body.querySelector('.tagContainer');
    tagContainer.innerHTML = `<button class="tp-all-payments__tag">
        <span class="tpatom-btn__text sly-text js-field" data-key="invoiceDates" data-value="2023-07-20 - 2023-10-19">2023-07-20 - 2023-10-19</span>
        <i class="icon icon-Close_new"></i>
    </button>`;
    this.allpayments = new Allpayments({
      el: document.body.querySelector('.tp-all-payments')
    });
    this.allpaymentsFilter = new AllPaymentsFilter({}, document.body.querySelector('.tp-all-payments'));
    this.allPaymentsDateRange = new AllPaymentsDateRange({modal: modalContainer}, document.body.querySelector('.tp-all-payments'));

    this.initSpy = sinon.spy(this.allpayments, 'init');
    this.bindEventsSpy = sinon.spy(this.allpayments, 'bindEvents');
    this.renderPaymentSpy = sinon.spy(this.allpayments, 'renderPayment');
    this.paginationActionSpy = sinon.spy(this.allpayments, 'paginationAction');
    this.getPaymentApiUrlSpy = sinon.spy(this.allpayments, 'getPaymentApiUrl');
    this.sortActionSpy = sinon.spy(this.allpayments, 'sortAction');
    this.renderErrorTemplateSpy = sinon.spy(this.allpayments, 'renderErrorTemplate');
    this.renderFilterFormSpy = sinon.spy(this.allpayments, 'renderFilterForm');
    this.applyFilterSpy = sinon.spy(this.allpayments, 'applyFilter');
    this.resetFilterSpy = sinon.spy(this.allpaymentsFilter, 'resetFilter');
    this.getAllFilterSpy = sinon.spy(this.allpaymentsFilter, 'getAllFilters');
    this.getPaymentFiltersApiUrlSpy = sinon.spy(this.allpaymentsFilter, 'getPaymentFiltersApiUrl');
    this.renderTagsSpy = sinon.spy(this.allpaymentsFilter, 'renderTags');
    // this.inputMaskUtilSpy = sinon.spy(this.allPaymentsDateRange, 'inputMaskUtil');
    this.tokenStub = sinon.stub(auth, 'getToken').callsArgWith(0, {
      data: {
        access_token: 'fLW1l1EA38xjklTrTa5MAN7GFmo2',
        expires_in: '43199',
        token_type: 'BearerToken'
      }
    });
    const [fromDate, toDate] = _getFilterDateRange(90, true);
    filterApiUrl += `?fromdatetime=${fromDate}&todatetime=${toDate}`;
    fetchStub = sinon.stub(window, 'fetch');
    fetchStub.withArgs(statusApiUrl).resolves({ json: sinon.stub().resolves(statusMappingData) });
    fetchStub.withArgs(filterApiUrl).resolves({ json: sinon.stub().resolves(allpaymentFilterData) });
    windowStub = sinon.stub(window, 'open');
  });

  afterEach(function () {
    document.body.innerHTML = '';
    this.initSpy.restore();
    this.bindEventsSpy.restore();
    this.renderPaymentSpy.restore();
    this.tokenStub.restore();
    this.paginationActionSpy.restore();
    this.renderErrorTemplateSpy.restore();
    this.renderFilterFormSpy.restore();
    this.applyFilterSpy.restore();
    this.resetFilterSpy.restore();
    this.getAllFilterSpy.restore();
    this.getPaymentFiltersApiUrlSpy.restore();
    this.renderTagsSpy.restore();
    // this.inputMaskUtilSpy.restore();
    fetchStub.restore();
    sinon.restore();
    windowStub.restore();
  });

  it('should initialize', async function(){
    fetchStub.resolves({ json: () => Promise.resolve(allpaymentData)});
    this.allpayments.init();

    await this.allpayments.renderPayment();
    expect(this.allpayments.init.called).to.be.true;
    expect(this.allpayments.bindEvents.called).to.be.true;
  });

  it('should get list of allpayments', async function(){
    fetchStub.resolves({ json: () => Promise.resolve(allpaymentData)});
    this.allpayments.init();

    expect(this.allpayments.renderPayment.called).to.be.true;
  });

  it('should get next/prev page data', async function(){
    fetchStub.resolves({ json: () => Promise.resolve(allpaymentData)});
    this.allpayments.init();

    const evt = new MouseEvent('click', {
      bubbles: true,
      cancelable: true,
      view: window
    });
    document.querySelector('.js-page-number').dispatchEvent(evt);

    expect(this.allpayments.paginationAction.called).to.be.true;
    expect(this.allpayments.renderPayment.called).to.be.true;
  });

  it('should user can go last page when click on last button', async function(){
    fetchStub.resolves({ json: () => Promise.resolve(allpaymentData)});
    this.allpayments.init();

    const evt = new MouseEvent('click', {
      bubbles: true,
      cancelable: true,
      view: window
    });
    document.querySelector('.js-page-number.tp-tbl-pagination__last').dispatchEvent(evt);
    expect(this.allpayments.paginationAction.called).to.be.true;
    expect(this.allpayments.renderPayment.called).to.be.true;

    // Fetch data always same so now also the active pagenumber 2
    expect(document.querySelector('.js-page-number.active').getAttribute('data-page-number')).to.equal('1');
  });

  it('should user can go next page when click on next button', async function(){
    fetchStub.resolves({ json: () => Promise.resolve(allpaymentData)});
    this.allpayments.init();

    const evt = new MouseEvent('click', {
      bubbles: true,
      cancelable: true,
      view: window
    });
    document.querySelector('.js-page-number.tp-tbl-pagination__next').dispatchEvent(evt);
    expect(this.allpayments.paginationAction.called).to.be.true;
    expect(this.allpayments.renderPayment.called).to.be.true;

    // Fetch data always same so now also the active pagenumber 2
    expect(document.querySelector('.js-page-number.active').getAttribute('data-page-number')).to.equal('1');
  });

  it('should user can go next page when click on prev button', async function(){
    fetchStub.resolves({ json: () => Promise.resolve(allpaymentData)});
    this.allpayments.init();

    const evt = new MouseEvent('click', {
      bubbles: true,
      cancelable: true,
      view: window
    });
    document.querySelector('.js-page-number.tp-tbl-pagination__prev').dispatchEvent(evt);
    expect(this.allpayments.paginationAction.called).to.be.true;
    expect(this.allpayments.renderPayment.called).to.be.true;

    // Fetch data always same so now also the active pagenumber 2
    expect(document.querySelector('.js-page-number.active').getAttribute('data-page-number')).to.equal('1');
  });

  it('should user can go the specific page when click on page number button', async function(){
    fetchStub.resolves({ json: () => Promise.resolve(allpaymentData)});
    this.allpayments.init();
    this.allpayments.cache.itemsPerPage = 1;
    const evt = new MouseEvent('click', {
      bubbles: true,
      cancelable: true,
      view: window
    });
    document.querySelector('.js-page-number.tp-tbl-pagination__direct').dispatchEvent(evt);
    expect(this.allpayments.paginationAction.called).to.be.true;
    expect(this.allpayments.renderPayment.called).to.be.true;

    // Fetch data always same so now also the active pagenumber 2
    expect(document.querySelector('.js-page-number.active').getAttribute('data-page-number')).to.equal('1');
  });

  it('should able to sort the data', async function(){
    fetchStub.resolves({ json: () => Promise.resolve(allpaymentData)});
    this.allpayments.init();

    const evt = new MouseEvent('click', {
      bubbles: true,
      cancelable: true,
      view: window
    });
    document.querySelector('.js-all-payments__table-summary__sort').dispatchEvent(evt);

    expect(this.allpayments.sortAction.called).to.be.true;
    document.querySelector('.js-all-payments__table-summary__sort').dispatchEvent(evt);
    expect(this.allpayments.renderPayment.called).to.be.true;
  });

  it('Check the item per page is changed, the pagination work as expect', async function(){
    fetchStub.resolves({ json: () => Promise.resolve(allpaymentData)});
    this.allpayments.init();
    this.allpayments.itemsPerPage = 1;

    const evt = new MouseEvent('click', {
      bubbles: true,
      cancelable: true,
      view: window
    });
    document.querySelector('.js-all-payments__table-summary__sort').dispatchEvent(evt);

    expect(this.allpayments.sortAction.called).to.be.true;
    document.querySelector('.js-all-payments__table-summary__sort').dispatchEvent(evt);
    expect(this.allpayments.renderPayment.called).to.be.true;
  });

  it('should show the no data found if there is no response', async function(){
    const emptyResult = {
      'meta': {
        'count': 500,
        'skip': 0,
        'total': 0,
        'version': '1'
      },
      'data': []
    };

    fetchStub.resolves({ json: () => Promise.resolve(emptyResult)});
    this.allpayments.init();

    expect(this.allpayments.renderPayment.called).to.be.true;
  });

  it('should handle fetch error', async function() {
    const errorMessage = 'Failed to fetch data';

    // Set up the stub to return a rejected promise with the error message
    fetchStub.rejects(new Error(errorMessage));
    try {
      await this.allpayments.init();
    } catch (error) {
      expect(this.allpayments.renderErrorTemplate.called).to.be.true;
    }
  });
  it('should handle show hide link', async function() {
    fetchStub.resolves({ json: () => Promise.resolve(allpaymentData)});
    this.allpayments.init();
    this.allpayments.itemsPerPage = 1;

    const evt = new MouseEvent('click', {
      bubbles: true,
      cancelable: true,
      view: window
    });
    document.querySelector('.tp-all-payments__table-summary__body .tp-all-payments__table-summary__row').dispatchEvent(evt);
    expect(windowStub.called).to.be.true;
  })
  it('should render filter form', async function() {
    fetchStub.resolves({ json: () => Promise.resolve(allpaymentData)});
    this.allpayments.init();
    this.allpayments.itemsPerPage = 1;

    const evt = new MouseEvent('click', {
      bubbles: true,
      cancelable: true,
      view: window
    });
    document.querySelector('.js-all-payments__customise-table-action').dispatchEvent(evt);
    expect(this.allpayments.renderFilterForm.called).to.be.true;
  })
  it('should handle checkout groupe change event', async function() {
    fetchStub.resolves({ json: () => Promise.resolve(allpaymentData)});
    this.allpayments.init();
    this.allpayments.itemsPerPage = 1;

    const evt = new MouseEvent('change', {
      bubbles: true,
      cancelable: true,
      view: window
    });
    document.querySelector('.js-tp-all-payments-filter-group-checkbox').dispatchEvent(evt);
    expect(document.querySelectorAll('.tpatom-checkbox__input')).to.exist;
  });
  it('should handle checkbox item change event', async function() {
    fetchStub.resolves({ json: () => Promise.resolve(allpaymentData)});
    this.allpayments.init();
    this.allpayments.itemsPerPage = 1;

    const evt = new MouseEvent('change', {
      bubbles: true,
      cancelable: true,
      view: window
    });
    document.querySelector('.tp-all-payments-group-filter-options .js-tp-all-payments-filter-checkbox').dispatchEvent(evt);
    expect(document.querySelectorAll('.tpatom-checkbox__text js-checkbox__text')).to.exist;
  });
  it('should handle apply filter button', async function() {
    fetchStub.resolves({ json: () => Promise.resolve(allpaymentData)});
    this.allpayments.init();
    this.allpayments.itemsPerPage = 1;

    const evt = new MouseEvent('click', {
      bubbles: true,
      cancelable: true,
      view: window
    });
    document.querySelector('.js-apply-filter-button').dispatchEvent(evt);
    expect(this.allpayments.applyFilter.called).to.be.true;
  });
  it('should handle apply filter button', async function() {
    fetchStub.resolves({ json: () => Promise.resolve(allpaymentData)});
    this.allpayments.init();
    this.allpayments.itemsPerPage = 1;

    const evt = new MouseEvent('click', {
      bubbles: true,
      cancelable: true,
      view: window
    });
    document.querySelector('.js-mobile-header-actions').dispatchEvent(evt);
    expect(document.querySelector('.tp-all-payments__header-actions')).to.exist;
  });

  // Filter Spec
  it('should user can show the modal, when click on the filter chip button', async function() {
    fetchStub.resolves({ json: () => Promise.resolve(allpaymentData)});
    const modalContainer = document.body.querySelector('.js-filter-modal');
    modalContainer.innerHTML = filterFormInvoiceDateTmpl();

    this.allpayments.init();
    this.allpayments.itemsPerPage = 1;
    const evt = new MouseEvent('click', {
      bubbles: true,
      cancelable: true,
      view: window
    });
    document.querySelector('.tp-all-payments__filter-button').dispatchEvent(evt);
    expect(document.querySelector('.tp-all-payments-filter')).to.exist;
  });

  it('auto focus the first input box, when open the invoice no modal', async function() {
    fetchStub.resolves({ json: () => Promise.resolve(allpaymentData)});
    this.allpayments.init();
    this.allpayments.itemsPerPage = 1;
    const modalContainer = document.body.querySelector('.js-filter-modal');
    modalContainer.innerHTML = filterFormInvoiceNoTmpl();

    const evt = new MouseEvent('click', {
      bubbles: true,
      cancelable: true,
      view: window
    });
    await document.querySelector('.tp-all-payments__filter-button[data-key="documentReferenceIDs"]').dispatchEvent(evt);
    expect(document.querySelector('.tp-all-payments-filter')).to.exist;
    document.querySelector('input[type="text"]').value = 'Test';
    document.querySelector('.js-apply-filter-button').dispatchEvent(evt);
    expect(modalContainer.getAttribute('aria-hidden')).to.equal('true');
  });

  it('should close the modal, when we click on the close button in invoice no modal', async function() {
    fetchStub.resolves({ json: () => Promise.resolve(allpaymentData)});
    this.allpayments.init();
    this.allpayments.itemsPerPage = 1;
    const modalContainer = document.body.querySelector('.js-filter-modal');
    modalContainer.innerHTML = filterFormInvoiceNoTmpl();

    const evt = new MouseEvent('click', {
      bubbles: true,
      cancelable: true,
      view: window
    });
    document.querySelector('.tp-all-payments__filter-button[data-key="documentReferenceIDs"]').dispatchEvent(evt);
    document.querySelector('.js-close-btn').dispatchEvent(evt);
    expect(modalContainer.getAttribute('aria-hidden')).to.equal('true');
  });

  it('should display the date range when we click on the Other option in invoice date modal', async function() {
    fetchStub.resolves({ json: () => Promise.resolve(allpaymentData)});
    this.allpayments.init();
    this.allpayments.itemsPerPage = 1;
    const modalContainer = document.body.querySelector('.js-filter-modal');
    modalContainer.innerHTML = filterFormInvoiceDateTmpl();
    const evt = new MouseEvent('click', {
      bubbles: true,
      cancelable: true,
      view: window
    });
    document.getElementById('tpatomRadioother').dispatchEvent(evt);
    expect(document.querySelector('.js-data-range-wrapper').classList.contains('show')).to.be.true;
    document.querySelector('.js-date-range-input-from').value = '2022-11-11';
    document.querySelector('.js-apply-filter-button').dispatchEvent(evt);
    expect(modalContainer.getAttribute('aria-hidden')).to.equal('true');
  });

  it('Modal popup should be closed when we click on apply filter in invoice date modal', async function() {
    fetchStub.resolves({ json: () => Promise.resolve(allpaymentData)});
    this.allpayments.init();
    this.allpayments.itemsPerPage = 1;
    const modalContainer = document.body.querySelector('.js-filter-modal');
    modalContainer.innerHTML = filterFormInvoiceDateTmpl();
    document.querySelector('#tpatomRadiolast90Days').checked = true;
    const evt = new MouseEvent('click', {
      bubbles: true,
      cancelable: true,
      view: window
    });
    document.querySelector('.js-apply-filter-button').dispatchEvent(evt);
    expect(modalContainer.getAttribute('aria-hidden')).to.equal('true');
  });

  it('should clear the text when we click on the date range X icon in the input box in invoice date modal', async function() {
    fetchStub.resolves({ json: () => Promise.resolve(allpaymentData)});
    this.allpayments.init();
    this.allpayments.itemsPerPage = 1;
    const modalContainer = document.body.querySelector('.js-filter-modal');
    modalContainer.innerHTML = filterFormInvoiceDateTmpl();
    document.querySelector('.js-date-range-input-from').value = '2022-11-11';
    const evt = new MouseEvent('click', {
      bubbles: true,
      cancelable: true,
      view: window
    });
    document.querySelector('.js-date-range-clear-icon').dispatchEvent(evt);
    expect(document.querySelector('.js-date-range-input-from').value).to.equal('');
  });

  it('Should user can submit the modal, when enter key pressed from input box in invoice no modal', async function() {
    fetchStub.resolves({ json: () => Promise.resolve(allpaymentData)});
    this.allpayments.init();
    const modalContainer = document.body.querySelector('.js-filter-modal');
    modalContainer.innerHTML = filterFormInvoiceNoTmpl();
    this.allpayments.itemsPerPage = 1;
    document.querySelector('.js-tp-all-payments-filter-input').focus();
    const event = new KeyboardEvent('keydown', {
      key: 'Enter',
      code: 'Enter',
      which: 13,
      keyCode: 13,
      target: modalContainer.querySelector('.js-tp-all-payments-filter-input')
    });
    const evt = new MouseEvent('click', {
      bubbles: true,
      cancelable: true,
      view: window
    });
    document.querySelector('.tp-all-payments__filter-button[data-key="documentReferenceIDs"]').dispatchEvent(evt);
    modalContainer.dispatchEvent(event);
    expect(document.querySelectorAll('.tp-all-payments__tag').length).to.equal(1);
  });

  it('Modal popup should be closed when we click on apply filter in company modal', async function() {
    fetchStub.resolves({ json: () => Promise.resolve(allpaymentData)});
    this.allpayments.init();
    this.allpayments.itemsPerPage = 1;
    const modalContainer = document.body.querySelector('.js-filter-modal');
    modalContainer.innerHTML = filterFormCompanyTmpl();
    document.querySelector('#tpatomCheck0111').checked = true;
    const evt = new MouseEvent('click', {
      bubbles: true,
      cancelable: true,
      view: window
    });
    document.querySelector('.js-apply-filter-button').dispatchEvent(evt);
    expect(modalContainer.getAttribute('aria-hidden')).to.equal('true');
  });
  it('should user can reset the filter, when click on the reset button', async function() {
    fetchStub.resolves({ json: () => Promise.resolve(allpaymentData)});
    this.allpayments.init();
    this.allpayments.itemsPerPage = 1;

    const evt = new MouseEvent('click', {
      bubbles: true,
      cancelable: true,
      view: window
    });
    document.querySelector('.tp-all-payments__reset-button').dispatchEvent(evt);
    expect(document.querySelectorAll('.tp-all-payments__tag').length).to.equal(1);
  });

  // Tag Spec
  it('should not remove the tag, when user click on the default tag', async function() {
    fetchStub.resolves({ json: () => Promise.resolve(allpaymentData)});
    this.allpayments.init();
    this.allpayments.itemsPerPage = 1;
    const modalContainer = document.body.querySelector('.tagContainer');
    modalContainer.innerHTML = `<button class="tp-all-payments__tag">
        <span class="tpatom-btn__text sly-text js-field" data-key="invoiceDates" data-value="2023-07-20 - 2023-10-19">2023-07-20 - 2023-10-19</span>
        <i class="icon icon-Close_new"></i>
    </button>`;
    const evt = new MouseEvent('click', {
      bubbles: true,
      cancelable: true,
      view: window
    });
    document.querySelector('.tp-all-payments__tag').dispatchEvent(evt);
    expect(document.body.querySelector('.tagContainer').childNodes.length).to.equal(1);
  });

  it('should the tag, when user click on the tag', async function() {
    fetchStub.resolves({ json: () => Promise.resolve(allpaymentData)});
    this.allpayments.init();
    this.allpayments.itemsPerPage = 1;
    const modalContainer = document.body.querySelector('.tagContainer');
    modalContainer.innerHTML = `<button class="tp-all-payments__tag js-tag">
        <span class="tpatom-btn__text sly-text js-field" data-key="invoiceDates" data-value="2023-07-20 - 2023-10-19">2023-07-20 - 2023-10-19</span>
        <i class="icon icon-Close_new"></i>
    </button>`;
    const evt = new MouseEvent('click', {
      bubbles: true,
      cancelable: true,
      view: window
    });
    document.querySelector('.js-tag').dispatchEvent(evt);
    expect(document.body.querySelector('.tagContainer').childNodes.length).to.equal(0);
  });

  it('should remove the tag, when user click on the X icon in the tag', async function() {
    fetchStub.resolves({ json: () => Promise.resolve(allpaymentData)});
    this.allpayments.init();
    this.allpayments.itemsPerPage = 1;
    const modalContainer = document.body.querySelector('.tagContainer');
    modalContainer.innerHTML = `<button class="tp-all-payments__tag active-tag js-tag">
      <span class="tpatom-btn__text sly-text js-field" data-key="documentReferenceIDs" data-value="test">test</span>
      <i class="icon icon-Close_new"></i>
    </button>`;
    const evt = new MouseEvent('click', {
      bubbles: true,
      cancelable: true,
      view: window
    });
    document.querySelector('.js-tag').dispatchEvent(evt);
    expect(document.body.querySelector('.tagContainer').childNodes.length).to.equal(0);
  });
  // validate the date range
  it('should validate the to date range, when we click apply filter button invoice date modal', async function() {
    fetchStub.resolves({ json: () => Promise.resolve(allpaymentData)});
    this.allpayments.init();
    this.allpayments.itemsPerPage = 1;
    const modalContainer = document.body.querySelector('.js-filter-modal');
    modalContainer.innerHTML = filterFormInvoiceDateTmpl();
    document.querySelector('#tpatomRadioother').checked = true;
    document.querySelector('.js-date-range-input-from').value = '';
    document.querySelector('.js-date-range-input-to').value = '2022-11-10';
    // document.querySelector('.js-date-range-input-from').focus();
    const evt = new MouseEvent('click', {
      bubbles: true,
      cancelable: true,
      view: window
    });
    document.querySelector('.js-apply-filter-button').dispatchEvent(evt);
    expect(modalContainer.getAttribute('aria-hidden')).to.equal('true');
    // expect(document.querySelector('.js-date-range-to-error').classList.contains('hide')).to.be.false;
  });
  it('should validate the from and to date range, when we click apply filter button invoice date modal', async function() {
    fetchStub.resolves({ json: () => Promise.resolve(allpaymentData)});
    this.allpayments.init();
    this.allpayments.itemsPerPage = 1;
    const modalContainer = document.body.querySelector('.js-filter-modal');
    modalContainer.innerHTML = filterFormInvoiceDateTmpl();
    document.querySelector('#tpatomRadioother').checked = true;
    document.querySelector('.js-date-range-input-from').value = '2022-11-09';
    document.querySelector('.js-date-range-input-to').value = '2022-11-10';
    // document.querySelector('.js-date-range-input-from').focus();
    const evt = new MouseEvent('click', {
      bubbles: true,
      cancelable: true,
      view: window
    });
    document.querySelector('.js-apply-filter-button').dispatchEvent(evt);
    expect(modalContainer.getAttribute('aria-hidden')).to.equal('true');
  });

  it('should validate the from and to date range, when we click apply filter button invoice date modal', async function() {
    fetchStub.resolves({ json: () => Promise.resolve(allpaymentData)});
    this.allpayments.init();
    this.allpayments.itemsPerPage = 1;
    const modalContainer = document.body.querySelector('.js-filter-modal');
    modalContainer.innerHTML = filterFormInvoiceDateTmpl();
    document.querySelector('#tpatomRadioother').checked = true;
    const evt = new MouseEvent('click', {
      bubbles: true,
      cancelable: true,
      view: window
    });
    document.querySelector('#tpatomRadiolast90Days').dispatchEvent(evt);
    expect(document.querySelector('.js-data-range-wrapper').classList.contains('show')).to.be.false;
  });

  it('should validate the from and to date range, when we click apply filter button invoice date modal', async function() {
    fetchStub.resolves({ json: () => Promise.resolve(allpaymentData)});
    this.allpayments.init();
    this.allpayments.itemsPerPage = 1;
    const modalContainer = document.body.querySelector('.js-filter-modal');
    modalContainer.innerHTML = filterFormInvoiceDateTmpl();
    document.querySelector('#tpatomRadioother').checked = true;
    const evt = new MouseEvent('click', {
      bubbles: true,
      cancelable: true,
      view: window
    });
    document.querySelector('#tpatomRadiolast90Days').dispatchEvent(evt);
    expect(document.querySelector('.js-data-range-wrapper').classList.contains('show')).to.be.false;
  });

  it('Should throw error, when user type wrong input value in from date', function() {
    fetchStub.resolves({ json: () => Promise.resolve(allpaymentData)});
    this.allpayments.init();
    this.allpayments.itemsPerPage = 1;
    const modalContainer = document.body.querySelector('.js-filter-modal');
    modalContainer.innerHTML = filterFormInvoiceDateTmpl();
    document.querySelector('#tpatomRadioother').checked = true;
    document.querySelector('.js-date-range-input-from').value = '2022-22-22';
    this.allPaymentsDateRange.validateDateRange();
    expect(document.querySelector('.js-date-range-from-error').classList.contains('hide')).to.be.false;
  });

  it('Should throw error, when user type wrong input value in to date', function() {
    fetchStub.resolves({ json: () => Promise.resolve(allpaymentData)});
    this.allpayments.init();
    this.allpayments.itemsPerPage = 1;
    const modalContainer = document.body.querySelector('.js-filter-modal');
    modalContainer.innerHTML = filterFormInvoiceDateTmpl();
    document.querySelector('#tpatomRadioother').checked = true;
    document.querySelector('.js-date-range-input-to').value = '2022-22-22';
    this.allPaymentsDateRange.validateDateRange();
    expect(document.querySelector('.js-date-range-to-error').classList.contains('hide')).to.be.false;
  });

  it('should disable the apply filter button, if from and to date are empty', function() {
    fetchStub.resolves({ json: () => Promise.resolve(allpaymentData)});
    this.allpayments.init();
    this.allpayments.itemsPerPage = 1;
    const modalContainer = document.body.querySelector('.js-filter-modal');
    modalContainer.innerHTML = filterFormInvoiceDateTmpl();
    document.querySelector('#tpatomRadioother').checked = true;
    document.querySelector('.js-date-range-input-to').value = '';
    document.querySelector('.js-date-range-input-from').value = '';
    this.allPaymentsDateRange.validateDateRange();
    expect( document.querySelector('.js-apply-filter-button').hasAttribute('disabled')).to.be.true;
  });

  it('should enable the apply filter button, if from and to date are valid', function() {
    fetchStub.resolves({ json: () => Promise.resolve(allpaymentData)});
    this.allpayments.init();
    this.allpayments.itemsPerPage = 1;
    const modalContainer = document.body.querySelector('.js-filter-modal');
    modalContainer.innerHTML = filterFormInvoiceDateTmpl();
    document.querySelector('#tpatomRadioother').checked = true;
    document.querySelector('.js-date-range-input-from').value = '2023-07-10';
    document.querySelector('.js-date-range-input-to').value = '2023-07-22';
    this.allPaymentsDateRange.validateDateRange();
    expect( document.querySelector('.js-apply-filter-button').hasAttribute('disabled')).to.be.false;
  });

  it('should disable the apply filter button, if from and to date are invalid', function() {
    fetchStub.resolves({ json: () => Promise.resolve(allpaymentData)});
    this.allpayments.init();
    this.allpayments.itemsPerPage = 1;
    const modalContainer = document.body.querySelector('.js-filter-modal');
    modalContainer.innerHTML = filterFormInvoiceDateTmpl();
    document.querySelector('#tpatomRadioother').checked = true;
    document.querySelector('.js-date-range-input-from').value = '2023-07-25';
    document.querySelector('.js-date-range-input-to').value = '2023-07-22';
    this.allPaymentsDateRange.validateDateRange();
    expect( document.querySelector('.js-apply-filter-button').hasAttribute('disabled')).to.be.true;
  });

  it('Should throw error, when user type wrong input value in to date with Febrauray month 28 and 29', function() {
    fetchStub.resolves({ json: () => Promise.resolve(allpaymentData)});
    this.allpayments.init();
    this.allpayments.itemsPerPage = 1;
    const modalContainer = document.body.querySelector('.js-filter-modal');
    modalContainer.innerHTML = filterFormInvoiceDateTmpl();
    document.querySelector('#tpatomRadioother').checked = true;
    document.querySelector('.js-date-range-input-to').value = '2023-02-29';
    this.allPaymentsDateRange.validateDateRange();
    expect(document.querySelector('.js-date-range-to-error').classList.contains('hide')).to.be.false;
    document.querySelector('.js-date-range-input-to').value = '2020-02-30';
    this.allPaymentsDateRange.validateDateRange();
    expect(document.querySelector('.js-date-range-to-error').classList.contains('hide')).to.be.false;

  });

  it('Should throw error, when user type wrong input value in to date inbut', function() {
    fetchStub.resolves({ json: () => Promise.resolve(allpaymentData)});
    this.allpayments.init();
    this.allpayments.itemsPerPage = 1;
    const modalContainer = document.body.querySelector('.js-filter-modal');
    modalContainer.innerHTML = filterFormInvoiceDateTmpl();
    document.querySelector('#tpatomRadioother').checked = true;
    document.querySelector('.js-date-range-input-to').value = '2023-09-31';
    this.allPaymentsDateRange.validateDateRange();
    expect(document.querySelector('.js-date-range-to-error').classList.contains('hide')).to.be.false;
  });

  it('Should throw error, when user type wrong input pattern in to date inbut', function() {
    fetchStub.resolves({ json: () => Promise.resolve(allpaymentData)});
    this.allpayments.init();
    this.allpayments.itemsPerPage = 1;
    const modalContainer = document.body.querySelector('.js-filter-modal');
    modalContainer.innerHTML = filterFormInvoiceDateTmpl();
    document.querySelector('#tpatomRadioother').checked = true;
    document.querySelector('.js-date-range-input-to').value = '20-09-2022';
    this.allPaymentsDateRange.validateDateRange();
    expect(document.querySelector('.js-date-range-to-error').classList.contains('hide')).to.be.false;
  });

  it('Should throw error, when user type wrong input pattern in to date inbut with ', function() {
    fetchStub.resolves({ json: () => Promise.resolve(allpaymentData)});
    this.allpayments.init();
    this.allpayments.itemsPerPage = 1;
    const modalContainer = document.body.querySelector('.js-filter-modal');
    modalContainer.innerHTML = filterFormInvoiceDateTmpl();
    document.querySelector('#tpatomRadioother').checked = true;
    document.querySelector('.js-date-range-input-to').value = '20-09-2022';
    this.allPaymentsDateRange.validateDateRange();
    expect(document.querySelector('.js-date-range-to-error').classList.contains('hide')).to.be.false;
  });

});
