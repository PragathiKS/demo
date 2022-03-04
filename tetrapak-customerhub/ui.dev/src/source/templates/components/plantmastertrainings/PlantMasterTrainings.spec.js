import $ from 'jquery';
import PlantMasterTrainings from './PlantMasterTrainings';
import plantMasterTrainingsTemplate from '../../../test-templates-hbs/plantMasterTrainings.hbs';
import {ajaxWrapper} from "../../../scripts/utils/ajax";
import auth from "../../../scripts/utils/auth";
import {render} from "../../../scripts/utils/render";
import plantMasterTrainingsData from "./data/plantMasterTrainings.json";

describe('PlantMasterTrainings', function () {
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
    $(document.body).empty().html(plantMasterTrainingsTemplate());
    this.plantMasterTrainings = new PlantMasterTrainings({
      el: document.body,
    });
    this.initSpy = sinon.spy(this.plantMasterTrainings, 'init');
    this.getTrainingsDataSpy = sinon.spy(this.plantMasterTrainings,'getTrainingsData');
    this.renderSpy = sinon.spy(render, 'fn');
    this.removeAllErrorMessagesSpy = sinon.spy(this.plantMasterTrainings, 'removeAllErrorMessages');
    this.addErrorMsgSpy = sinon.spy(this.plantMasterTrainings, 'addErrorMsg');
    this.handleFormSubmitSpy = sinon.spy(this.plantMasterTrainings, 'handleFormSubmit');

    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse(plantMasterTrainingsData));
    this.tokenStub = sinon.stub(auth, 'getToken').callsArgWith(0, {
      data: {
        access_token: "fLW1l1EA38xjklTrTa5MAN7GFmo2",
        expires_in: "43199",
        token_type: "BearerToken"
      }
    });

    this.plantMasterTrainings.init();
  });

  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.getTrainingsDataSpy.restore();
    this.renderSpy.restore();

    this.ajaxStub.restore();
    this.tokenStub.restore();
    this.removeAllErrorMessagesSpy.restore();
    this.addErrorMsgSpy.restore();
    this.handleFormSubmitSpy.restore();
  });

  it('should initialize', function (done) {
    expect(this.initSpy.called).to.be.true;
    done();
  });

  it('should call get trainings data', function (done) {
    expect(this.getTrainingsDataSpy.called).to.be.true;
    done();
  });

  it('should get and render trainings data', function (done) {
    this.ajaxStub.restore();
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse(plantMasterTrainingsData));
    expect(render.fn.called).to.be.true;
    done();
  });

  it('should render form validation errors', function (done) {
    $('.js-aip__submit').trigger('click');
    expect(this.removeAllErrorMessagesSpy.called).to.be.true;
    expect(this.addErrorMsgSpy.called).to.be.true;
    done();
  });

  it('should submit form', function (done) {
    $('.js-aip__submit').trigger('click');
    expect(this.removeAllErrorMessagesSpy.called).to.be.true;
    expect(this.handleFormSubmitSpy.called).to.be.true;
    done();
  });
});
