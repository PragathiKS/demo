import $ from 'jquery';
import PlantMasterLicensesEngineering from './PlantMasterLicenses-engineering';
import PlantMasterEngLicensesData from './data/plantMasterLicenses-engineering.json';
import PlantMasterLicensesTemplate from '../../../test-templates-hbs/PlantMasterLicenses.hbs';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import auth from '../../../scripts/utils/auth';
import {render} from "../../../scripts/utils/render";

describe('PlantMasterLicensesEngineering', function () {

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
    $(document.body).empty().html(PlantMasterLicensesTemplate());
    this.engineeringLicenses = new PlantMasterLicensesEngineering(
      document.body,
    );

    this.initSpy = sinon.spy(this.engineeringLicenses, 'init');
    this.renderSpy = sinon.spy(render, 'fn');
    this.bindEventsSpy = sinon.spy(this.engineeringLicenses, 'bindEvents');
    this.getEngineeringLicensesDataSpy = sinon.spy(this.engineeringLicenses, 'getEngineeringLicensesData');
    this.renderEngLicensesDescSpy = sinon.spy(this.engineeringLicenses, 'renderEngLicensesDesc');
    this.renderLicenseHoldersSpy = sinon.spy(this.engineeringLicenses, 'renderLicenseHolders');
    this.showContentSpy = sinon.spy(this.engineeringLicenses, 'showContent');
    this.renderSuccessMessageSpy = sinon.spy(this.engineeringLicenses, 'renderSuccessMessage');
    this.showSpinnerSpy = sinon.spy(this.engineeringLicenses, 'showSpinner');
    this.addErrorMsgSpy = sinon.spy(this.engineeringLicenses, 'addErrorMsg');
    this.removeAllErrorMessagesSpy = sinon.spy(this.engineeringLicenses, 'removeAllErrorMessages');
    this.getLicenseCheckboxDataSpy = sinon.spy(this.engineeringLicenses, 'getLicenseCheckboxData');
    this.submitRequestFormSpy = sinon.spy(this.engineeringLicenses, 'submitRequestForm');
    this.validateLicenseHolderSpy = sinon.spy(this.engineeringLicenses, 'validateLicenseHolder');
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse(PlantMasterEngLicensesData));
    this.tokenStub = sinon.stub(auth, 'getToken').callsArgWith(0, {
      data: {
        access_token: 'fLW1l1EA38xjklTrTa5MAN7GFmo2',
        expires_in: '43199',
        token_type: 'BearerToken'
      }
    });
    this.engineeringLicenses.init();
  });

  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.bindEventsSpy.restore();
    this.getEngineeringLicensesDataSpy.restore();
    this.renderEngLicensesDescSpy.restore();
    this.renderLicenseHoldersSpy.restore();
    this.renderSuccessMessageSpy.restore();
    this.showContentSpy.restore();
    this.showSpinnerSpy.restore();
    this.addErrorMsgSpy.restore();
    this.removeAllErrorMessagesSpy.restore();
    this.ajaxStub.restore();
    this.tokenStub.restore();
    this.renderSpy.restore();
    this.submitRequestFormSpy.restore();
    this.validateLicenseHolderSpy.restore();
    this.getLicenseCheckboxDataSpy.restore();
  });

  it('should initialize', function (done) {
    expect(this.initSpy.called).to.be.true;
    expect(this.bindEventsSpy.called).to.be.true;
    expect(this.getEngineeringLicensesDataSpy.called).to.be.true;
    done();
  });

  it('should get data before rendering Engineering License sections', function (done) {
    expect(this.getEngineeringLicensesDataSpy.called).to.be.true;
    expect(this.renderEngLicensesDescSpy.called).to.be.true;
    expect(this.renderLicenseHoldersSpy.called).to.be.true;
    expect(this.showContentSpy.called).to.be.true;
    done();
  });

  it('should render License Description section', function (done) {
    expect(this.renderEngLicensesDescSpy.called).to.be.true;
    expect(render.fn.called).to.be.true;
    done();
  });

  it('should render License Holder section', function (done) {
    expect(this.renderLicenseHoldersSpy.called).to.be.true;
    expect(this.getLicenseCheckboxDataSpy.called).to.be.true;
    expect(render.fn.called).to.be.true;
    done();
  });

  it('should render form validation errors', function (done) {
    $('.js-tp-aip-licenses-eng__btn').trigger('click');
    expect(this.validateLicenseHolderSpy.called).to.be.true;
    expect(this.removeAllErrorMessagesSpy.called).to.be.true;
    expect(this.addErrorMsgSpy.called).to.be.true;
    done();
  });

  it('should add new user', function (done) {
    expect($('.js-tp-aip-licenses-eng__new-holder').length).to.equal(1);
    $('.js-tp-aip-licenses-eng__add-user').trigger('click');
    expect($('.js-tp-aip-licenses-eng__new-holder').length).to.equal(2);
    done();
  });

  it('should remove user', function (done) {
    expect($('.js-tp-aip-licenses-eng__new-holder').length).to.equal(2);
    $('.js-tp-aip-licenses-eng__remove-user').trigger('click');
    expect($('.js-tp-aip-licenses-eng__new-holder').length).to.equal(1);
    done();
  });

  it('should submit form', function (done) {
    this.ajaxStub.restore();
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.returns(ajaxResponse({"result":"Success","status":202}));

    $('#licenseHolderName-1').val('Test Data');
    $('#activationDate-1').val('2022-03-17');
    $('.js-tp-aip-licenses__checkbox-group .tpatom-checkbox__input').first().prop('checked', true);
    $('.js-tp-aip-licenses-eng__btn').trigger('click');
    expect(this.validateLicenseHolderSpy.called).to.be.true;
    expect(this.submitRequestFormSpy.called).to.be.true;
    expect(this.showContentSpy.called).to.be.true;
    done();
  });
})
