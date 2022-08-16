import $ from 'jquery';
import PlantMasterLicensesActive from './PlantMasterLicenses-active';
import PlantMasterEngLicensesData from './data/plantMasterLicenses-engineering.json';
import PlantMasterLicensesTemplate from '../../../test-templates-hbs/plantMasterLicenses.hbs';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import auth from '../../../scripts/utils/auth';
import {render} from "../../../scripts/utils/render";

describe('PlantMasterLicensesActive', function () {

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
    this.activeLicenses = new PlantMasterLicensesActive(
      document.body,
    );

    this.initSpy = sinon.spy(this.activeLicenses, 'init');
    this.renderSpy = sinon.spy(render, 'fn');
    this.bindEventsSpy = sinon.spy(this.activeLicenses, 'bindEvents');
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse(PlantMasterEngLicensesData));
    this.tokenStub = sinon.stub(auth, 'getToken').callsArgWith(0, {
      data: {
        access_token: 'fLW1l1EA38xjklTrTa5MAN7GFmo2',
        expires_in: '43199',
        token_type: 'BearerToken'
      }
    });
    this.getActiveLicensesDataSpy = sinon.spy(this.activeLicenses, 'getActiveLicensesData');
    this.renderActiveLicensesSpy = sinon.spy(this.activeLicenses, 'renderActiveLicenses');
    this.renderLicenseWithdrawModalSpy = sinon.spy(this.activeLicenses, 'renderLicenseWithdrawModal');
    this.submitLicenseWithdrawSpy = sinon.spy(this.activeLicenses, 'submitLicenseWithdraw');
    this.renderLicenseWithdrawSuccessSpy = sinon.spy(this.activeLicenses, 'renderLicenseWithdrawSuccess');
    this.trackAccordionClickSpy = sinon.spy(this.activeLicenses, 'trackAccordionClick');
    this.trackWithdrawStartSpy = sinon.spy(this.activeLicenses, 'trackWithdrawStart');
    this.trackWithdrawCancelSpy = sinon.spy(this.activeLicenses, 'trackWithdrawCancel');
    this.trackWithdrawCompleteSpy = sinon.spy(this.activeLicenses, 'trackWithdrawComplete');

    this.activeLicenses.init();
  });

  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.bindEventsSpy.restore();
    this.ajaxStub.restore();
    this.tokenStub.restore();
    this.renderSpy.restore();
    this.getActiveLicensesDataSpy.restore();
    this.renderActiveLicensesSpy.restore();
    this.renderLicenseWithdrawModalSpy.restore();
    this.submitLicenseWithdrawSpy.restore();
    this.renderLicenseWithdrawSuccessSpy.restore();
    this.trackAccordionClickSpy.restore();
    this.trackWithdrawStartSpy.restore();
    this.trackWithdrawCancelSpy.restore();
    this.trackWithdrawCompleteSpy.restore();
  });

  it('should initialize', function (done) {
    expect(this.initSpy.called).to.be.true;
    expect(this.bindEventsSpy.called).to.be.true;
    expect(this.getActiveLicensesDataSpy.called).to.be.true;
    done();
  });

  it('should get data before rendering Active License sections', function (done) {
    expect(this.getActiveLicensesDataSpy.called).to.be.true;
    expect(this.renderActiveLicensesSpy.called).to.be.true;
    done();
  });

  it('should submit data and show success message when confirming withdraw', function (done) {
    $(document.body).empty().html(PlantMasterLicensesTemplate());
    $('.js-tp-aip-licenses-active__confirm').trigger('click');
    expect(this.submitLicenseWithdrawSpy.called).to.be.true;
    expect(this.renderLicenseWithdrawSuccessSpy.called).to.be.true;
    expect(this.trackWithdrawCompleteSpy.called).to.be.true;
    done();
  });

  it('should track accordion click', function (done) {
    $(document.body).empty().html(PlantMasterLicensesTemplate());
    $('.tp-aip-licenses-active .btn-link').trigger('click');
    expect(this.trackAccordionClickSpy.called).to.be.true;
    done();
  });

  it('should track Withdraw start', function (done) {
    $(document.body).empty().html(PlantMasterLicensesTemplate());
    $('.js-tp-aip-licenses-active__withdraw').trigger('click');
    expect(this.trackWithdrawStartSpy.called).to.be.true;
    done();
  });

  it('should track Withdraw cancel', function (done) {
    $(document.body).empty().html(PlantMasterLicensesTemplate());
    $('.js-tp-aip-licenses-active__withdraw').trigger('click');
    $('.js-tp-aip-licenses-active__cancel').trigger('click');
    expect(this.trackWithdrawCancelSpy.called).to.be.true;
    done();
  });
})
