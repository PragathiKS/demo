import Allpayments from './Allpayments';
import allpaymentsTmpl from '../../../test-templates-hbs/allpayments.hbs';
import auth from '../../../scripts/utils/auth';
import allpaymentData from './data/allpayments.json';

describe('Allpayments', function () {
  let fetchStub;

  beforeEach(function () {
    this.enableTimeouts(false);
    document.body.innerHTML = allpaymentsTmpl();

    this.allpayments = new Allpayments({
      el: document.body
    });

    this.initSpy = sinon.spy(this.allpayments, 'init');
    this.bindEventsSpy = sinon.spy(this.allpayments, 'bindEvents');
    this.renderPaymentSpy = sinon.spy(this.allpayments, 'renderPayment');
    this.paginationActionSpy = sinon.spy(this.allpayments, 'paginationAction');
    this.getPaymentApiUrlSpy = sinon.spy(this.allpayments, 'getPaymentApiUrl');
    this.sortActionSpy = sinon.spy(this.allpayments, 'sortAction');
    this.renderErrorTemplateSpy = sinon.spy(this.allpayments, 'renderErrorTemplate');

    this.tokenStub = sinon.stub(auth, 'getToken').callsArgWith(0, {
      data: {
        access_token: 'fLW1l1EA38xjklTrTa5MAN7GFmo2',
        expires_in: '43199',
        token_type: 'BearerToken'
      }
    });

    fetchStub = sinon.stub(window, 'fetch');
  });

  afterEach(function () {
    document.body.innerHTML = '';
    this.initSpy.restore();
    this.bindEventsSpy.restore();
    this.renderPaymentSpy.restore();
    this.tokenStub.restore();
    this.paginationActionSpy.restore();
    this.renderErrorTemplateSpy.restore();
    sinon.restore();
    fetchStub.restore();
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
    await this.allpayments.init();

    try {
      await this.allpayments.init();
    } catch (error) {
      expect(this.allpayments.renderErrorTemplate.called).to.be.true;
    }
  });

});
