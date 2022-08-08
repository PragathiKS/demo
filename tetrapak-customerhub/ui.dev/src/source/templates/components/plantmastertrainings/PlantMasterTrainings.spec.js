import $ from 'jquery';
import PlantMasterTrainings from './PlantMasterTrainings';
import PlantMasterTrainingsTemplate from '../../../test-templates-hbs/plantMasterTrainings.hbs';
import {ajaxWrapper} from "../../../scripts/utils/ajax";
import auth from "../../../scripts/utils/auth";
import {render} from "../../../scripts/utils/render";
import plantMasterTrainingsData from "./data/plantMasterTrainings.json";
import plantMasterLearningHistoryData from "./data/plantMasterLearningHistory.json";
import plantMasterUserGroup from "./data/plantMasterUserGroup.json"

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
    $(document.body).empty().html(PlantMasterTrainingsTemplate());
    this.plantMasterTrainings = new PlantMasterTrainings({
      el: document.body,
    });
    this.initSpy = sinon.spy(this.plantMasterTrainings, 'init');
    this.bindEventsSpy = sinon.spy(this.plantMasterTrainings, 'bindEvents');
    this.getUserGroupSpy = sinon.spy(this.plantMasterTrainings,'getUserGroup');
    this.getTrainingsDataSpy = sinon.spy(this.plantMasterTrainings,'getTrainingsData');
    this.getLearningHistoryDataSpy = sinon.spy(this.plantMasterTrainings,'getLearningHistoryData');
    this.renderLearningHistorySpy = sinon.spy(this.plantMasterTrainings,'renderLearningHistory');
    this.renderTrainingsSpy = sinon.spy(this.plantMasterTrainings,'renderTrainings');
    this.processTrainingsDataSpy = sinon.spy(this.plantMasterTrainings,'processTrainingsData');
    this.processLearningHistoryDataSpy = sinon.spy(this.plantMasterTrainings,'processLearningHistoryData');
    this.renderSpy = sinon.spy(render, 'fn');
    this.removeAllErrorMessagesSpy = sinon.spy(this.plantMasterTrainings, 'removeAllErrorMessages');
    this.addErrorMsgSpy = sinon.spy(this.plantMasterTrainings, 'addErrorMsg');
    this.handleFormSubmitSpy = sinon.spy(this.plantMasterTrainings, 'handleFormSubmit');

    this.trackAccordionClickSpy = sinon.spy(this.plantMasterTrainings, 'trackAccordionClick');
    this.trackFormErrorSpy = sinon.spy(this.plantMasterTrainings, 'trackFormError');
    this.trackFormCompleteSpy = sinon.spy(this.plantMasterTrainings, 'trackFormComplete');
    this.trackFormStartSpy = sinon.spy(this.plantMasterTrainings, 'trackFormStart');

    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse(plantMasterUserGroup));
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
    this.bindEventsSpy.restore();
    this.getTrainingsDataSpy.restore();
    this.getLearningHistoryDataSpy.restore();
    this.renderLearningHistorySpy.restore();
    this.renderTrainingsSpy.restore();
    this.getUserGroupSpy.restore();
    this.processTrainingsDataSpy.restore();
    this.processLearningHistoryDataSpy.restore();
    this.renderSpy.restore();
    this.ajaxStub.restore();
    this.tokenStub.restore();
    this.removeAllErrorMessagesSpy.restore();
    this.addErrorMsgSpy.restore();
    this.handleFormSubmitSpy.restore();
    this.trackAccordionClickSpy.restore();
    this.trackFormErrorSpy.restore();
    this.trackFormCompleteSpy.restore();
    this.trackFormStartSpy.restore();
  });

  it('should initialize', function (done) {
    expect(this.initSpy.called).to.be.true;
    expect(this.bindEventsSpy.called).to.be.true;
    done();
  });

  it('should call get trainings User Group', function (done) {
    expect(this.getUserGroupSpy.called).to.be.true;
    done();
  });

  it('should call and render trainings data', function (done) {
    this.ajaxStub.restore();
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse(plantMasterTrainingsData));
    expect(this.getTrainingsDataSpy.called).to.be.true;
    expect(this.renderTrainingsSpy.called).to.be.true;
    expect(render.fn.called).to.be.true;
    done();
  });

  it('should call and render learning history data', function (done) {
    this.ajaxStub.restore();
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse(plantMasterLearningHistoryData));
    expect(this.getLearningHistoryDataSpy.called).to.be.true;
    expect(this.renderLearningHistorySpy.called).to.be.true;
    expect(render.fn.called).to.be.true;
    done();
  });

  it('should show error messages on empty form submit', function (done) {
    $(document.body).empty().html(PlantMasterTrainingsTemplate());
    $('.js-aip-trainings__form').trigger('submit');
    expect(this.removeAllErrorMessagesSpy.called).to.be.true;
    expect(this.addErrorMsgSpy.called).to.be.true;
    expect(this.trackFormErrorSpy.called).to.be.true;
    done();
  });

  it('should submit training form', function (done) {
    $(document.body).empty().html(PlantMasterTrainingsTemplate());

    this.ajaxStub.restore();
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.returns(ajaxResponse({"result":"Success","status":202}));

    $('#noOfParticipants-0').val('2');
    $('#preferredLocation-0').val('Test');
    $('#preferredDate-0').val('2030-10-10');
    $('#comments-0').val('Test');
    $('#tpatomCheckconsent-0').attr('checked', true);
    $('.js-aip-trainings__form').trigger('submit');
    expect(this.handleFormSubmitSpy.called).to.be.true;
    expect(this.removeAllErrorMessagesSpy.called).to.be.true;
    expect(this.trackFormCompleteSpy.called).to.be.true;
    done();
  });

  it('should track accordion click', function (done) {
    $(document.body).empty().html(PlantMasterTrainingsTemplate());
    $('.js-aip-trainings__accordion .btn-link').trigger('click');
    expect(this.trackAccordionClickSpy.called).to.be.true;
    done();
  });

  it('should track form start', function (done) {
    $(document.body).empty().html(PlantMasterTrainingsTemplate());
    $('#preferredLocation-0').val('Test').trigger('change');
    expect(this.trackFormStartSpy.called).to.be.true;
    done();
  });
});
