import $ from 'jquery';
import CotsSupport from './CotsSupport';
import cotsSupportTemplate from '../../../test-templates-hbs/cotsSupport.hbs';

const helpers = {
  fillEmptyInputs() {
    $('input:text')
        .filter((idx, el) => !$(el).val())
        .val('Test data');
  }
}

describe('CotsSupport', function () {
  before(function () {
    $(document.body).empty().html(cotsSupportTemplate());
    this.cotsSupport = new CotsSupport({
      el: document.body,
    });
    this.initSpy = sinon.spy(this.cotsSupport, 'init');
    this.renderTypeOfQueryForm = sinon.spy(this.cotsSupport,'renderTypeOfQueryForm');
    this.renderConfirmationDetailsFormSpy = sinon.spy(this.cotsSupport,'renderConfirmationDetailsForm');
    this.renderSuccessMessageSpy = sinon.spy(this.cotsSupport,'renderSuccessMessage');
    this.addErrorMsgSpy = sinon.spy(this.cotsSupport, 'addErrorMsg');
    this.cotsSupport.init();
  });

  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.renderTypeOfQueryForm.restore();
    this.renderConfirmationDetailsFormSpy.restore();
    this.renderSuccessMessageSpy.restore();
    this.addErrorMsgSpy.restore();
  });

  it('should initialize', function (done) {
    expect(this.initSpy.called).to.be.true;
    done();
  });

  it('should render type of query form', function (done) {
    expect(this.renderTypeOfQueryForm.called).to.be.true;
    done();
  });

  it('should render form validation errors', function (done) {
    $('.js-tp-cots-support__submit-type-of-query').trigger('click');
    expect(this.renderConfirmationDetailsFormSpy.called).to.be.false;
    expect(this.addErrorMsgSpy.called).to.be.true;
    done();
  });

  it('should render confirmation details form after type of query submit', function (done) {
    $('#technicalIssues[value=technicalIssues]').prop('checked', true);
    $('#company').val('The Milk Company');
    $('#customerSite').val('Test Data');
    $('#affectedSystems option:eq(1)').prop('selected', true);
    $('#softwareVersion').val('1.0.0');
    $('#licensenumber').val('123456');
    $('#description').val('Test description');
    $('#questions').val('Test content');
    $('.js-tp-cots-support__submit-type-of-query').trigger('click');
    expect(this.renderConfirmationDetailsFormSpy.called).to.be.true;
    done();
  });

  it('should render success message after confirmation detail submit', function (done) {
    $('#name').val('Test name');
    $('#emailAddress').val('example@example.com');
    $('#telephone').val('461234567890');
    $('.js-tp-cots-support__submit-confirmation-details').trigger('click');
    expect(this.renderSuccessMessageSpy.called).to.be.true;
    done();
  });

  it('should handle config read error', function () {
    $(document.body).empty().html(cotsSupportTemplate());
    $('.js-tp-cots-support__config').remove();
    this.cotsSupport = new CotsSupport({
      el: document.body,
    });
    this.cotsSupport.init();
    expect(this.cotsSupport.cache.i18nKeys).to.be.deep.equal({});
  });
});
