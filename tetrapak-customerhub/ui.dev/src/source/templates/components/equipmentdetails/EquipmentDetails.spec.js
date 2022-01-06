import $ from 'jquery';
import EquipmentDetails from './EquipmentDetails';
import equipmentDetailsData from './data/equipmentdetails.json';
import equipmentDetailsTemplate from '../../../test-templates-hbs/equipmentdetails.hbs';
import { render } from '../../../scripts/utils/render';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import auth from '../../../scripts/utils/auth';

describe('EquipmentDetails', function () {
  const jqRef = {
    setRequestHeader() {
      // Dummy method
    }
  };
  function setDom($this) {
    $(document.body).empty().html(equipmentDetailsTemplate());
    $this.equipmentDetails = new EquipmentDetails({
      el: document.body
    });
  }
  function ajaxResponse(response, operation = 'success') {
    const pr = $.Deferred();
    if(operation === 'success') {
      pr.resolve(response, operation, jqRef);
    } else {
      pr.reject(response);
    }
    
    return pr.promise();
  };
  before(function () {
    setDom(this);
    this.initSpy = sinon.spy(this.equipmentDetails, 'init');
    this.bindEventsSpy = sinon.spy(this.equipmentDetails, 'bindEvents');
    this.renderEquipmentDetailsSpy = sinon.spy(this.equipmentDetails, 'renderEquipmentDetails');
    this.renderEquipInfoCardSpy = sinon.spy(this.equipmentDetails, 'renderEquipInfoCard')
    this.renderEquipUpdateModalSpy = sinon.spy(this.equipmentDetails, 'renderEquipUpdateModal');

    this.removeErrorMsgSpy = sinon.spy(this.equipmentDetails, 'removeErrorMsg');
    this.removeAllErrorMessagesSpy = sinon.spy(this.equipmentDetails, 'removeAllErrorMessages');
    this.trackLinkClickSpy = sinon.spy(this.equipmentDetails, 'trackLinkClick');
    this.trackFormStartSpy = sinon.spy(this.equipmentDetails, 'trackFormStart');
    this.trackFormStepCompleteSpy = sinon.spy(this.equipmentDetails, 'trackFormStepComplete');
    this.trackFormCompleteSpy = sinon.spy(this.equipmentDetails, 'trackFormComplete');
    this.trackFormCancelSpy = sinon.spy(this.equipmentDetails, 'trackFormCancel');
    this.trackFormErrorSpy = sinon.spy(this.equipmentDetails, 'trackFormError');
    this.renderSpy = sinon.spy(render, 'fn');
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse(equipmentDetailsData));
    this.openStub = sinon.stub(window, 'open');
    this.tokenStub = sinon.stub(auth, 'getToken').callsArgWith(0, {
      data: {
        access_token: 'fLW1l1EA38xjklTrTa5MAN7GFmo2',
        expires_in: '43199',
        token_type: 'BearerToken'
      }
    });
    this.equipmentDetails.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.trackLinkClickSpy.restore();
    this.trackFormStartSpy.restore();
    this.trackFormStepCompleteSpy.restore();
    this.trackFormCompleteSpy.restore();
    this.trackFormCancelSpy.restore();
    this.trackFormErrorSpy.restore();
    this.renderEquipmentDetailsSpy.restore();
    this.renderEquipUpdateModalSpy.restore();
    this.renderEquipInfoCardSpy.restore();
    this.renderSpy.restore();
    this.ajaxStub.restore();
    this.openStub.restore();
    this.tokenStub.restore();
  });

  beforeEach(function() {
    this.renderSpy.resetHistory();
    this.renderEquipmentDetailsSpy.resetHistory();
    this.renderEquipInfoCardSpy.resetHistory();
    this.removeAllErrorMessagesSpy.resetHistory();
    this.trackFormStartSpy.resetHistory();
    this.trackFormCancelSpy.resetHistory();
    this.trackFormStepCompleteSpy.resetHistory();
    this.trackFormErrorSpy.resetHistory();
  });

  it('should call trackFormStart when form is rendered ', function(done) {
    setDom(this);
    $('.js-equipment-details__update').trigger('click')
    expect(this.trackFormStartSpy.calledOnce).to.be.true;
    done();
  });

  // it('should not submit form when no fields changed', function (done) {
  //   setDom(this);
  //   $('.js-equipment-details__req-update').trigger('click');
  //   expect(this.removeAllErrorMessagesSpy.calledOnce).to.be.true;
  //   // expect(this.trackFormErrorSpy.calledOnce).to.be.true;
  //   expect(this.renderEquipUpdateModalSpy.called).to.be.false;
  //   done();
  // });

  // it('should not submit form when no required fields are provided', function (done) {
  //   setDom(this);
  //   $('#comments').val('0070');
  //   $('.js-equipment-details__req-update').trigger('click');
  //   expect(this.removeAllErrorMessagesSpy.calledOnce).to.be.true;
  //   expect(this.renderEquipUpdateModalSpy.called).to.be.false;
  //   done();
  // });

  // it('should submit form and open modal when required fields are not empty', function (done) {
  //   setDom(this);
  //   $('#country').val('DE');
  //   $('#equipmentStatus').val('STCU');
  //   $('#equipmentTypeDesc').val('Steriltank');
  //   $('#position').val('0070');
  //   $('#siteName').val('Biesenhofen');
  //   $('#location').val('DL');
  //   $('#lineName').val('BASE');
  //   $('#comments').val('Test');
  //
  //   $('.js-equipment-details__req-update').trigger('click');
  //   // expect(this.trackFormStepCompleteSpy.calledOnce).to.be.true;
  //   expect(this.renderEquipUpdateModalSpy.calledOnce).to.be.true;
  //   done();
  // });

  it('should get country list and it should be equal to 2', function (done) {
    expect($('#country').children).to.have.length(2);
    done();
  });

  it('should not show thank you page when api does not work', function (done) {
    setDom(this);
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse({ status: 400 }), 'fail');
    $('.js-equipment-details__req-make-update').trigger('click');
    expect($('.js-equipment-details__error').hasClass('d-none')).to.be.false;
    done();
  });

  it('should show thank you page when api works', function (done) {
    setDom(this);
    $('#location').val('Berlin');
    $('#country').val('Germany');
    $('#siteName').val('Hannover');
    $('#lineName').val('Test2');
    $('#position').val('0080');
    $('#equipmentStatus').val('In Prod');
    $('#equipmentTypeDesc').val('In Prod');
    $('.js-equipment-details__req-update').trigger('click');
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse({ status: 201 }));
    $('.js-equipment-details__req-make-update').trigger('click');
    expect(this.trackFormStepCompleteSpy.calledOnce).to.be.true;
    expect($('.tp-equipment-details__conf-title')).to.exist;
    done();
  });

  it('should close modal on close icon click', function (done) {
    setDom(this);
    $('.js-close-btn').trigger('click');
    expect($('.equipment-update-modal').hasClass('show')).to.be.false;
    expect(this.trackFormCancelSpy.calledOnce).to.be.true;
    done();
  });

  it('should call trackFormCancel when clicked on cancel button', function (done) {
    $('.js-equipment-details__conf-cancel').trigger('click');
    expect(this.trackFormCancelSpy.calledOnce).to.be.true;
    done();
  });
});
