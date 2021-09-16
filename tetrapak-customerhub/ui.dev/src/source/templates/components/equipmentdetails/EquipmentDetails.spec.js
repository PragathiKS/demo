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
  function ajaxResponse(response) {
    const pr = $.Deferred();
    pr.resolve(response, 'success', jqRef);
    return pr.promise();
  };
  before(function () {
    this.equipmentDetails = new EquipmentDetails({
      el: document.body
    });
    $(document.body).empty().html(equipmentDetailsTemplate());
    this.initSpy = sinon.spy(this.equipmentDetails, 'init');
    this.renderEquipmentDetailsSpy = sinon.spy(this.equipmentDetails, 'renderEquipmentDetails');
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
});
