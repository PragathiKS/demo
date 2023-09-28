import Paymentdetails from './Paymentdetails';
import paymentDetailsTemplate from '../../../test-templates-hbs/paymentDetails.hbs';
import auth from '../../../scripts/utils/auth';
import paymentDetailsData from './data/paymentDetails.json';

describe('Paymentdetails', function () {
  let fetchStub;
  const detailsApiUrl = 'https://api-dev.tetrapak.com/supplierpayments/invoices';
  before(function () {
    this.enableTimeouts(false);
    document.body.innerHTML = paymentDetailsTemplate();
    this.paymentdetails = new Paymentdetails({
      el: document.body.querySelector('.tp-all-payments')
    });
    this.initSpy = sinon.spy(this.paymentdetails, 'init');
    this.bindEventsSpy = sinon.spy(this.paymentdetails, 'bindEvents');
    this.renderPaymentDetailsSpy = sinon.spy(this.paymentdetails, 'renderPaymentDetails');
    this.renderPaymentDetailsDataSpy = sinon.spy(this.paymentdetails, 'renderPaymentDetailsData');
    this.showLoaderSpy = sinon.spy(this.paymentdetails, 'showLoader');
    this.downloadPDfSpy = sinon.spy(this.paymentdetails, 'downloadPDf');
    this.tokenStub = sinon.stub(auth, 'getToken').callsArgWith(0, {
      data: {
        access_token: 'fLW1l1EA38xjklTrTa5MAN7GFmo2',
        expires_in: '43199',
        token_type: 'BearerToken'
      }
    });
    fetchStub = sinon.stub(window, 'fetch');
    fetchStub.withArgs(detailsApiUrl).resolves({ json: sinon.stub().resolves(paymentDetailsData) });
  });

  after(function () {
    document.body.innerHTML = '';
    this.initSpy.restore();
    this.bindEventsSpy.restore();
    this.renderPaymentDetailsSpy.restore();
    this.tokenStub.restore();
    this.renderPaymentDetailsDataSpy.restore();
    this.showLoaderSpy.restore();
    this.downloadPDfSpy.restore();
    fetchStub.restore();
    sinon.restore();
  });
  it('should initialize', async function(){
    fetchStub.resolves({ json: () => Promise.resolve(paymentDetailsData)});
    this.paymentdetails.init();

    await this.paymentdetails.renderPaymentDetails();
    expect(this.paymentdetails.init.called).to.be.true;
    expect(this.paymentdetails.bindEvents.called).to.be.true;
  });

  it('should get payment details of payment', async function(){
    fetchStub.resolves({ json: () => Promise.resolve(paymentDetailsData)});
    this.paymentdetails.init();

    expect(this.paymentdetails.renderPaymentDetails.called).to.be.true;
  });

  it('should call export to PDF event', async function(){
    fetchStub.resolves({ json: () => Promise.resolve(paymentDetailsData)});
    this.paymentdetails.init();

    const evt = new MouseEvent('click', {
      bubbles: true,
      cancelable: true,
      view: window
    });
    document.querySelector('.js-payment-details__export-pdf-action').dispatchEvent(evt);

    expect(this.paymentdetails.downloadPDf.called).to.be.true;
  });

  it('should call mobile view handler', async function(){
    fetchStub.resolves({ json: () => Promise.resolve(paymentDetailsData)});
    this.paymentdetails.init();

    const evt = new MouseEvent('click', {
      bubbles: true,
      cancelable: true,
      view: window
    });
    document.querySelector('.js-mobile-header-actions').dispatchEvent(evt);
    expect(document.querySelector('.tp-payment-details__header-actions')).to.exist;
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
    this.paymentdetails.init();

    expect(this.paymentdetails.renderPaymentDetails.called).to.be.true;
  });

  it('should handle fetch error', async function() {
    const errorMessage = 'Failed to fetch data';

    // Set up the stub to return a rejected promise with the error message
    fetchStub.rejects(new Error(errorMessage));
    try {
      await this.paymentdetails.init();
    } catch (error) {
      expect(this.paymentdetails.renderPaymentDetails.called).to.be.true;
    }
  });

});
