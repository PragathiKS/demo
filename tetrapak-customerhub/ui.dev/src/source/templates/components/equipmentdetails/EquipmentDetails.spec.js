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
    $(document.body).empty().html(equipmentDetailsTemplate());
    this.equipmentDetails = new EquipmentDetails({
      el: document.body
    });
    this.initSpy = sinon.spy(this.equipmentDetails, 'init');
    this.renderEquipmentDetailsSpy = sinon.spy(this.equipmentDetails, 'renderEquipmentDetails');
    this.renderEquipInfoCardSpy = sinon.spy(this.equipmentDetails, 'renderEquipInfoCard')
    this.renderEquipUpdateModalSpy = sinon.spy(this.equipmentDetails, 'renderEquipUpdateModal');
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
    this.renderEquipmentDetailsSpy.restore();
    this.renderEquipUpdateModalSpy.restore();
    this.renderEquipInfoCardSpy.restore();
    this.renderSpy.restore();
    this.ajaxStub.restore();
    this.openStub.restore();
    this.tokenStub.restore();
  });
  it('should initialize', function (done) {
    expect(this.equipmentDetails.init.called).to.be.true;
    done();
  });
  it('should render component on page load', function (done) {
    expect(render.fn.called).to.be.true;
    done();
  });
  it('should render equipment details sections', function (done) {
    expect(this.equipmentDetails.renderEquipmentDetails.called).to.be.true;
    done();
  });

  it('should remove disallowed script tag from input', function (done) {
    const $el = $('#comments');
    $el.val('<script>test</script>');
    $el.trigger('blur');
    expect($el.val()).to.equal('test');
    done();
  });

  it('should not submit form when no fields changed', function (done) {
    this.equipmentDetails.cache.$updateBtn.click();
    expect(this.renderEquipUpdateModalSpy.called).to.be.false;
    done();
  });

  it('should submit form and open modal when required fields are not empty', function (done) {
    $('#comments').val('Test');
    $('.js-equipment-details__req-update').trigger('click');
    expect(this.renderEquipUpdateModalSpy.called).to.be.true;
    done();
  });

  it('should get country list and it should be equal to 2', function () {
    expect($('#country').children).to.have.length(2);
  });

  it('should not show thank you page when api does not work', function (done) {
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse({ status: 400 }), 'fail');
    $('.js-equipment-details__req-make-update').trigger('click');
    expect($('.js-equipment-details__error').hasClass('d-none')).to.be.true;
    done();
  });

  it('should show thank you page when api works', function (done) {
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse({ status: 201 }));
    $('.js-equipment-details__req-make-update').trigger('click');
    expect($('.tp-equipment-details__conf-title')).to.exist;
    done();
  });

  it('should close modal on close icon click', function (done) {
    $('.js-close-btn').trigger('click');
    expect($('.equipment-update-modal').hasClass('show')).to.be.false;
    done();
  });
});
